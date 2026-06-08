#!/usr/bin/env node

/**
 * Skill file and repository-context auditor.
 *
 * Validates:
 *   - .agents/skills/*\/SKILL.md frontmatter fields (name, description, etc.)
 *   - AGENTS.md, .agents/skills/**\/*.md, docs/support-arch/**\/*.md,
 *     docs/superpowers/**\/*.md for stale and broken references.
 *
 * Enforces the Agent Skills specification:
 *   https://github.com/agentskills/agentskills
 *
 * Requires: npm ci (uses gray-matter from package.json)
 * Usage:    node .github/scripts/validate-skills.js [repo-root]
 */

const fs = require("fs");
const path = require("path");
const os = require("os");

// ---------------------------------------------------------------------------
// Constants
// ---------------------------------------------------------------------------

const NAME_MAX_LENGTH = 64;
const NAME_PATTERN = /^[a-z0-9]+(-[a-z0-9]+)*$/;
const DESCRIPTION_MAX_LENGTH = 1024;
const COMPATIBILITY_MAX_LENGTH = 500;
const BODY_MAX_LINES = 500;

const KNOWN_FRONTMATTER_FIELDS = new Set([
  "name",
  "description",
  "license",
  "compatibility",
  "metadata",
  "allowed-tools",
  // Repo-specific extensions (not part of the Agent Skills spec)
  "argument-hint",
]);

// Known top-level repo path prefixes to validate when they appear in backticks.
const KNOWN_PATH_PREFIXES = [
  "AGENTS.md", ".agents/", ".github/", "android/", "app/", "buildSrc/",
  "common/", "data/", "domain/", "docs/", "feature/", "gradle/", "task/",
  "package.json", "package-lock.json"
];

// Patterns that indicate a placeholder, not a real path.
const PLACEHOLDER_PATTERNS = [
  /<.*>/, /{.*}/, /\.\.\./, /\bpath\/to\b/i, /\bexample\b/i
];

// SHA-pinned GitHub blob link pattern.
const SHA_BLOB_RE = /github\.com\/[^/\s]+\/[^/\s]+\/blob\/[0-9a-f]{40}\/[^\s)]+#L\d+/g;

// Stale reference patterns that must fail.
const STALE_REFERENCES = [
  { re: /\.github\/skills/gi, label: ".github/skills (obsolete directory)" },
  { re: /\.github\/instructions\/.*\.instructions\.md/gi, label: ".github/instructions/*.instructions.md (removed from routing)" },
];

// Repo-local code-review-graph skill prefix.
const CRG_SKILL_PREFIX = "code-review-graph-";

// Markdown file globs for repository-context validation.
const REPO_CONTEXT_GLOBS = [
  "AGENTS.md",
  ".agents/skills/**/*.md",
  "docs/support-arch/**/*.md",
  "docs/superpowers/**/*.md",
];

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

let matter;
try {
  matter = require("gray-matter");
} catch (_) {
  console.error(
    "gray-matter is required. Install it with: npm install gray-matter"
  );
  process.exit(2);
}

/**
 * Parse SKILL.md file with gray-matter. Returns { data, content, lines }
 * or null if the file is unreadable. `lines` is the raw line array for
 * position calculations.
 */
function parseSkillFile(filePath) {
  const raw = fs.readFileSync(filePath, "utf-8");
  const lines = raw.split("\n");
  const parsed = matter(raw);

  let bodyStartLine = 0;
  if (lines[0]?.trim() === "---") {
    const endIdx = lines.slice(1).findIndex((l) => l.trim() === "---");
    if (endIdx !== -1) {
      bodyStartLine = endIdx + 2;
    }
  }

  return { data: parsed.data, content: parsed.content, lines, bodyStartLine };
}

/**
 * Return true when `target` is a cross-skill reference (starts with ../).
 */
function isCrossSkillLink(target) {
  return target.startsWith("../");
}

/**
 * Calculate how many directory levels deep a relative path goes from
 * the skill root.  `./references/foo.md` = 1, `./references/a/b.md` = 2.
 */
