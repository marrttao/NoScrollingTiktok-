package app.unscroll.patches.tiktok.shared

/** Global TikTok package. Unscroll keeps the original package ID (decision T3). */
internal const val TIKTOK_PACKAGE = "com.zhiliaoapp.musically"

/**
 * TikTok versions whose bytecode every Unscroll patch has been checked against.
 * Patches refuse other versions instead of half-applying (fail-closed).
 */
internal val SUPPORTED_TIKTOK_VERSIONS = setOf("46.2.3")

internal val TIKTOK_COMPATIBILITY = TIKTOK_PACKAGE to SUPPORTED_TIKTOK_VERSIONS

internal const val EXTENSION_PACKAGE = "Lapp/unscroll/extension/tiktok"
