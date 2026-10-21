package app.unscroll.patches.tiktok.navigation

import app.morphe.patcher.patch.bytecodePatch
import app.unscroll.patches.tiktok.shared.EXTENSION_PACKAGE
import app.unscroll.patches.tiktok.shared.TIKTOK_COMPATIBILITY
import app.unscroll.patches.tiktok.shared.beforeEveryObjectReturn
import app.unscroll.patches.tiktok.shared.unscrollExtensionPatch

private const val TAB_POLICY = "$EXTENSION_PACKAGE/navigation/TabPolicy;"

/**
 * Navigation layer of decision P1: the bottom bar keeps only Create, Inbox and
 * Profile; the Home feed's top tabs are reduced to one (whose content is empty).
 */
@Suppress("unused")
val restrictNavigationPatch = bytecodePatch(
    name = "Restrict navigation",
    description = "Removes Home, Friends, Shop and unknown tabs from the bottom bar and feed tabs from the top bar.",
) {
    dependsOn(unscrollExtensionPatch)
    compatibleWith(TIKTOK_COMPATIBILITY)

    execute {
        BottomTabModelListFingerprint.method.beforeEveryObjectReturn(
            """
                invoke-static/range { {reg} .. {reg} }, $TAB_POLICY->filterBottomTabs(Ljava/util/List;)Ljava/util/List;
                move-result-object {reg}
            """,
        )
        TopTabModelListFingerprint.method.beforeEveryObjectReturn(
            """
                invoke-static/range { {reg} .. {reg} }, $TAB_POLICY->filterTopTabs(Ljava/util/List;)Ljava/util/List;
                move-result-object {reg}
            """,
        )
    }
}
