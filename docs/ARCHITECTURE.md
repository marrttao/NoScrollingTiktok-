# Architecture

Unscroll is a **patch bundle** for the [Morphe](https://github.com/MorpheApp) patcher.
The bundle is applied by the user to an original TikTok APK they obtained themselves;
the repository never contains TikTok code or binaries.

```
             original TikTok APK (user-supplied, pinned version)
                                 │
          ┌──────────────────────┴───────────────────────┐
          │  Morphe patcher (Morphe Manager on the phone, │
          │  or Morphe Desktop CLI)                       │
          │    + patches-<version>.mpp  (this repository) │
          └──────────────────────┬───────────────────────┘
                                 │  bytecode patches + extension dex, re-signed
                                 ▼
                     Unscroll (package com.zhiliaoapp.musically)
```

## Repository layout

| Path | Contents |
|---|---|
| `patches/` | Kotlin patch definitions. Each patch finds methods in TikTok by **fingerprint** (shape of a method: class, name, return type, parameters, strings, referenced fields) and injects smali that calls the extension. Compiled to JVM classes + `classes.dex` inside the `.mpp`. |
| `extensions/tiktok/` | Java code merged into TikTok at patch time (`extensions/tiktok.mpe`). Holds every decision (which tab, which link, which video may play) as plain, unit-tested Java. |
| `extensions/tiktok/stub/` | Compile-only stubs for TikTok classes, never packaged. |
| `docs/` | Research, decisions, architecture, compatibility, QA. |

Build tooling is provided by the Morphe Gradle plugin (`app.morphe.patches`):
`./gradlew :patches:buildAndroid` produces `patches/build/libs/patches-<version>.mpp`.

## Design rules

1. **Thin hooks, thick extension.** A hook only forwards values to an extension method.
   All logic lives in Java where it is testable on the JVM.
2. **Layered blocking.** A feed has to get through three independent layers:
   * *data layer* – feed responses are emptied (`Block video feeds`);
   * *navigation layer* – the UI entries to feeds are removed or redirected
     (`Restrict navigation`, start screen, deep-link guard, search restriction);
   * *paging layer* – a video screen shows exactly one video (single-video patch).
   Losing one layer to a TikTok change does not re-open a feed by itself.
3. **Fail closed at build time.** Every patch is pinned to the TikTok versions in
   `patches/.../shared/Constants.kt`. Fingerprints that do not resolve make the patch
   fail; the build script refuses to produce an APK if any Unscroll patch failed.
4. **Fail safe at run time.** If an extension method meets an unexpected object shape it
   logs the failure type (never user data) and leaves the data-layer guard in place.
   It never throws into TikTok code.
5. **No settings, no off switch, no telemetry, no network calls of our own.**

## Patches

Status legend: **implemented** = code exists and compiles; **verified** = applied to the
pinned TikTok APK and checked on a device. See `docs/COMPATIBILITY.md` for the current
verification state; nothing is "verified" until that table says so.

| Patch | Decision | Hook points (TikTok 47.4.3) | Status |
|---|---|---|---|
| Block feed endpoints | P1, P8, P9, P11 | every `com.bytedance.retrofit2.client.Request` constructor: the URL passes through `FeedEndpointPolicy`; feed API paths are redirected to an unreachable local address | applies |
| Restrict navigation | P1, P2 | `enable()` of the Friends/Shop/Explore/Discover bottom-tab protocols returns false; `TabChangeManager` tab switch redirected by `TabPolicy` (Home and unknown tabs open Inbox) | applies |
| Disable video swipe | P3 | `VerticalViewPager.onInterceptTouchEvent/onTouchEvent/canScrollVertically` return false | applies |
| Friends-only profile playback | P5–P7 | end of `DetailActivity.onCreate`: `ProfilePlaybackGuard` reads TikTok's `video_from` / `follow_status` extras and closes the screen for non-friends | applies |
| Block feed screens | P9, P11 | end of `onCreate` of `LivePlayActivity`, `ChallengeDetailActivity`, `MusicDetailActivity` → `BlockedScreen.close` | applies |
| Fix Google login | T5 | `GoogleAuth` availability → false, TikTok uses browser sign-in | applies |
| Accounts-only search | P8 | video/hashtag/sound/LIVE search results blocked by the endpoint guard; account search stays | data layer only — the result tabs still exist but stay empty |
| Deep links | P12 | covered by the layers above: feed links land on Home (redirected to Inbox), blocked endpoints or blocked screens; single-video links open `DetailActivity` | no dedicated hook |
| Stories: no auto-advance between authors | P10 | story pager | **not implemented** |
| Programmatic auto-advance (TikTok's opt-in "Auto scroll") | P3 | pager `setCurrentItem` paths | **not implemented** — auto scroll is off by default; see QA C3 |

Removed during reverse engineering of 47.4.3: emptying `FeedItemList.getItems()` —
TikTok also uses `FeedItemList` for profile video lists, so it would have broken
profiles. The endpoint guard covers the same data without that side effect.

## Signing and installation model

Morphe re-signs the patched APK with the build publisher's keystore. For self-built
APKs, Morphe Manager keeps the user's keystore on the phone; the CLI accepts
`--keystore`. A channel APK can use a different key from a self-built APK.
Android only installs an update signed with the same key, therefore:

* The official TikTok must be uninstalled before the first Unscroll install (its local
  data — including unpublished drafts — is lost; see `docs/INSTALL.md`).
* Every later Unscroll build must be signed with the same keystore.
* Google Play must not auto-update TikTok over Unscroll (it would fail anyway).

## Testing strategy

| Level | What | Where |
|---|---|---|
| Unit | Extension policies (`TabPolicy`, `LinkPolicy`, `PlaybackPolicy`, `FeedGuard`) | `./gradlew :extensions:tiktok:testDebugUnitTest`, CI |
| Build | Patch bundle compiles, contains `classes.dex` and the extension | CI |
| Patch application | Every Unscroll patch applies to the pinned TikTok APK; no failed patch | `scripts/` + CI job (APK never uploaded) |
| Device | Manual QA checklist (`docs/QA_CHECKLIST.md`) | owner's phone |
