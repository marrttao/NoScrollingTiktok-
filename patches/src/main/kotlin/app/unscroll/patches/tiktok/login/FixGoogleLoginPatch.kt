/*
 * Adapted from ReVanced Patches (GPL-3.0),
 * https://gitlab.com/ReVanced/revanced-patches/-/blob/main/patches/src/main/kotlin/app/revanced/patches/tiktok/misc/login/fixgoogle/
 * via Metra TikTok Patches.
 */
package app.unscroll.patches.tiktok.login

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.unscroll.patches.tiktok.shared.TIKTOK_COMPATIBILITY
import com.android.tools.smali.dexlib2.AccessFlags

/** Whether TikTok may use the native Google Play services sign-in. */
internal object GoogleAuthAvailableFingerprint : Fingerprint(
    definingClass = "Lcom/bytedance/lobby/google/GoogleAuth;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    parameters = listOf(),
)

/**
 * Native Google sign-in checks the app's signing certificate, which changes when the
 * APK is re-signed. Reporting native sign-in as unavailable makes TikTok fall back to
 * its browser-based Google sign-in. No signature is spoofed.
 */
@Suppress("unused")
val fixGoogleLoginPatch = bytecodePatch(
    name = "Fix Google login",
    description = "Uses TikTok's browser-based Google sign-in, which works with a re-signed app.",
) {
    compatibleWith(TIKTOK_COMPATIBILITY)

    execute {
        GoogleAuthAvailableFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """,
        )
    }
}
