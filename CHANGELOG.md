# Changelog

All notable changes are documented here. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/); versions follow SemVer.

## [Unreleased]

### Added
- Target **TikTok 47.4.3** (`com.zhiliaoapp.musically`), APKMirror bundle `.apkm` supported.
- *Block feed endpoints*: feed API requests (For You, Following, Friends, Explore, Nearby,
  LIVE feed, video search results, hashtag/sound/playlist lists) never leave the phone.
- *Restrict navigation*: Friends/Shop/Explore/Discover tabs off; Home and unknown tabs
  open Inbox, including at start-up.
- *Disable video swipe*: the vertical video pager ignores swipes; every video is single.
- *Friends-only profile playback*: videos from non-friends' profile grids do not open.
- *Block feed screens*: other people's LIVE rooms, hashtag pages and sound pages close.
- *Fix Google login*: browser-based Google sign-in for the re-signed app.
- Policies with unit tests; patch metadata tests; reproducible fail-closed patch script;
  CI building the bundle and (with a private APK URL) applying it.

### Changed
- Removed the 46.2.3 hooks taken from Metra after they failed on 47.4.3; all hooks were
  re-derived from 47.4.3 with stable anchors (class names, API paths, Android overrides).

### Not yet verified
- All patches apply to 47.4.3 and the injected code was checked in the patched dex.
  Nothing has been verified on a device yet.
- Not implemented: no auto-advance between story authors (P10); blocking TikTok's opt-in
  "Auto scroll" (off by default).
