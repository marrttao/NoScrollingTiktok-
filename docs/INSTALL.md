# Installing Unscroll

> **Read this whole page before uninstalling anything.** Replacing TikTok with Unscroll
> deletes TikTok's local data on your phone. Unpublished drafts are the main thing you
> can lose.

## 1. Why the official TikTok has to go

Android only lets an app be updated by an APK signed with the **same key**. Unscroll is
signed with your own key (TikTok's key is private), and it keeps TikTok's package ID,
so:

* it cannot be installed over the official TikTok;
* the official TikTok must be **uninstalled first**;
* uninstalling deletes everything TikTok stored *on the phone*.

| Data | After uninstalling the official app |
|---|---|
| Account, followers, published videos, likes, favourites | Kept (stored on TikTok's servers) |
| Direct messages | Kept on the server; history reloads after login |
| **Drafts** (unpublished videos) | **Deleted** unless saved first |
| Local settings, cache, downloaded offline videos | Deleted |
| Login session | Deleted — you sign in again |

## 2. Before uninstalling — checklist

1. **Drafts.** Open *Profile → Drafts*. For each draft you want to keep, open it and
   save it to the phone gallery (*Post → ⋯ → Save to device*, or post it as
   *Only me*). Draft files in TikTok's private storage cannot be copied out without root.
2. **Login.** Make sure you can sign in without the official app:
   * know your password (*Settings and privacy → Account → Password*), or
   * have access to the phone number / e-mail on the account.
   Google sign-in uses TikTok's browser flow in Unscroll (patch *Fix Google login*);
   having a password as a fallback is still strongly recommended.
3. **Two-step verification.** Keep access to the second factor.
4. **Google Play auto-update.** Open TikTok's Play Store page → ⋮ → untick
   *Enable auto update*. (Play cannot update Unscroll anyway, but it will keep
   offering to.)

## 3. Get the original APK

Unscroll patches only one exact TikTok build. Check `docs/COMPATIBILITY.md` for the
supported version (currently **47.4.3**, package `com.zhiliaoapp.musically`).

Download the original, unmodified APK or APKMirror bundle (`.apkm`) of exactly that version from a
source that verifies TikTok's signature, e.g. APKMirror. Do not use already-modified
APKs.

## 4. Patch

### Option A — on the phone with Morphe Manager (no computer)

1. Install Morphe Manager from the official Morphe project.
2. *Patches → add source* → the Unscroll repository URL (available once a release is
   published).
3. Select the TikTok APK, keep the default Unscroll patch selection, patch.
4. Morphe Manager signs the app with its own key stored on the phone. Keep Morphe
   Manager installed (or export its keystore) — future Unscroll updates must be signed
   with the same key.

### Option B — on a computer (Linux/macOS/WSL, Java 21+)

```bash
scripts/unscroll-patch.sh \
  --apk TikTok-47.4.3.apkm \
  --bundle patches-<version>.mpp \
  --keystore ~/unscroll.keystore \
  --out unscroll-47.4.3.apk
```

* The script downloads a pinned Morphe Desktop CLI and checks its SHA-256.
* If **any** Unscroll patch fails, no APK is produced.
* `~/unscroll.keystore` is created on first use. **Back it up.** Losing it means the
  next update requires uninstalling again (and losing local data again).

## 5. Install

1. Complete the checklist in section 2.
2. Uninstall the official TikTok.
3. Install the patched APK (open it on the phone, or `adb install unscroll-47.4.3.apk`).
4. Open it and sign in.

## 6. Updating

Only update when `docs/COMPATIBILITY.md` lists a new supported TikTok version. Patch
the new original APK with the **same keystore** and install it over Unscroll — local
data is kept.

## 7. Going back to the official TikTok

Uninstall Unscroll (local data is deleted again — save drafts first) and install TikTok
from Google Play.
