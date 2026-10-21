# Product and technical decisions

Decisions agreed with the project owner on 2026-10-21. Changing any of them
requires an explicit discussion; the patches implement exactly these rules.

## Technical

| # | Decision | Rationale |
|---|---|---|
| T1 | Patch framework: **Morphe** (patch bundle `.mpp`, Kotlin patches + Java extension) | Active, on-device and desktop patching, verified TikTok 46.2.3 hook points exist upstream (see RESEARCH.md). |
| T2 | Target: global TikTok `com.zhiliaoapp.musically`, single pinned version (initially 46.2.3, moved to **47.4.3** on 2026-10-21 because that is the build the owner obtained; 46.2.3 upstream hooks were re-checked and replaced) | Fingerprints are version-specific; one pinned version keeps behaviour verifiable. |
| T3 | Keep the original package ID | Unscroll replaces TikTok, it does not run next to it. |
| T4 | Owner device: official Google Play TikTok, Android 12+ | Determines APK architecture (arm64) and install path. |
| T5 | All login methods must keep working (email, phone, username, Google) | Owner requirement. Google Sign-In needs special handling after re-signing; see ARCHITECTURE.md. |
| T6 | No settings screen, no "disable restrictions" switch, no telemetry | Product principles 5–7. |

## Product rules

| # | Rule |
|---|---|
| P1 | For You, Following, Friends feed, LIVE feed, Explore/Nearby, Shop and Auto scroll are unreachable. |
| P2 | App starts on Inbox (messages). |
| P3 | Any opened video is a **single video**: no vertical paging to another video, no auto-advance, no "related videos". When it ends it **loops** (stock TikTok behaviour). |
| P4 | Videos shared in Direct Messages, and videos opened from a system/inbox notification, play regardless of who posted them. |
| P5 | Own profile: videos play (one at a time). |
| P6 | **Friend** profiles (friend = mutual follow, TikTok's own definition): videos play one at a time, including the Reposts / Favorites tabs. |
| P7 | Non-friend profiles: profile page and grid are visible, but **videos do not play** from it. |
| P8 | Search: **accounts only**. Video, hashtag, sound, LIVE, shop and "top/mixed" search results are blocked. Music search inside the video editor stays. |
| P9 | Hashtag and sound pages are blocked (they are feeds). |
| P10 | Friends' stories are kept. Automatic advance from one story author to the next is not allowed. |
| P11 | Watching other people's LIVE streams is blocked. Starting **own** LIVE is kept. |
| P12 | Deep links (`tiktok.com/...`, `snssdk1233://...`) to a specific video open that single video under rule P3 (a link pasted from another messenger is treated like a DM share, P4); links to feeds, hashtags, sounds, search or LIVE rooms are redirected to Inbox. |

## Distribution

| # | Decision |
|---|---|
| D1 | Source code and developer documentation live on GitHub. The primary download and update destination is the owner's Telegram channel, [@SayNoToScrolling](https://t.me/SayNoToScrolling). |
| D2 | The owner has selected **mod APK versions distributed through Telegram** as the user-facing download path. The repository directs users to the channel for release availability, version status and installation instructions; it does not store target APKs or signing keys. Self-building the `.mpp` patch bundle and patching an original TikTok APK remain documented options for developers. This distribution decision does not establish that an APK has been published or tested. |

## Pending (ask before acting)

* Making the GitHub repository public and publishing a release.
* Uninstalling the official TikTok on the owner's device (data-loss warning required).
