# Troubleshooting

## Patching

**`Morphe Desktop checksum mismatch`** — the cached CLI jar is damaged or was replaced.
The script deletes it; run again. If it persists, do not continue and open an issue.

**A patch fails / `at least one patch failed`** — your APK is not exactly the supported
version, is a split bundle, or is already modified. Check with
`aapt2 dump badging TikTok.apk | head -1` that `name='com.zhiliaoapp.musically'` and
`versionName` matches `docs/COMPATIBILITY.md`. Unscroll intentionally produces no APK
rather than a partially patched one.

**`Could not resolve app.morphe:morphe-patcher` while building** — Gradle needs a GitHub
token with `read:packages` (see CONTRIBUTING.md). A `401` means the token is missing or
lacks the scope.

## Installing

**`App not installed` / `INSTALL_FAILED_UPDATE_INCOMPATIBLE`** — the official TikTok (or
an Unscroll build signed with a different key) is still installed. Follow
`docs/INSTALL.md` section 2 before uninstalling it.

**Play Store offers a TikTok update** — ignore it and disable auto-update for TikTok.
The update cannot be installed over Unscroll.

## Using

**Google sign-in fails** — Unscroll uses TikTok's browser-based Google sign-in. Make
sure a browser is installed and enabled. As a fallback, sign in with phone/e-mail and
password (set a password in the official app before switching, if you can).

**A feed or a "next video" swipe is still reachable** — that is a bug. Open an issue
with the steps (no screenshots of other people's content or names), the bundle version
and the TikTok version.

**Something you need (messages, posting, drafts…) is broken** — also a bug; Unscroll
must not remove those. Include the QA checklist line that fails.

**Collecting a log without personal data** — Unscroll writes only under the tag
`Unscroll`: `adb logcat -s Unscroll`. TikTok's own log lines are not needed; please do
not attach full `logcat` output, which can contain personal data.
