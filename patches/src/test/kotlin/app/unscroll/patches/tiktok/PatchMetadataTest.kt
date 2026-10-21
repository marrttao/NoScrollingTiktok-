package app.unscroll.patches.tiktok

import app.unscroll.patches.tiktok.feed.blockFeedEndpointsPatch
import app.unscroll.patches.tiktok.login.fixGoogleLoginPatch
import app.unscroll.patches.tiktok.navigation.restrictNavigationPatch
import app.unscroll.patches.tiktok.shared.SUPPORTED_TIKTOK_VERSIONS
import app.unscroll.patches.tiktok.shared.TIKTOK_PACKAGE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class PatchMetadataTest {
    private val unscrollPatches = listOf(
        blockFeedEndpointsPatch,
        restrictNavigationPatch,
        fixGoogleLoginPatch,
    )

    @Test
    fun `every patch is pinned to the supported TikTok versions only`() {
        unscrollPatches.forEach { patch ->
            val compatibility = requireNotNull(patch.compatibility) { "${patch.name}" }.single()
            assertEquals(TIKTOK_PACKAGE, compatibility.packageName, patch.name)
            assertEquals(SUPPORTED_TIKTOK_VERSIONS, compatibility.targets.map { it.version }, patch.name)
        }
    }

    @Test
    fun `every patch is enabled by default`() {
        unscrollPatches.forEach { assertTrue(it.default, it.name) }
    }

    @Test
    fun `the patch script applies every patch`() {
        val script = generateSequence(File("").absoluteFile) { it.parentFile }
            .map { File(it, "scripts/unscroll-patch.sh") }
            .first { it.isFile }
            .readText()
        unscrollPatches.forEach { patch ->
            assertTrue(script.contains("\"${patch.name}\""), "scripts/unscroll-patch.sh misses ${patch.name}")
        }
    }
}
