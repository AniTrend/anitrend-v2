---
name: adb-device-workflow
description: 'Use ADB to connect devices, install Android debug builds, inspect logs, and troubleshoot deployment failures. Use for device detection errors, install failures, launch failures, package selection across flavors, and first-pass pid-scoped logcat capture.'
argument-hint: 'Describe the ADB task, target variant, and whether device is USB or wireless'
---

# ADB Device Workflow

## What This Skill Produces

- A reliable, repeatable ADB flow to install and validate Android debug builds.
- Device connectivity checks (USB and wireless) before deployment.
- Fast troubleshooting branches for common ADB and package install failures.

## When To Use

- Installing a debug build to a phone/emulator.
- Diagnosing adb device detection issues.
- Capturing app logs after launch failures.
- Narrowing runtime failures to the correct installed package and process.
- Reinstalling, clearing app data, or validating package state.

## Procedure

1. Verify ADB is available and server is healthy.

```bash
command -v adb
adb start-server
adb version
```

Quality check:
- `adb version` must return a valid version string.

2. Enumerate connected targets.

```bash
adb devices -l
```

Decision point:
- If no devices appear, check cable/USB debugging or start an emulator.
- If a device shows as `unauthorized`, unlock device and accept RSA prompt.
- If multiple devices are connected, capture target serial for later commands.

3. Build and install with Gradle first (preferred path for this repo).

```bash
./gradlew --no-daemon installDebug
```

Decision point:
- If multiple devices are attached and Gradle cannot choose a target, install manually with `adb -s <serial> install -r <apk-path>`.
- If install succeeds, continue to validation.

4. Manual install fallback with explicit target serial.

```bash
adb -s <serial> install -r app/build/outputs/apk/github/debug/app-github-debug.apk
```

Notes:
- Use `-r` to replace existing app while preserving data.
- Add `-d` only when intentionally allowing version downgrade.
- Add `-t` when installing test-only APKs.

5. Launch and validate app process.

```bash
adb -s <serial> shell monkey -p co.anitrend -c android.intent.category.LAUNCHER 1
adb -s <serial> shell pidof co.anitrend
```

Quality check:
- App process should have a PID after launch.

6. If the repo ships multiple flavors or package names, resolve the installed package before collecting logs.

```bash
adb -s <serial> shell pm list packages | grep anitrend
adb -s <serial> shell pidof -s <package-name>
```

Decision point:
- If more than one AniTrend package is installed, pick the exact package that matches the variant you just installed.
- If the process exits too quickly to keep a PID, fall back to unscoped `adb logcat -d` immediately after repro.

7. Capture focused logs for startup/debug failures.

```bash
adb -s <serial> logcat -c
adb -s <serial> logcat | grep -E "AndroidRuntime|FATAL EXCEPTION|anitrend|ActivityManager"
```

Decision point:
- If process crashes immediately, collect stack trace and check runtime permissions or missing resources.

8. Prefer pid-scoped logcat once the app is alive.

```bash
pid=$(adb -s <serial> shell pidof -s <package-name> | tr -d '\r')
adb -s <serial> logcat -d --pid="$pid"
```

Quality check:
- The captured log should be dominated by the target process instead of unrelated system noise.

9. Escalate to the deeper runtime workflow when logs alone are insufficient.

Use the `android-runtime-investigation` skill when you need to:
- correlate UI failures with recorded HTTP responses
- inspect debug-only Chucker traffic
- pull evidence from the app sandbox before changing serializers or mappers

## Wireless ADB Branch

Use when USB is unavailable and device + host are on same network.

```bash
adb -s <serial> tcpip 5555
adb connect <device-ip>:5555
adb devices -l
```

Quality check:
- Device appears as `<device-ip>:5555` and accepts shell commands.

## Recovery Branches

- Device stuck `offline`:

```bash
adb kill-server
adb start-server
adb reconnect offline
adb devices -l
```

- Install conflict (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`):

```bash
adb -s <serial> uninstall co.anitrend
adb -s <serial> install app/build/outputs/apk/github/debug/app-github-debug.apk
```

- Signature mismatch or stale state:

```bash
adb -s <serial> shell pm clear co.anitrend
```

## Completion Checklist

- ADB server is running and target device is `device` state.
- Debug APK installs successfully.
- App launches and remains alive (PID present).
- If failure occurred, actionable log output was captured and scoped to the correct package when possible.

## Fast Invocation Examples

- "Install debug build to my connected device and verify launch"
- "ADB sees unauthorized device, recover and deploy debug APK"
- "Use wireless ADB to deploy and stream startup crash logs"
