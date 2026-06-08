#!/usr/bin/env node

/**
 * Skill file auditor for .agents/skills/** /SKILL.md files.
 *
 * Enforces the Agent Skills specification:
 *   https://github.com/agentskills/agentskills
 *
 * Validates frontmatter fields (name, description, license, compatibility,
 * metadata, allowed-tools), body length, internal link resolution, and
 * reference depth.
 *
 * Requires: npm install gray-matter
 * Usage:    node .github/scripts/validate-skills.js [repo-root]
 */

const fs = require("fs");
const path = require("path");

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

  // Report
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
    console.log(`All ${skillDirs.length} skill files passed validation.`);
  } else if (errors.length === 0) {
    console.log(
      `${skillDirs.length} skill files passed validation with ${warnings.length} warning(s).`
    );
  }

  process.exit(exitCode);
}

// ---------------------------------------------------------------------------
// Entry
// ---------------------------------------------------------------------------

const repoRoot = process.argv[2] || process.cwd();
validateSkills(path.resolve(repoRoot));
