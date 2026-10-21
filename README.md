# Unscroll

**TikTok without doomscrolling.**
Keep your friends. Keep creating. Lose the feed.

Unscroll is an open-source patch set for the original TikTok Android app. It removes
the infinite video feeds and the swipe-to-next-video loop, and keeps everything that is
about people: messages, shared videos, streaks, your profile and the full creator
toolkit.

> **Status: pre-release.** All patches apply to TikTok 47.4.3 and the injected code was
> checked, but nothing has been tested on a device yet. See
> [Compatibility](docs/COMPATIBILITY.md) for what is verified.

Unscroll is **not** a TikTok clone, a separate player, an overlay blocker or a screen-time
timer. It is the TikTok app you know, minus the feed.

## What changes

| Removed | Kept |
|---|---|
| For You, Following, Friends feeds | Direct messages, group chats, streaks |
| Swiping to the next video, auto-advance | Watching a video a friend sent you |
| "Related" and recommended videos | Likes, comments, favourites, reposts, sharing |
| LIVE feed, Explore, Nearby, Shop | Recording, uploading, editing, drafts, posting |
| Video, hashtag and sound search results | Account search, your profile, analytics |
| Watching non-friends' profiles | Friends' profiles (mutual follows), friends' stories |
| Deep links into feeds | Starting your own LIVE, settings, privacy |

There is no settings screen and no "just five minutes" switch. The full rule set is in
[docs/DECISIONS.md](docs/DECISIONS.md).

## How it works

Unscroll is a patch bundle for the [Morphe](https://github.com/MorpheApp) patcher (a
continuation of ReVanced). You patch an original TikTok APK yourself, on your phone with
Morphe Manager or on a computer with `scripts/unscroll-patch.sh`. This repository
contains **no TikTok code or APKs**.

Feeds are blocked in three independent layers: feed data is emptied, the navigation to
feeds is removed, and video screens show a single video. Details:
[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

## Install

Replacing TikTok deletes its local data (including unpublished **drafts**). Read
[docs/INSTALL.md](docs/INSTALL.md) first.

Quick version (computer, Java 21+):

```bash
./gradlew :patches:buildAndroid          # or download a released patches-*.mpp
scripts/unscroll-patch.sh --apk TikTok-47.4.3.apkm \
  --bundle patches/build/libs/patches-0.1.0.mpp \
  --keystore ~/unscroll.keystore --arch arm64-v8a
```

## Build from source

Requirements: JDK 21, a GitHub token with `read:packages` (Morphe publishes its patcher
on GitHub Packages).

```bash
export GITHUB_ACTOR=<your-github-user>
export GITHUB_TOKEN=<token with read:packages>
./gradlew :extensions:tiktok:testDebugUnitTest :patches:test   # tests
./gradlew :patches:buildAndroid                                 # patches/build/libs/patches-<version>.mpp
```

## Compatibility

| TikTok | Package | State |
|---|---|---|
| 47.4.3 | `com.zhiliaoapp.musically` | patches apply (verified locally); device QA pending |

Full table and APK requirements: [docs/COMPATIBILITY.md](docs/COMPATIBILITY.md).

## Limitations

* One pinned TikTok version at a time; TikTok updates need new patch work.
* The official TikTok has to be uninstalled first (different signing key).
* TikTok can change server behaviour at any time; Unscroll cannot control the server.
* Patching an app may violate TikTok's terms of service. You use Unscroll at your own
  risk and on your own account.

## Privacy and security

Unscroll adds no telemetry, no analytics, no servers and no network requests. It logs
only fixed diagnostic strings to Android's local log, never user content. It does not
bypass login, CAPTCHA or account protection. See [SECURITY.md](SECURITY.md).

## Documentation

* [Research](docs/RESEARCH.md) · [Decisions](docs/DECISIONS.md) · [Architecture](docs/ARCHITECTURE.md)
* [Install](docs/INSTALL.md) · [Compatibility](docs/COMPATIBILITY.md) · [Troubleshooting](docs/TROUBLESHOOTING.md)
* [Test report](docs/TEST_REPORT.md) · [QA checklist](docs/QA_CHECKLIST.md) · [Contributing](CONTRIBUTING.md) · [Changelog](CHANGELOG.md)

## License

GPL-3.0 with the additional section 7 terms inherited from Morphe-derived code — see
[LICENSE](LICENSE) and [NOTICE](NOTICE). Unscroll adapts hook points from
[Metra TikTok Patches](https://github.com/icysymmetra/tiktok-patches-for-morphe) and
[ReVanced Patches](https://gitlab.com/ReVanced/revanced-patches).

Unscroll is not affiliated with TikTok, ByteDance, Morphe or ReVanced. "TikTok" is a
trademark of its owner and is used only to describe compatibility.
