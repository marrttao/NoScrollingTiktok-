package app.unscroll.patches.tiktok.navigation

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.unscroll.patches.tiktok.shared.EXTENSION_PACKAGE
import app.unscroll.patches.tiktok.shared.TIKTOK_COMPATIBILITY
import app.unscroll.patches.tiktok.shared.unscrollExtensionPatch

private const val TAB_POLICY = "$EXTENSION_PACKAGE/navigation/TabPolicy;"

/**
 * Navigation layer of decisions P1 and P2: Friends, Shop, Explore and Discover tabs are
 * switched off, and every switch to a tab other than Create, Inbox or Profile (including
 * the start-up switch to Home) opens Inbox instead.
 */
@Suppress("unused")
val restrictNavigationPatch = bytecodePatch(
    name = "Restrict navigation",
    description = "Starts on Inbox and keeps only the Create, Inbox and Profile tabs reachable.",
) {
    dependsOn(unscrollExtensionPatch)
    compatibleWith(TIKTOK_COMPATIBILITY)

    execute {
        DISABLED_BOTTOM_TAB_PROTOCOLS.forEach { type ->
            val enable = mutableClassDefBy(type).methods.singleOrNull {
                it.name == "enable" && it.returnType == "Z" && it.parameterTypes.isEmpty()
            } ?: throw PatchException("$type has no enable()Z")
            enable.addInstructions(
                0,
                """
                    const/4 v0, 0x0
                    return v0
                """,
            )
        }

        // p1 is the target tab tag.
        SwitchTabFingerprint.method.addInstructions(
            0,
            """
                invoke-static/range { p1 .. p1 }, $TAB_POLICY->redirectTab(Ljava/lang/String;)Ljava/lang/String;
                move-result-object p1
            """,
        )
    }
}
