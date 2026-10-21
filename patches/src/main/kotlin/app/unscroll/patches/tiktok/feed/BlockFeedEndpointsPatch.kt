package app.unscroll.patches.tiktok.feed

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.unscroll.patches.tiktok.shared.EXTENSION_PACKAGE
import app.unscroll.patches.tiktok.shared.TIKTOK_COMPATIBILITY
import app.unscroll.patches.tiktok.shared.unscrollExtensionPatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

/** TikTok's HTTP request model (Bytedance's Retrofit fork). Not obfuscated. */
internal const val RETROFIT_REQUEST = "Lcom/bytedance/retrofit2/client/Request;"

private const val FEED_ENDPOINT_POLICY = "$EXTENSION_PACKAGE/feed/FeedEndpointPolicy;"

/**
 * Network layer of decisions P1, P8, P9 and P11: every Retrofit request URL is passed
 * through FeedEndpointPolicy before it is stored, so requests to feed endpoints never
 * reach TikTok's servers.
 */
@Suppress("unused")
val blockFeedEndpointsPatch = bytecodePatch(
    name = "Block feed endpoints",
    description = "Stops the app from downloading video feeds (For You, Following, Friends, LIVE, search videos, hashtags, sounds).",
) {
    dependsOn(unscrollExtensionPatch)
    compatibleWith(TIKTOK_COMPATIBILITY)

    execute {
        val requestClass = mutableClassDefBy(RETROFIT_REQUEST)
        var hooked = 0

        requestClass.methods.filter { it.name == "<init>" }.forEach { constructor ->
            val instructions = constructor.implementation?.instructions?.toList() ?: return@forEach
            instructions.withIndex()
                .filter { (_, instruction) ->
                    instruction.opcode == Opcode.IPUT_OBJECT &&
                        ((instruction as ReferenceInstruction).reference as FieldReference).let {
                            it.definingClass == RETROFIT_REQUEST && it.name == "url" && it.type == "Ljava/lang/String;"
                        }
                }
                .map { it.index }
                .asReversed()
                .forEach { index ->
                    val register = (instructions[index] as TwoRegisterInstruction).registerA
                    constructor.addInstructions(
                        index,
                        """
                            invoke-static/range { v$register .. v$register }, $FEED_ENDPOINT_POLICY->filterUrl(Ljava/lang/String;)Ljava/lang/String;
                            move-result-object v$register
                        """,
                    )
                    hooked++
                }
        }

        if (hooked == 0) throw PatchException("No assignment of $RETROFIT_REQUEST->url found")
    }
}