function linkDepth(target) {
  const parts = target.replace(/^\.\//, "").split("/");
  return parts.length - 1;
}

/**
 * Extract markdown links of the form [text](target) from body text.
 */
function extractRelativeLinks(content) {
  const re = /\[([^\]]*)\]\(([^)]+)\)/g;
  const links = [];
  let m;
  while ((m = re.exec(content)) !== null) {
    const target = m[2];
    if (target.startsWith("./") || target.startsWith("../")) {
      links.push({ text: m[1], target, pos: m.index });
    }
  }
  return links;
}

// ---------------------------------------------------------------------------
// Field validators
// ---------------------------------------------------------------------------

function validateName(data, dirName, fileRel) {
  const issues = [];
  const raw = data.name;

  if (raw === undefined || raw === null || String(raw).trim().length === 0) {
    issues.push({
      file: fileRel,
      line: 1,
      message: 'Required frontmatter field "name" is missing or empty',
    });
    return issues;
  }

  const name = String(raw).trim();

  if (name.length > NAME_MAX_LENGTH) {
    issues.push({
      file: fileRel,
      line: 1,
      message: `"name" is ${name.length} characters (max ${NAME_MAX_LENGTH})`,
    });
  }

  if (!NAME_PATTERN.test(name)) {
    issues.push({
      file: fileRel,
      line: 1,
      message: `"name" must be lowercase alphanumeric + hyphens only, no leading/trailing/consecutive hyphens. Got: "${name}"`,
    });
  }

  if (name !== dirName) {
    issues.push({
      file: fileRel,
      line: 1,
      message: `Directory name "${dirName}" does not match frontmatter name "${name}"`,
    });
  }

  return issues;
}

function validateDescription(data, fileRel) {
  const issues = [];
  const raw = data.description;

  if (raw === undefined || raw === null || String(raw).trim().length === 0) {
    issues.push({
      file: fileRel,
      line: 1,
      message: 'Required frontmatter field "description" is missing or empty',
    });
    return issues;
  }

  const desc = String(raw).trim();
  if (desc.length > DESCRIPTION_MAX_LENGTH) {
    issues.push({
      file: fileRel,
      line: 1,
      message: `"description" is ${desc.length} characters (max ${DESCRIPTION_MAX_LENGTH})`,
    });
  }
  return issues;
}

function validateCompatibility(data, fileRel) {
  if (data.compatibility === undefined || data.compatibility === null) return [];
  const val = String(data.compatibility).trim();
  if (val.length === 0) return [];
  if (val.length > COMPATIBILITY_MAX_LENGTH) {
    return [
      {
        file: fileRel,
        line: 1,
        message: `"compatibility" is ${val.length} characters (max ${COMPATIBILITY_MAX_LENGTH})`,
      },
    ];
  }
  return [];
}

function validateMetadata(data, fileRel) {
  if (data.metadata === undefined || data.metadata === null) return [];
  const md = data.metadata;

  if (typeof md !== "object" || Array.isArray(md)) {
    return [
      {
        file: fileRel,
        line: 1,
        message: '"metadata" must be a YAML mapping (object), not ' + typeof md,
      },
    ];
  }

  const issues = [];
  for (const [key, value] of Object.entries(md)) {
    if (typeof value !== "string") {
      issues.push({
        file: fileRel,
        line: 1,
        message: `metadata.${key} must be a string, got ${typeof value}`,
      });
    }
  }
  return issues;
}

function validateAllowedTools(data, fileRel) {
  if (data["allowed-tools"] === undefined || data["allowed-tools"] === null)
    return [];
  if (typeof data["allowed-tools"] !== "string") {
    return [
      {
        file: fileRel,
        line: 1,
        message: '"allowed-tools" must be a space-separated string',
      },
    ];
  }
  return [];
}

