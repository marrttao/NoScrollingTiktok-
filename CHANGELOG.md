# Changelog

All notable changes are documented here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/); versions follow SemVer.

## [Unreleased]

### Added
- Research, product decisions, architecture, install, compatibility and QA documents.
- Morphe-based Gradle project with patches and a Java extension.
- Patch *Block video feeds*: empties For You, Following and Friends feed data.
- Patch *Restrict navigation*: bottom bar limited to Create, Inbox and Profile.
- Patch *Fix Google login*: browser-based Google sign-in for the re-signed app.
- Extension policies with unit tests: tabs, links, profile playback, feed guard.
- `scripts/unscroll-patch.sh`: reproducible, fail-closed patching with a pinned Morphe CLI.
- CI: unit tests, patch bundle build, patch application against the original TikTok APK.

### Not yet verified
- No patch has been verified on a real TikTok APK or device yet.
