package app.unscroll.patches.tiktok.video

import app.morphe.patcher.patch.bytecodePatch
import app.unscroll.patches.tiktok.shared.EXTENSION_PACKAGE
import app.unscroll.patches.tiktok.shared.TIKTOK_COMPATIBILITY
import app.unscroll.patches.tiktok.shared.activityOnCreate
import app.unscroll.patches.tiktok.shared.beforeEveryVoidReturn
import app.unscroll.patches.tiktok.shared.unscrollExtensionPatch

/** TikTok's single-video screen. Profile grids open it with video_from / follow_status extras. */
internal const val DETAIL_ACTIVITY = "Lcom/ss/android/ugc/aweme/detail/ui/DetailActivity;"

private const val GUARD = "$EXTENSION_PACKAGE/playback/ProfilePlaybackGuard;"

/**
 * Decisions P5–P7: videos opened from the profile grid of someone who is not a friend
 * (mutual follow) close immediately with a short notice. Own and friends' profiles,
 * DMs, notifications and links are not affected.
 */
@Suppress("unused")
val profilePlaybackRulePatch = bytecodePatch(
    name = "Friends-only profile playback",
    description = "Videos from profile grids play only on your own profile and on friends' (mutual follow) profiles.",
) {
    dependsOn(unscrollExtensionPatch)
    compatibleWith(TIKTOK_COMPATIBILITY)

    execute {
        activityOnCreate(DETAIL_ACTIVITY).beforeEveryVoidReturn(
            "invoke-static/range { p0 .. p0 }, $GUARD->onVideoScreenCreated(Landroid/app/Activity;)V",
        )
    }
}
