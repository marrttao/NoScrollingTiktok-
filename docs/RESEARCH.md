# Unscroll — Phase 1 Research

Status: **initial research, read-only**. Date: 2026-10-21.
Everything below is either verified in the build environment (marked *verified*) or
taken from upstream project documentation (marked *upstream claim*, not yet verified
by us). Nothing in this document claims that a patch works on a device.

## 1. Goal restated

Patch the **original** TikTok Android APK so that every "infinite video feed"
(For You, Following, Friends feed, vertical next-video paging, related videos, etc.)
is unreachable, while messaging, creator tools and account features keep working.
Distribute only patch source code and build tooling, never TikTok binaries.

## 2. Existing approaches evaluated

| Approach | How it works | Pros | Cons | Verdict |
|---|---|---|---|---|
| **Morphe** patcher + patch bundle (`.mpp`) | Bytecode patching with fingerprints (method shape matching), optional Java "extension" code merged into the APK. Fork/continuation of ReVanced, GPLv3 + §7 naming notice. | Active (Morphe Desktop v1.18.1, dev builds published 2026-10-21). On-device patching with Morphe Manager (no PC required) **and** desktop CLI. Existing TikTok patch set (Metra) with fingerprints verified for TikTok 46.2.3. Reproducible: same APK + same bundle = same patch. | Patches are tied to one exact TikTok version; each TikTok update needs fingerprint re-validation. Patcher artifacts are hosted on GitHub Packages (needs a token in CI). | **Recommended** |
| **ReVanced** patcher + patches | Same model (Morphe forked from it). | Large community. | TikTok patches stuck on TikTok 36.5.4 (2024 build); multiple open 2026 issues report TikTok patches failing to apply ([manager#3539](https://github.com/ReVanced/revanced-manager/issues/3539), [manager#3348](https://github.com/ReVanced/revanced-manager/issues/3348)). Old TikTok builds risk forced-update prompts. | Rejected for now |
| **LSPatch + Xposed module** (e.g. NexAlloy build) | Embeds an Xposed framework into the APK, hooks at runtime. | Less version-sensitive. | Heavier runtime (whole hook framework injected, signature-bypass loader), harder to audit, upstream LSPatch maintenance uncertain, "fail-closed" is harder (hooks silently miss). | Rejected |
| **apktool + hand-written smali diffs** | Decompile, edit smali, rebuild. | No framework. | Resource rebuild of TikTok is fragile (reported resource failures), diffs are not portable across versions, no fingerprint matching. | Rejected |
| Accessibility service / overlay blocker | Separate app watching TikTok's UI. | No APK modification. | Explicitly excluded by the product brief. | Excluded |

### Prior art we can legally reuse

* **Metra TikTok Patches** — `github.com/icysymmetra/tiktok-patches-for-morphe`,
  GPL-3.0 + Morphe §7 notice, v0.8.1 (2026-10-15), target `com.zhiliaoapp.musically`
  **46.2.3** (*upstream claim*). It already contains fingerprints for the main feed
  response, Following feed, Friends feed, search mixed feed, profile lists, cached
  feed insertion, auto-scroll gate, video looping and tab navigation (*verified* by
  listing the classes in the published `patches-0.8.1.mpp`).
  Its purpose is the opposite of ours (better feed), but the **hook points** are
  exactly the ones we need to cut. Reusing them obliges Unscroll to be GPLv3 and to
  keep upstream notices; Morphe's §7 notice forbids using the name "Morphe" in our
  product name (we use "Unscroll" — compliant).

## 3. Target app facts

| Item | Value | Source |
|---|---|---|
| Global package ID | `com.zhiliaoapp.musically` | upstream claim |
| Asian package ID | `com.ss.android.ugc.trill` | upstream claim (ReVanced) |
| Version with verified fingerprints | 46.2.3 | Metra README (upstream claim) |
| Required APK form | full/merged universal APK (not split APKs bundle) | Morphe docs (upstream claim) |

## 4. Signing and install constraints (Android platform rules)

* A patched APK **must be re-signed** with our own key. Android refuses to install
  an update whose signing certificate differs from the installed app. Therefore the
  patched app **cannot be installed over the official TikTok**; the official app
  must be uninstalled first, which deletes its local data (unpublished **drafts**,
  cache, local settings). Server-side data (account, published videos, messages)
  is not affected by uninstalling.
* Keeping the original package ID is technically possible (TikTok then cannot be
  installed in parallel — which matches the "replacement" requirement). Google Play
  will offer TikTok "updates" that fail with a signature conflict; auto-update for
  TikTok should be disabled in Play.
* Google Sign-In is bound to the app's signing certificate; after re-signing,
  "Continue with Google" is expected to fail unless a signature-spoofing patch is
  applied. Email/phone/username login is expected to keep working (*upstream claim*,
  to be verified on device).
* The same keystore must be reused for every future Unscroll build, otherwise every
  update again requires uninstalling and losing local data. The keystore is the
  user's and must never be committed.

## 5. Build environment findings (this session)

| Check | Result |
|---|---|
| JDK | OpenJDK 21 available (*verified*) — Morphe requires Java 21+ |
| Gradle | 8.14.3 available (*verified*) |
| Maven Central, Gradle Plugin Portal | reachable (*verified*) |
| Google Maven (`dl.google.com`) | **blocked** by network policy (*verified*) — no AGP / d8 locally |
| GitHub Packages, JitPack | **blocked** (*verified*) — Morphe patcher dependency not resolvable locally |
| GitHub release downloads | reachable (*verified*) — Morphe Desktop `morphe-desktop-1.18.1-all.jar` (contains the patcher API, smali and apksig) downloaded |
| APKMirror / APKPure | **blocked** (*verified*) — no way to obtain a TikTok APK from here |
| `/dev/kvm`, Android emulator, ADB device | **not available** (*verified*) — no on-device testing from this container |

Consequences:
1. Patch code can be compiled locally against the Morphe Desktop jar; the official
   build path (Gradle + Morphe plugin) will run in GitHub Actions where GitHub
   Packages and Google Maven are reachable.
2. **Without a TikTok 46.2.3 APK in the environment, new hook points cannot be
   reverse-engineered and no patch can be applied or verified here.** This is the
   main blocker.
3. Device testing must be performed by the owner of the device following a manual
   QA checklist, unless a device becomes reachable.

## 6. Hook strategy (to be validated against the real APK)

Layered, fail-closed:

1. **Data layer (primary)** — drop every item returned by the recommendation
   (For You), Following, Friends-feed and "related/more videos" endpoints, and the
   cached/cold-start feed insertions. Even if a UI path to a feed survives, it
   receives an empty list. Metra fingerprints cover most of these endpoints.
2. **Navigation layer** — remove Home/Friends tabs and the top feed tabs; start the
   app on an allowed screen; route deep links to feed screens to an allowed screen.
3. **Paging layer** — in single-video screens (shared video, notification target,
   profile grid item) disable vertical paging and auto-advance, so exactly one
   video is shown.
4. **Integrity check** — if any mandatory fingerprint fails to match, the patch
   bundle fails the build (Morphe aborts on failed patches); the build is never
   labelled "Unscroll" with a missing blocker.

No settings screen and no "temporarily disable" switch will be added.

## 7. Open questions (asked to the project owner)

See the question block in the session; answers will be recorded in
`docs/DECISIONS.md`.