function validateUnknownFields(data, fileRel) {
  const issues = [];
  for (const key of Object.keys(data)) {
    if (!KNOWN_FRONTMATTER_FIELDS.has(key)) {
      issues.push({
        file: fileRel,
        line: 1,
        message: `Unknown frontmatter field "${key}" (allowed: ${[...KNOWN_FRONTMATTER_FIELDS].join(", ")})`,
      });
    }
  }
  return issues;
}

// ---------------------------------------------------------------------------
// Repository-context validators
// ---------------------------------------------------------------------------

/**
 * Check if a path looks like a placeholder rather than a real file path.
 */
function isPlaceholderPath(p) {
  return PLACEHOLDER_PATTERNS.some((re) => re.test(p));
}

/**
 * Check if a potential file path starts with a known top-level prefix.
 */
function matchesKnownPrefix(p) {
  return KNOWN_PATH_PREFIXES.some((prefix) => p.startsWith(prefix));
}

/**
 * Collect all markdown files matching the repo-context globs.
 */
function collectRepoContextFiles(repoRoot) {
  const files = [];

  // AGENTS.md at repo root
  const agentsPath = path.join(repoRoot, "AGENTS.md");
  if (fs.existsSync(agentsPath)) files.push(agentsPath);

  // .agents/skills/**/*.md
  const skillsDir = path.join(repoRoot, ".agents", "skills");
  if (fs.existsSync(skillsDir)) {
    walkDir(skillsDir, files, ".md");
  }

  // docs/support-arch/**/*.md
  const supportArchDir = path.join(repoRoot, "docs", "support-arch");
  if (fs.existsSync(supportArchDir)) {
    walkDir(supportArchDir, files, ".md");
  }

  // docs/superpowers/**/*.md
  const superpowersDir = path.join(repoRoot, "docs", "superpowers");
  if (fs.existsSync(superpowersDir)) {
    walkDir(superpowersDir, files, ".md");
  }

  return files;
}

function walkDir(dir, out, ext) {
  let entries;
  try {
    entries = fs.readdirSync(dir, { withFileTypes: true });
  } catch (_) {
    return;
  }
  for (const e of entries) {
    const fp = path.join(dir, e.name);
    if (e.isDirectory()) {
      walkDir(fp, out, ext);
    } else if (e.name.endsWith(ext)) {
      out.push(fp);
    }
  }
}

/**
 * Validate stale references in file content.
 * Returns array of { file, line, message }.
 */
function validateStaleReferences(content, fileRel) {
  const issues = [];
  const lines = content.split("\n");

  for (const { re, label } of STALE_REFERENCES) {
    re.lastIndex = 0;
    for (const line of lines) {
      if (re.test(line)) {
        re.lastIndex = 0;
        const lineNum = lines.indexOf(line) + 1;
        issues.push({
          file: fileRel,
          line: lineNum,
          message: `Stale reference: ${label}`,
        });
      }
    }
  }

  // SHA-pinned GitHub blob links
  SHA_BLOB_RE.lastIndex = 0;
  let m;
  while ((m = SHA_BLOB_RE.exec(content)) !== null) {
    const before = content.substring(0, m.index);
    const lineNum = before.split("\n").length;
    issues.push({
      file: fileRel,
      line: lineNum,
      message: `SHA-pinned GitHub blob link is not allowed: ${m[0].substring(0, 80)}...`,
    });
  }

  return issues;
}

/**
 * Validate that code-review-graph-* links in repo-local docs point to
 * files that actually exist.
 */
function validateCrgSkillLinks(content, fileRel, repoRoot) {
  const issues = [];
  const re = /`code-review-graph-([a-z-]+)`/g;
  let m;
  while ((m = re.exec(content)) !== null) {
    const skillName = `code-review-graph-${m[1]}`;
    const expectedPath = path.join(
      repoRoot, ".agents", "skills", skillName, "SKILL.md"
    );
    const globalPath = path.join(
      require("os").homedir(), ".agents", "skills", skillName, "SKILL.md"
    );
    if (!fs.existsSync(expectedPath) && !fs.existsSync(globalPath)) {
      const before = content.substring(0, m.index);
      const lineNum = before.split("\n").length;
      issues.push({
        file: fileRel,
        line: lineNum,
        message: `Repo-local code-review-graph skill link "${skillName}" but file not found at ${expectedPath} or ${globalPath}`,
      });
    }
  }
  return issues;
}

