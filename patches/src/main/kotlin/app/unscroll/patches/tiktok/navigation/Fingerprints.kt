/*
 * Hook points adapted from Metra TikTok Patches (GPL-3.0),
 * https://github.com/icysymmetra/tiktok-patches-for-morphe
 * (patches/.../tiktok/misc/navigation/Fingerprints.kt).
 * The method names are obfuscated and valid for TikTok 46.2.3 only.
 */
package app.unscroll.patches.tiktok.navigation

import app.morphe.patcher.Fingerprint

/** Builds the list of top tabs above the Home feed. */
internal object TopTabModelListFingerprint : Fingerprint(
    definingClass = "/TabAbilityAssem;",
    name = "W52",
    returnType = "Ljava/util/List;",
    parameters = listOf(),
)

/** Builds the list of bottom navigation tabs. */
internal object BottomTabModelListFingerprint : Fingerprint(
    definingClass = "/TabAbilityAssem;",
    name = "OA",
    returnType = "Ljava/util/List;",
    parameters = listOf(),
)
