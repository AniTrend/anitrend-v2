---
name: android-ui-automator-preview
description: 'Capture quick Android UI evidence with explicit launches, UIAutomator dumps, and adb screenshots for fast visual debugging and reproducible repro notes.'
argument-hint: 'Provide the full application ID, target screen, and whether app data should be cleared first'
---

# Android UI Automator Preview

## What This Skill Produces

- A deterministic launch path into the real app activity.
- Repeatable post-clear-data navigation steps for AniTrend onboarding flows.
- Paired UI evidence: XML hierarchy plus PNG screenshot.
- Fast text extraction from dumps to confirm whether expected labels are present.

## When To Use

- You need quick visual proof of what is currently on-screen.
- A repro depends on onboarding or permission prompts after `pm clear`.
- You suspect drawer/menu truncation and need objective evidence before code changes.

## Preconditions

- If the full application ID is not provided, stop and ask for it before running any adb command.
- If the target device is not the expected resolution or density, re-derive coordinates from a fresh `uiautomator dump` or scale them from the recorded 1080x2400 @ 420dpi baseline.
- If the user says app data should not be cleared, skip the clear-data command and continue from the explicit launch step.

## Procedure

1. Start from a known state.

```bash
adb devices -l
# clear app data only when the user explicitly asked for a fresh state
adb shell pm clear <package-name>
adb logcat -c
```

2. Launch the intended activity explicitly.

```bash
adb shell am start -n <package-name>/co.anitrend.android.deeplink.component.screen.DeepLinkScreen
```

Why:
- Replace `<package-name>` with the application ID for the installed build.
- The activity class is fixed for AniTrend; replace it only if you are targeting a different app.
- Explicit launch avoids `monkey` randomness.

3. Drive onboarding and permission prompts (AniTrend profile).

```bash
adb shell input swipe 900 1700 200 1700 300  # onboarding page 1 -> 2
adb shell input swipe 900 1700 200 1700 300  # onboarding page 2 -> 3
adb shell input swipe 900 1700 200 1700 300  # onboarding page 3 -> 4
adb shell input swipe 900 1700 200 1700 300  # onboarding page 4 -> 5
adb shell input tap 540 2280                # tap Get Started
adb shell input tap 540 1285                # tap Allow
```

4. Open target UI (example: drawer nav button region).

```bash
adb shell input tap 84 2240
```

5. Capture XML and screenshot.

```bash
adb shell uiautomator dump /sdcard/window_dump.xml
adb pull /sdcard/window_dump.xml /tmp/window_dump.xml
adb exec-out screencap -p > /tmp/screen.png
```

6. Validate expected labels.

```bash
grep -E "General|Manage|Catalogs|Support|Home|Discover|News|Forums|Episodes|FAQ" /tmp/window_dump.xml
```

7. Optional: capture process-scoped logs during same window.

```bash
pid=$(adb shell pidof -s <package-name> | tr -d '\r')
adb logcat -d --pid="$pid" > /tmp/<package-name>-ui-window.log
```

## Completion Checklist

- Explicit activity launch command recorded.
- XML dump and PNG screenshot captured from same UI state.
- Any onboarding/permission steps documented with tap/swipe actions.
- At least one text-based assertion from the XML hierarchy included.

## Fast Invocation Examples

- "Capture current drawer screen as XML + PNG after clear-data flow"
- "Run explicit launch and show whether onboarding is blocking main shell"
- "Grab small screenshot proof for this emulator repro"
