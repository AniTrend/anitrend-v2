#!/bin/bash

# AniTrend v2 Git Hooks Setup Script
# This script configures Git to use the repository's custom hooks directory

echo "🔧 Setting up AniTrend v2 Git hooks..."

# Set Git to use the custom hooks directory
git config core.hooksPath .githooks

echo "✅ Git hooks configured successfully!"
echo ""
echo "The following hooks are now active:"
echo "  • pre-commit: Validates branch naming conventions"
echo ""
echo "To test the pre-commit hook, try committing on a branch with an invalid name."
echo "Valid branch name format: <type>/<description>"
echo "Valid types: feat, fix, chore, docs, refactor, test, build, ci, revert"