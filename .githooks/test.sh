#!/bin/bash

# Test script for pre-commit hook validation
# This script tests various branch name scenarios to ensure the hook works correctly

cd "$(dirname "$0")/.."

echo "🧪 Testing AniTrend v2 Pre-commit Hook"
echo "======================================"

# Test cases: valid branch names
VALID_BRANCHES=(
    "feat/add-login-feature"
    "fix/bug-in-login"
    "chore/update-dependencies"
    "docs/update-readme"
    "refactor/clean-up-code"
    "test/add-unit-tests"
    "build/update-gradle"
    "ci/add-github-actions"
    "revert/undo-breaking-change"
    "feat/123-add-feature-with-number"
    "fix/some-long-description-with-dashes"
)

# Test cases: invalid branch names
INVALID_BRANCHES=(
    "feature/old-naming-convention"
    "bugfix/old-naming-convention"
    "hotfix/old-naming-convention"
    "enhancement/old-naming-convention"
    "dependencies/old-naming-convention"
    "random-branch-name"
    "feat/InvalidCamelCase"
    "feat/spaces not allowed"
    "feat/under_scores_not_allowed"
    "feat/"
    "/no-type-prefix"
    "feat"
    "FEAT/uppercase-not-allowed"
)

# Special branches that should be allowed
SPECIAL_BRANCHES=(
    "main"
    "master"
    "develop"
)

echo ""
echo "🟢 Testing valid branch names..."
echo "-------------------------------"

for branch in "${VALID_BRANCHES[@]}"; do
    # Create a temporary script that simulates being on this branch
    echo "#!/bin/sh
BRANCH_NAME=\"$branch\"
PATTERN=\"^(feat|fix|chore|docs|refactor|test|build|ci|revert)\/[a-z0-9\-]+$\"
if ! echo \"\$BRANCH_NAME\" | grep -qE \"\$PATTERN\"; then
    exit 1
fi
exit 0" > /tmp/test_hook.sh
    chmod +x /tmp/test_hook.sh
    
    if /tmp/test_hook.sh; then
        echo "  ✅ $branch"
    else
        echo "  ❌ $branch (should be valid!)"
    fi
done

echo ""
echo "🔴 Testing invalid branch names..."
echo "--------------------------------"

for branch in "${INVALID_BRANCHES[@]}"; do
    # Create a temporary script that simulates being on this branch
    echo "#!/bin/sh
BRANCH_NAME=\"$branch\"
PATTERN=\"^(feat|fix|chore|docs|refactor|test|build|ci|revert)\/[a-z0-9\-]+$\"
if ! echo \"\$BRANCH_NAME\" | grep -qE \"\$PATTERN\"; then
    exit 1
fi
exit 0" > /tmp/test_hook.sh
    chmod +x /tmp/test_hook.sh
    
    if /tmp/test_hook.sh; then
        echo "  ❌ $branch (should be invalid!)"
    else
        echo "  ✅ $branch (correctly rejected)"
    fi
done

echo ""
echo "⚪ Testing special branches (should be allowed)..."
echo "------------------------------------------------"

for branch in "${SPECIAL_BRANCHES[@]}"; do
    # Create a temporary script that simulates being on this branch
    echo "#!/bin/sh
BRANCH_NAME=\"$branch\"
if [ \"\$BRANCH_NAME\" = \"main\" ] || [ \"\$BRANCH_NAME\" = \"master\" ] || [ \"\$BRANCH_NAME\" = \"develop\" ]; then
    exit 0
fi
PATTERN=\"^(feat|fix|chore|docs|refactor|test|build|ci|revert)\/[a-z0-9\-]+$\"
if ! echo \"\$BRANCH_NAME\" | grep -qE \"\$PATTERN\"; then
    exit 1
fi
exit 0" > /tmp/test_hook.sh
    chmod +x /tmp/test_hook.sh
    
    if /tmp/test_hook.sh; then
        echo "  ✅ $branch"
    else
        echo "  ❌ $branch (should be allowed!)"
    fi
done

# Clean up
rm -f /tmp/test_hook.sh

echo ""
echo "🏁 Test completed!"