// File extensions that indicate a backtick path is a concrete file reference.
const FILE_EXTENSIONS = new Set([
  "kt", "kts", "java", "xml", "json", "yml", "yaml", "md", "properties",
  "pro", "gradle", "sh", "toml", "cfg", "conf", "graphql", "gql", "css",
  "html", "js", "ts", "jsx", "tsx", "png", "svg", "webp", "jpg", "jpeg",
]);

/**
 * Validate backtick-enclosed paths that look like real file references.
 */
function validateBacktickPaths(content, fileRel, repoRoot) {
  const issues = [];
  const re = /`([^`]+)`/g;
  let m;
  while ((m = re.exec(content)) !== null) {
    const candidate = m[1].trim();
    if (!matchesKnownPrefix(candidate)) continue;
    if (isPlaceholderPath(candidate)) continue;
    // Skip if it's an obvious glob or contains wildcards
    if (candidate.includes("*") || candidate.includes("?")) continue;
    // Skip paths that look like URLs
    if (candidate.startsWith("http://") || candidate.startsWith("https://")) continue;

    // Only validate paths that look like concrete file references:
    // - have a file extension
    // - or contain a line anchor (#L or :line)
    const ext = path.extname(candidate).replace(/^\./, "").toLowerCase();
    const hasFileExt = FILE_EXTENSIONS.has(ext);
    const hasLineAnchor = /[#:]\d+/.test(candidate);
    if (!hasFileExt && !hasLineAnchor) continue;

    // Strip line anchor for resolution
    const cleanPath = candidate.replace(/[#:]\d+.*$/, "");
    const resolved = path.join(repoRoot, cleanPath);
    if (!fs.existsSync(resolved)) {
      const before = content.substring(0, m.index);
      const lineNum = before.split("\n").length;
      issues.push({
        file: fileRel,
        line: lineNum,
        message: `Backtick path does not resolve: \`${candidate}\` -> ${resolved}`,
      });
    }
  }
  return issues;
}

/**
 * Run all repository-context validations across collected markdown files.
 */
function validateRepoContext(repoRoot) {
  const errors = [];
  const files = collectRepoContextFiles(repoRoot);

  for (const filePath of files) {
    const fileRel = path.relative(repoRoot, filePath);
    let content;
    try {
      content = fs.readFileSync(filePath, "utf-8");
    } catch (_) {
      continue;
    }

    errors.push(...validateStaleReferences(content, fileRel));
    errors.push(...validateCrgSkillLinks(content, fileRel, repoRoot));
    errors.push(...validateBacktickPaths(content, fileRel, repoRoot));
  }

  return errors;
}

// ---------------------------------------------------------------------------
// Main validator
// ---------------------------------------------------------------------------

