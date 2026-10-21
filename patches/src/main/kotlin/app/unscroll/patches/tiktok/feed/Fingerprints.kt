/*
 * Hook points adapted from Metra TikTok Patches (GPL-3.0),
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 * (patches/.../tiktok/feedfilter/Fingerprints.kt), itself forked from ReVanced Patches.
 * Unscroll uses them to empty feeds instead of filtering them.
 */
package app.unscroll.patches.tiktok.feed

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

internal const val FEED_API_SERVICE = "Lcom/ss/android/ugc/aweme/feed/FeedApiService;"
internal const val FEED_ITEM_LIST = "Lcom/ss/android/ugc/aweme/feed/model/FeedItemList;"
internal const val FOLLOW_FEED_LIST = "Lcom/ss/android/ugc/aweme/follow/presenter/FollowFeedList;"
internal const val FRIENDS_FEED_RESPONSE = "Lcom/ss/android/ugc/aweme/friendstab/api/FriendsFeedResponse;"
private const val FRIENDS_FEED = "Lcom/ss/android/ugc/aweme/feed/model/friends/FriendsFeed;"
private const val AWEME = "Lcom/ss/android/ugc/aweme/feed/model/Aweme;"

private fun com.android.tools.smali.dexlib2.iface.Method.fieldReferences() =
    implementation?.instructions
        ?.mapNotNull { (it as? ReferenceInstruction)?.reference as? FieldReference }
        .orEmpty()

private fun com.android.tools.smali.dexlib2.iface.Method.methodReferences() =
    implementation?.instructions
        ?.mapNotNull { (it as? ReferenceInstruction)?.reference as? MethodReference }
        .orEmpty()

/** Network fetch of the main video feed (For You and related FeedApiService feeds). */
internal object MainFeedFetchFingerprint : Fingerprint(
    definingClass = FEED_API_SERVICE,
    name = "fetchFeedList",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = FEED_ITEM_LIST,
    custom = { method, classDef ->
        classDef.type == FEED_API_SERVICE && method.parameterTypes.size == 1
    },
)

/** Every reader of a main-feed page goes through this getter. */
internal object FeedItemListGetItemsFingerprint : Fingerprint(
    definingClass = FEED_ITEM_LIST,
    name = "getItems",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "Ljava/util/List;",
    parameters = emptyList(),
    custom = { method, _ ->
        method.fieldReferences().any {
            it.definingClass == FEED_ITEM_LIST && it.name == "items" && it.type == "Ljava/util/List;"
        }
    },
)

/** Every reader of a Following-feed page goes through this getter. */
internal object FollowFeedListGetItemsFingerprint : Fingerprint(
    definingClass = FOLLOW_FEED_LIST,
    name = "getItems",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "Ljava/util/List;",
    parameters = emptyList(),
)

/** Network response handler of the Friends tab feed. */
internal object FriendsFeedNetworkResponseFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Ljava/lang/Object;"),
    strings = listOf("client_read_gids_report_time"),
    custom = { method, _ ->
        method.fieldReferences().any {
            it.definingClass == FRIENDS_FEED_RESPONSE && it.name == "friendFeedData" && it.type == "Ljava/util/List;"
        } && method.methodReferences().any {
            it.definingClass == FRIENDS_FEED && it.name == "getAweme" && it.returnType == AWEME
        }
    },
)
