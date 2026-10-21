package app.unscroll.patches.tiktok.navigation

import app.morphe.patcher.Fingerprint

internal const val TAB_CHANGE_MANAGER = "Lcom/ss/android/ugc/aweme/main/TabChangeManager;"

/**
 * TikTok's central bottom-tab switch: (tag, ..., Bundle, enterMethod). Used for user taps,
 * deep links and the initial tab at start-up. Checked on TikTok 47.4.3.
 */
internal object SwitchTabFingerprint : Fingerprint(
    definingClass = TAB_CHANGE_MANAGER,
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "Z", "I", "Z", "Landroid/os/Bundle;", "Ljava/lang/String;"),
    strings = listOf(", tag:"),
)

/**
 * Bottom-tab protocols whose tab leads to a feed. Each has an `enable()Z` that TikTok
 * itself uses to switch tabs off per region; Unscroll makes it return false.
 * Home is not listed: TikTok's main screen depends on it, so it stays in the model and
 * every switch to it is redirected (see [SwitchTabFingerprint]).
 */
internal val DISABLED_BOTTOM_TAB_PROTOCOLS = listOf(
    "Lcom/ss/android/ugc/aweme/tab/FriendsTabProtocol;",
    "Lcom/ss/android/ugc/aweme/ecommerce/mall/ShopBottomTabProtocol;",
    "Lcom/ss/android/ugc/aweme/explore/entrance/ExploreBottomTabProtocol;",
    "Lcom/ss/android/ugc/aweme/newfollow/ui/DiscoverCompassTabProtocol;",
)
