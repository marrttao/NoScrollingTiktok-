# Test report

Date: 2026-10-21 · Bundle: 0.1.0 (unreleased) · TikTok 47.4.3 (versionCode 2024704030),
APKMirror bundle `.apkm` · Morphe Desktop 1.18.1 · Environment: Linux container, no device.

Only what is listed as **pass** was actually executed. Nothing was tested on a phone.

## 1. Automated tests

| Suite | Result |
|---|---|
| Extension unit tests (`FeedEndpointPolicy`, `TabPolicy`, `PlaybackPolicy`) — 52 cases | pass (local JVM; CI runs `:extensions:tiktok:testDebugUnitTest`) |
| Patch metadata tests (pinned to 47.4.3, default-on, script lists every patch) | pass (local, and CI against morphe-patcher 1.15.1) |
| CI build of `patches-*.mpp` with the official Morphe Gradle plugin | pass |

## 2. Source APK checks

| Check | Result |
|---|---|
| Package / version of `base.apk` | `com.zhiliaoapp.musically` 47.4.3 — pass |
| Signature of all 116 splits | one signer, `CN=musical.ly`, SHA-256 `9041803e…ab5ba` — pass |

## 3. Patch application (real APK)

| Check | Result |
|---|---|
| All 6 patches apply (`Block feed endpoints`, `Restrict navigation`, `Disable video swipe`, `Friends-only profile playback`, `Block feed screens`, `Fix Google login`) | pass |
| Injected calls present in patched dex (`Request.<init>` → `FeedEndpointPolicy.filterUrl`, `TabChangeManager` switch → `TabPolicy.redirectTab`, etc.) | pass (inspected with dexlib2) |
| Split bundle merged into one APK; manifest carries `com.android.dynamic.apk.fused.modules`; no `isSplitRequired` | pass |
| `scripts/unscroll-patch.sh` end to end → signed APK (also with `--arch arm64-v8a`: 532 MB, arm64 only) | pass |
| `apksigner verify` (v1 + v2 + v3) | pass |
| `zipalign -c -P 16 4` | pass |
| Output keeps package `com.zhiliaoapp.musically`, versionCode 2024704030 | pass |
| **Fail-closed:** bundle without the Unscroll patches → script exits 1, no APK | pass |
| Upstream 46.2.3 hooks (Metra) against 47.4.3 | 23 of 41 Metra patches fail — reason Unscroll re-derived its own hooks |

## 4. Not tested (needs a device)

Everything in `docs/QA_CHECKLIST.md`: start-up, login (all methods), messages, shared
videos, swipe blocking in practice, profiles, posting, drafts, notifications, LIVE,
restart behaviour, and attempts to bypass the restrictions.

Known risks to check first on the device:

1. **App starts at all** after the merged-bundle rebuild (TikTok has integrity checks;
   other projects patch TikTok the same way, but this build is unverified).
2. **Inbox at start-up**: the Home → Inbox redirect is applied at TikTok's tab switcher;
   whether the very first screen goes through it is unverified.
3. **Camera / editor** live in former dynamic-feature modules; they are fused into the
   APK and marked as installed, but recording/editing must be checked (QA G1–G3).
4. **Google sign-in** through the browser flow (QA A4).
5. **Swipe**: touch is disabled on the video pager; TikTok's opt-in *Auto scroll* can
   still advance programmatically (not blocked yet).
