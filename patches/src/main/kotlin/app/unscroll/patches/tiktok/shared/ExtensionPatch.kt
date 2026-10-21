package app.unscroll.patches.tiktok.shared

import app.morphe.patcher.patch.bytecodePatch

/** Merges the Unscroll extension classes (app.unscroll.extension.tiktok.*) into TikTok. */
internal val unscrollExtensionPatch = bytecodePatch {
    extendWith("extensions/tiktok.mpe")
}
