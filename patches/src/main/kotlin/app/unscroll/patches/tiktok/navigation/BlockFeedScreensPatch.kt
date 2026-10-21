package app.unscroll.patches.tiktok.navigation

import app.morphe.patcher.patch.bytecodePatch
import app.unscroll.patches.tiktok.shared.EXTENSION_PACKAGE
import app.unscroll.patches.tiktok.shared.TIKTOK_COMPATIBILITY
import app.unscroll.patches.tiktok.shared.activityOnCreate
import app.unscroll.patches.tiktok.shared.beforeEveryVoidReturn
import app.unscroll.patches.tiktok.shared.unscrollExtensionPatch

private const val BLOCKED_SCREEN = "$EXTENSION_PACKAGE/navigation/BlockedScreen;"

/**
 * Screens that only exist to browse a feed. Own LIVE uses LiveBroadcastActivity and the
 * editor's music picker uses MusicDetailListActivity; neither is listed.
 */
internal val FEED_SCREENS = listOf(
    "Lcom/ss/android/ugc/aweme/live/LivePlayActivity;",               // watching someone's LIVE
    "Lcom/ss/android/ugc/aweme/challenge/ui/ChallengeDetailActivity;", // hashtag page
    "Lcom/ss/android/ugc/aweme/music/ui/MusicDetailActivity;",         // sound page
)

/** Decisions P9 and P11: LIVE rooms of others, hashtag pages and sound pages close at once. */
@Suppress("unused")
val blockFeedScreensPatch = bytecodePatch(
    name = "Block feed screens",
    description = "Closes other people's LIVE rooms, hashtag pages and sound pages. Starting your own LIVE is kept.",
) {
    dependsOn(unscrollExtensionPatch)
    compatibleWith(TIKTOK_COMPATIBILITY)

    execute {
        FEED_SCREENS.forEach { screen ->
            activityOnCreate(screen).beforeEveryVoidReturn(
                "invoke-static/range { p0 .. p0 }, $BLOCKED_SCREEN->close(Landroid/app/Activity;)V",
            )
        }
    }
}
