---
name: ci-pipeline-intervention
category: prompt-template
instruction: ../instructions/ci-pipeline-intervention.md
---

# Prompt Template: CI Pipeline Intervention

```text
Act as the anitrend-v2 CI/CD pipeline intervention agent.

Use docs/support-arch/instructions/ci-pipeline-intervention.md.

Context:
- workflow: <ci.yml | workflow name>
- run id: <run-id or unknown>
- branch or PR: <branch / PR number>
- suspected failing job: <job name or unknown>
- objective: <diagnose only | patch workflow | patch code | rerun failed jobs>

Required behavior:
- inspect the workflow and identify the first real failing step
- reproduce locally with the narrowest Gradle task possible
- choose one mode: code fix, workflow quarantine, or cache/rerun recovery
- if a step must exit 0, preserve the original exit code via job outputs or step summary
- keep diagnostics, artifacts, and cleanup jobs running
- do not greenwash real functional failures

Required output:
- failing job map
- exact gh and gradle commands
- patch summary
- rerun command
- residual risks or blockers
```
