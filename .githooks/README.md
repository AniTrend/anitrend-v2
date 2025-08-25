# Git Hooks

This directory contains repository-managed Git hooks that enforce project conventions and standards.

## Available Hooks

### pre-commit
Validates that branch names follow the AniTrend v2 naming convention: `<type>/<description>`

**Valid branch types:**
- `feat` - A new feature
- `fix` - A bug fix  
- `chore` - Routine tasks (build processes, dependencies)
- `docs` - Documentation only changes
- `refactor` - Code change that neither fixes a bug nor adds a feature
- `test` - Adding missing tests or correcting existing tests
- `build` - Changes that affect the build system or dependencies
- `ci` - Changes to CI configuration files
- `revert` - Reverting a previous commit

**Examples of valid branch names:**
- `feat/add-login-feature`
- `fix/bug-in-login`
- `chore/update-dependencies`
- `docs/update-readme`

This hook automatically skips validation when commits occur in a detached HEAD state or during interactive operations such as rebase, squash, merge, cherry-pick, or revert. This prevents false failures when `HEAD` is not an actual branch name.

## Setup

To enable these hooks in your local repository, run:

```bash
./.githooks/setup.sh
```

This configures Git to use the custom hooks directory instead of the default `.git/hooks/`.

## Bypass Hook (Emergency Use Only)

If you need to bypass the pre-commit hook in an emergency, you can use:

```bash
git commit --no-verify
```

**Note:** This should only be used in exceptional circumstances as it defeats the purpose of maintaining consistent branch naming conventions.

## Troubleshooting

- Error mentioning `Invalid branch name: 'HEAD'` during squash/rebase: Update your hooks by re-running `./.githooks/setup.sh`. The hook now detects detached HEAD and skips validation during these operations.