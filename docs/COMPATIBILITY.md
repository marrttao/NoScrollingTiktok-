# Compatibility

Unscroll patches exactly one TikTok build at a time. Patches refuse other versions.

| TikTok version | Package | Architecture | Patch bundle | Patches apply (CI) | Device QA |
|---|---|---|---|---|---|
| 46.2.3 | `com.zhiliaoapp.musically` (global) | APK from APKMirror variant `tiktok-46-2-3-android-apk-download` | 0.1.0 (unreleased) | not yet verified | not yet verified |

Not supported: the Asian package `com.ss.android.ugc.trill`, TikTok Lite, split-APK
bundles (`.apkm`/`.xapk`), any already-modified APK.

## Requirements for the source APK

* Original, unmodified APK signed by TikTok.
* Exact version listed above (`versionName`).
* A full APK, not an App Bundle split set.

## Per-patch status for 46.2.3

"Verified" means the fingerprints resolved on the real APK in CI **and** the behaviour
was checked on a device with `docs/QA_CHECKLIST.md`.

| Patch | Fingerprints resolve | Behaviour checked on device |
|---|---|---|
| Block video feeds | not yet verified | no |
| Restrict navigation | not yet verified | no |
| Fix Google login | not yet verified | no |
