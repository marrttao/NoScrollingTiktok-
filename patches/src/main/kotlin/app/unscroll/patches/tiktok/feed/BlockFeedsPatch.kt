package app.unscroll.patches.tiktok.feed

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.unscroll.patches.tiktok.shared.EXTENSION_PACKAGE
import app.unscroll.patches.tiktok.shared.TIKTOK_COMPATIBILITY
import app.unscroll.patches.tiktok.shared.beforeEveryObjectReturn
import app.unscroll.patches.tiktok.shared.returnEmptyMutableList
import app.unscroll.patches.tiktok.shared.unscrollExtensionPatch

private const val FEED_GUARD = "$EXTENSION_PACKAGE/feed/FeedGuard;"

/**
 * Data layer of decision P1: the For You, Following and Friends feeds receive no
 * videos, whatever UI path reaches them.
 */
@Suppress("unused")
val blockVideoFeedsPatch = bytecodePatch(
    name = "Block video feeds",
    description = "Empties the For You, Following and Friends video feeds at the data layer.",
) {
    dependsOn(unscrollExtensionPatch)
    compatibleWith(TIKTOK_COMPATIBILITY)

    execute {
        MainFeedFetchFingerprint.method.beforeEveryObjectReturn(
            "invoke-static/range { {reg} .. {reg} }, $FEED_GUARD->emptyFeedItemList(Ljava/lang/Object;)V",
        )
        FeedItemListGetItemsFingerprint.method.returnEmptyMutableList()
        FollowFeedListGetItemsFingerprint.method.returnEmptyMutableList()
        FriendsFeedNetworkResponseFingerprint.method.addInstruction(
            0,
            "invoke-static/range { p1 .. p1 }, $FEED_GUARD->emptyFriendsFeedResponse(Ljava/lang/Object;)V",
        )
    }
}
