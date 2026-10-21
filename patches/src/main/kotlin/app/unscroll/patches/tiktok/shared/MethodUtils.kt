package app.unscroll.patches.tiktok.shared

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

/** Indexes of every `return-object` in [method], highest first so insertions keep earlier indexes valid. */
internal fun MutableMethod.returnObjectIndicesDescending(): List<Int> {
    val instructions = implementation?.instructions
        ?: throw PatchException("$definingClass->$name has no implementation")
    val indices = instructions.withIndex()
        .filter { it.value.opcode == Opcode.RETURN_OBJECT }
        .map { it.index }
    if (indices.isEmpty()) throw PatchException("$definingClass->$name has no object return")
    return indices.asReversed()
}

/**
 * Inserts [smaliTemplate] right before every `return-object` of [this].
 * `{reg}` in the template is replaced with the register being returned.
 */
internal fun MutableMethod.beforeEveryObjectReturn(smaliTemplate: String) {
    returnObjectIndicesDescending().forEach { index ->
        val register = getInstruction<OneRegisterInstruction>(index).registerA
        addInstructions(index, smaliTemplate.replace("{reg}", "v$register"))
    }
}

/** Makes every `return-object` of [this] return a new, empty, mutable list. */
internal fun MutableMethod.returnEmptyMutableList() = beforeEveryObjectReturn(
    """
        new-instance {reg}, Ljava/util/ArrayList;
        invoke-direct/range { {reg} .. {reg} }, Ljava/util/ArrayList;-><init>()V
    """,
)
