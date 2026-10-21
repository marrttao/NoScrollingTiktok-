# Compatibility

Unscroll patches exactly one TikTok build at a time. Patches refuse other versions.

| TikTok version | Package | Source file | Patches apply | Device QA |
|---|---|---|---|---|
| 47.4.3 (versionCode 2024704030) | `com.zhiliaoapp.musically` (global) | APKMirror bundle `.apkm` "(arm64-v8a + arm-v7a) (120-640dpi) (Android 6.0+)" | **yes** — verified 2026-10-21 with Morphe Desktop 1.18.1 | not yet verified |

Original signing certificate (all splits): `CN=musical.ly, OU=android, O=musical.ly Inc.`,
SHA-256 `9041803e91bcb814b4b4399fb5c85a91640b755e5e8ba76813814bf4cf2ab5ba`.

Not supported: the Asian package `com.ss.android.ugc.trill`, TikTok Lite, any
already-modified APK.

## Requirements for the source file

* Original, unmodified TikTok signed with the certificate above.
* Exact version listed above (`versionName`).
* A full APK or a complete split bundle (`.apkm`, `.apks`, `.xapk`). Keep the file
  extension — Morphe picks the format by it. Morphe merges the splits.

## Per-patch status for 47.4.3

"Applies" = fingerprints resolved and the code was injected (checked in the patched
dex). "Device" = behaviour checked with `docs/QA_CHECKLIST.md`.

| Patch | Applies | Device |
|---|---|---|
| Block feed endpoints | yes | not yet |
| Restrict navigation | yes | not yet |
| Disable video swipe | yes | not yet |
| Friends-only profile playback | yes | not yet |
| Block feed screens | yes | not yet |
| Fix Google login | yes | not yet |
