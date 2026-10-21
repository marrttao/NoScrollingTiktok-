package app.unscroll.patches.tiktok.video

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.unscroll.patches.tiktok.shared.TIKTOK_COMPATIBILITY

/**
 * TikTok's vertical video pager. In 47.4.3 it is only inflated by the base video-feed
 * fragment layout, which backs both the Home feed and the single-video (detail) screen.
 */
internal const val VERTICAL_VIEW_PAGER = "Lcom/ss/android/ugc/aweme/common/widget/VerticalViewPager;"

/** Android overrides (never obfuscated) that let the pager react to a vertical swipe. */
private val SWIPE_ENTRY_POINTS = listOf(
    Triple("onInterceptTouchEvent", listOf("Landroid/view/MotionEvent;"), "Z"),
    Triple("onTouchEvent", listOf("Landroid/view/MotionEvent;"), "Z"),
    Triple("canScrollVertically", listOf("I"), "Z"),
)

/**
 * Paging layer of decision P3: a video screen shows exactly one video. The pager never
 * takes a vertical swipe, so the next video cannot be reached by swiping. Touches on the
 * video itself (pause, double-tap like, buttons) are handled by its child views and keep
 * working.
 */
@Suppress("unused")
val disableVideoSwipePatch = bytecodePatch(
    name = "Disable video swipe",
    description = "Removes vertical swiping between videos; every opened video stays a single video.",
) {
    compatibleWith(TIKTOK_COMPATIBILITY)

    execute {
        val pager = mutableClassDefBy(VERTICAL_VIEW_PAGER)
        SWIPE_ENTRY_POINTS.forEach { (name, parameters, returnType) ->
            val method = pager.methods.singleOrNull {
                it.name == name && it.returnType == returnType && it.parameterTypes.map(CharSequence::toString) == parameters
            } ?: throw PatchException("$VERTICAL_VIEW_PAGER has no $name")
            method.addInstructions(
                0,
                """
                    const/4 v0, 0x0
                    return v0
                """,
            )
        }
    }
}
