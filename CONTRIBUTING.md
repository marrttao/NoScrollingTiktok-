# Contributing to Unscroll

Thanks for helping people keep TikTok for friends and creating, without the feed.

## Ground rules

* **Product rules are fixed** in [docs/DECISIONS.md](docs/DECISIONS.md). Patches that
  re-open a feed, add a "temporarily disable" switch, add settings or add telemetry
  will not be merged. Proposals to change a rule go to an issue first.
* **Never commit** TikTok APKs, decompiled TikTok sources, keystores, tokens or any
  personal data (screenshots with names, account ids, message content).
* Code adapted from other projects must be GPL-3.0 compatible and must say where it
  comes from in the file header; add the project to [NOTICE](NOTICE).

## Development setup

1. JDK 21.
2. A GitHub personal access token (classic) with only `read:packages` — Morphe hosts its
   patcher on GitHub Packages. Put it in `~/.gradle/gradle.properties`:
   ```properties
   gpr.user=<your-github-user>
   gpr.key=<token>
   ```
3. `./gradlew :extensions:tiktok:testDebugUnitTest :patches:test :patches:buildAndroid`

## Where things go

* `patches/src/main/kotlin/app/unscroll/patches/tiktok/<area>/` — one directory per
  patch: `Fingerprints.kt` (where to hook) and `<Name>Patch.kt` (what to inject).
* `extensions/tiktok/src/main/java/app/unscroll/extension/tiktok/` — the logic the hooks
  call. Keep it plain Java, side-effect free where possible, and **unit-tested**.
* Hooks must only forward values to extension methods. Decisions live in Java.

## Writing a fingerprint

* Prefer stable anchors: class names that are not obfuscated, string constants, field
  names of model classes. Obfuscated method names (`W52`, `OA`) are allowed only with a
  comment saying which TikTok version they were checked on.
* Make the patch throw `PatchException` when the code shape is not what you expect.
  A patch that silently does nothing is worse than a failing patch (fail-closed).
* Add the patch name to `PATCHES` in `scripts/unscroll-patch.sh` and to
  `docs/COMPATIBILITY.md`.

## Supporting a new TikTok version

1. Add the version to `SUPPORTED_TIKTOK_VERSIONS` and to the CI matrix.
2. Make every fingerprint resolve (CI job *Apply patches to TikTok*).
3. Run the full [QA checklist](docs/QA_CHECKLIST.md) on a device and record the result
   in `docs/COMPATIBILITY.md`. Only then remove the old version.

## Pull requests

* One logical change per PR, with a short description of what was verified and how.
* Do not claim "works" without saying which test or QA line shows it.
* Conventional commit prefixes are welcome (`feat:`, `fix:`, `docs:`, `ci:`).