function validateSkills(repoRoot) {
  const skillsDir = path.join(repoRoot, ".agents", "skills");
  if (!fs.existsSync(skillsDir)) {
    console.error(`Skills directory not found: ${skillsDir}`);
    process.exit(1);
  }

  const entries = fs.readdirSync(skillsDir, { withFileTypes: true });
  const skillDirs = entries
    .filter((e) => e.isDirectory())
    .map((e) => e.name)
    .sort();

  if (skillDirs.length === 0) {
    console.error("No skill directories found under .agents/skills/");
    process.exit(1);
  }

  const errors = [];
  const warnings = [];
  const seenNames = new Map();

  for (const dirName of skillDirs) {
    const filePath = path.join(skillsDir, dirName, "SKILL.md");
    const fileRel = `.agents/skills/${dirName}/SKILL.md`;

    if (!fs.existsSync(filePath)) {
      errors.push({
        file: `.agents/skills/${dirName}/`,
        line: null,
        message: "Missing SKILL.md",
      });
      continue;
    }

    const parsed = parseSkillFile(filePath);
    if (!parsed) continue;

    const { data, content, lines, bodyStartLine } = parsed;

    // Frontmatter presence (gray-matter returns empty data when no frontmatter)
    if (!data || Object.keys(data).length === 0) {
      if (lines[0]?.trim() !== "---") {
        errors.push({
          file: fileRel,
          line: 1,
          message: "Missing YAML frontmatter (file must start with ---)",
        });
        continue;
      }
    }

    // Field validators
    errors.push(...validateName(data, dirName, fileRel));

    if (data.name && String(data.name).trim().length > 0) {
      errors.push(...validateDescription(data, fileRel));
      errors.push(...validateCompatibility(data, fileRel));
      errors.push(...validateMetadata(data, fileRel));
      errors.push(...validateAllowedTools(data, fileRel));
      errors.push(...validateUnknownFields(data, fileRel));

      const nameVal = String(data.name).trim();
      if (seenNames.has(nameVal)) {
        errors.push({
          file: fileRel,
          line: 1,
          message: `Duplicate skill name "${nameVal}" (already used in .agents/skills/${seenNames.get(nameVal)}/SKILL.md)`,
        });
      } else {
        seenNames.set(nameVal, dirName);
      }
    }

    // Body length warning
    const bodyLineCount = content.split("\n").length;
    if (bodyLineCount > BODY_MAX_LINES) {
      warnings.push({
        file: fileRel,
        line: bodyStartLine + 1,
        message: `Body is ${bodyLineCount} lines (recommended max ${BODY_MAX_LINES}). Move detail into references/.`,
      });
    }

    // Internal links
    const links = extractRelativeLinks(content);
    for (const link of links) {
      const resolved = path.resolve(
        path.dirname(filePath),
        link.target.split("#")[0]
      );

      if (!fs.existsSync(resolved)) {
        const bodyLine =
          bodyStartLine +
          content.substring(0, link.pos).split("\n").length;
        errors.push({
          file: fileRel,
          line: bodyLine,
          message: `Broken link: ${link.target} -> ${resolved} (file not found)`,
        });
      }

      if (!isCrossSkillLink(link.target)) {
        const depth = linkDepth(link.target);
        if (depth > 1) {
          const bodyLine =
            bodyStartLine +
            content.substring(0, link.pos).split("\n").length;
          warnings.push({
            file: fileRel,
            line: bodyLine,
            message: `Deep reference: ${link.target} is ${depth} levels deep (keep references one level from SKILL.md)`,
          });
        }
      }
    }
  }

  // --- Repository-context validation ---
  const ctxErrors = validateRepoContext(repoRoot);
  errors.push(...ctxErrors);

  // --- Report ---
  let exitCode = 0;

  if (warnings.length > 0) {
    console.error(`\n${warnings.length} warning(s):\n`);
    for (const w of warnings) {
      const loc = w.line ? `  ${w.file}:${w.line}` : `  ${w.file}`;
      console.error(`${loc}`);
      console.error(`    ${w.message}\n`);
    }
  }

  if (errors.length > 0) {
    console.error(`\n${errors.length} error(s):\n`);
    for (const err of errors) {
      const loc = err.line ? `  ${err.file}:${err.line}` : `  ${err.file}`;
      console.error(`${loc}`);
      console.error(`    ${err.message}\n`);
    }
    exitCode = 1;
  }

  if (errors.length === 0 && warnings.length === 0) {
    console.log(
      `All ${skillDirs.length} skill files passed validation. ` +
      `Repo-context validation passed.`
    );
  } else if (errors.length === 0) {
    console.log(
      `${skillDirs.length} skill files passed validation with ${warnings.length} warning(s). ` +
      `Repo-context validation passed.`
    );
  }

  process.exit(exitCode);
}

// ---------------------------------------------------------------------------
// Entry
// ---------------------------------------------------------------------------

const repoRoot = process.argv[2] || process.cwd();
validateSkills(path.resolve(repoRoot));
