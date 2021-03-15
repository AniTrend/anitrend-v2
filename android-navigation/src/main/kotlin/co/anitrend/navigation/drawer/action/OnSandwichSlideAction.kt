package co.anitrend.navigation.drawer.action

import androidx.annotation.FloatRange
import co.anitrend.navigation.drawer.component.content.BottomDrawerContent

/**
 * Callback used to run actions when the offset/progress of [BottomDrawerContent]'s account
 * picker sandwich animation changes.
 */
internal interface OnSandwichSlideAction {

    /**
     * Called when the sandwich animation is running, either opening or closing the account picker.
     * [slideOffset] is a value between 0 and 1. 0 represents the closed, default state with the
     * account picker not visible. 1 represents the open state with the account picker visible.
     */
    fun onSlide(
        @FloatRange(
            from = 0.0,
            fromInclusive = true,
            to = 1.0,
            toInclusive = true
        ) slideOffset: Float
    )
}