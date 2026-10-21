package app.unscroll.patches.tiktok.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Global TikTok package. Unscroll keeps the original package ID (decision T3). */
internal const val TIKTOK_PACKAGE = "com.zhiliaoapp.musically"

/**
 * TikTok versions whose bytecode every Unscroll patch has been checked against.
 * Patches refuse other versions instead of half-applying (fail-closed).
 */
internal val SUPPORTED_TIKTOK_VERSIONS = listOf("46.2.3")

internal val TIKTOK_COMPATIBILITY = Compatibility(
    name = "TikTok",
    packageName = TIKTOK_PACKAGE,
    appIconColor = 0xFE2C55,
    targets = SUPPORTED_TIKTOK_VERSIONS.map { AppTarget(it) },
)

internal const val EXTENSION_PACKAGE = "Lapp/unscroll/extension/tiktok"
