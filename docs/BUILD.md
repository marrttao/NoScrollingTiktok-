# Build from source

[← Project](../README.en.md) · [APKs and updates in Telegram](https://t.me/SayNoToScrolling)

This guide is for contributors and people who want to build their own patches.
For mod APK versions and announcements, visit
[@SayNoToScrolling](https://t.me/SayNoToScrolling).

## Requirements

* JDK 21.
* Android SDK platform 36 and the build-tools required by the Android Gradle plugin.
  Set `ANDROID_HOME` to your SDK directory, or use an untracked `local.properties`
  with `sdk.dir`.
* GitHub Packages credentials with `read:packages` for Morphe's patcher and Gradle plugin.
* Access to the Gradle, Google, Maven Central, GitHub Packages and JitPack repositories.

Use the checked-in Gradle wrapper. See [CONTRIBUTING](../CONTRIBUTING.md) for credential
configuration; keep token values and signing keys out of the repository.

## Test and build the patch bundle

Run from the repository root, with `GITHUB_ACTOR` and `GITHUB_TOKEN` set securely in
your shell, or `gpr.user` and `gpr.key` in your private Gradle properties:

```bash
./gradlew :extensions:tiktok:testDebugUnitTest :patches:test --no-daemon
./gradlew :patches:buildAndroid --no-daemon
```

The bundle is written to `patches/build/libs/patches-<version>.mpp`.
Unit tests and a successful bundle build do not verify patch application to a real
TikTok APK or behaviour on a device. See [compatibility](COMPATIBILITY.md) and the
[QA checklist](QA_CHECKLIST.md).

## Patch your own APK

Use an original, unmodified, full TikTok APK matching [compatibility](COMPATIBILITY.md)
(currently 46.2.3, package `com.zhiliaoapp.musically`). Verify its original signature.
Split bundles and already-modified APKs are not supported as patch inputs.

```bash
scripts/unscroll-patch.sh \
  --apk /path/to/TikTok-46.2.3.apk \
  --bundle patches/build/libs/patches-0.1.0.mpp \
  --keystore /path/to/private/unscroll.keystore \
  --out /path/to/unscroll-46.2.3.apk
```

The script downloads a pinned Morphe Desktop CLI and verifies its SHA-256. It
produces no APK if any patch fails. The keystore is created on first use: keep it
private, back it up, and reuse it for your updates. Your self-signed build and an
APK from the channel may use different signing keys and cannot update each other
in place.

You can also patch on a phone using Morphe Manager from the official Morphe project:
import a compatible Unscroll `.mpp` bundle, select the original APK, keep all Unscroll
patches enabled and patch. Keep Manager's signing key for later updates.

**Before installing any build, save drafts and read [installation](INSTALL.md).**
Replacing official TikTok, or switching between different signing keys, requires
uninstalling and deletes local data.
