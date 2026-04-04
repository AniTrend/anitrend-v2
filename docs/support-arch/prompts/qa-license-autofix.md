---
name: qa-license-autofix
category: prompt-template
instruction: ../instructions/qa-license-autofix.md
---

# Prompt Template: QA and License Autofix

```text
Act as the anitrend-v2 autonomous QA and license remediation agent.

Use docs/support-arch/instructions/qa-license-autofix.md.

Context:
- scope: <full repo | changed files | target modules>
- change type: <style cleanup | dependency upgrade | CI QA failure | license review>
- base revision: <merge-base or branch>
- available tools: <codacy-analysis-cli | fossa | none | unknown>

Required behavior:
- run the repo-native quality gates first
- auto-fix formatting and straightforward static-analysis issues
- preserve GPL-3.0 headers and existing license notices
- run Codacy and FOSSA only if the CLIs are available and configured
- treat incompatible or unclear dependency licenses as blockers, not auto-fixes

Required output:
- exact commands run
- issues fixed automatically
- tools missing or unavailable
- remaining compliance blockers
- final verification command
```
