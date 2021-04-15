/*
 * Copyright (C) 2021  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.common.markdown.ui.widget

import android.content.Context
import android.graphics.drawable.PaintDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.setPadding
import androidx.core.view.updatePadding
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import co.anitrend.arch.extension.ext.getColorFromAttr
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.markdown.R
import co.anitrend.common.markdown.ui.animator.PushOnPressAnimator
import co.anitrend.core.android.extensions.px
import co.anitrend.core.android.getCompatDrawable
import co.anitrend.core.android.getCompatDrawableAttr
import co.anitrend.core.android.themeStyle
import co.anitrend.domain.common.entity.contract.ISynopsis
import com.airbnb.paris.extensions.style
import com.squareup.contour.ContourLayout

class MarkdownSynopsisWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ContourLayout(context, attrs), CustomView {

    private val markdownTextWidget = MarkdownTextWidget(context).apply {
        style(R.style.AppTheme_Material_TextBody_Secondary)
        setTextColor(this.context.getColorFromAttr(R.attr.colorSecondaryText))
        setTextIsSelectable(false)
        includeFontPadding = false
    }

    private val expandImageView = AppCompatImageView(context).apply {
        val drawable = this.context.getCompatDrawable(R.drawable.ic_arrow_down)
        background = this.context.getCompatDrawableAttr(
            android.R.attr.selectableItemBackgroundBorderless
        )
        setPadding(16.px)
        isFocusable = true
        isClickable = true
        setImageDrawable(drawable)
    }

    init { onInit(context, attrs) }

    fun setSynopsis(synopsis: ISynopsis) {
        markdownTextWidget.setText(synopsis)
        markdownTextWidget.maxLines = collapsedLines
    }

    private fun onExpandToggle(isExpanded: Boolean): Boolean {
        markdownTextWidget.maxLines = if (isExpanded) Int.MAX_VALUE else collapsedLines
        if (isExpanded) {
            markdownTextWidget.setTextIsSelectable(true)
            markdownTextWidget.setTextColor(
                context.getColorFromAttr(R.attr.colorPrimaryText)
            )
            expandImageView.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_arrow_up)
            )
        } else {
            markdownTextWidget.setTextIsSelectable(false)
            markdownTextWidget.setTextColor(
                context.getColorFromAttr(R.attr.colorSecondaryText)
            )
            expandImageView.setImageDrawable(
                context.getCompatDrawable(R.drawable.ic_arrow_down)
            )
        }
        return isExpanded
    }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        val color = context.getColorFromAttr(R.attr.colorPrimaryVariant)
        background = PaintDrawable(color).also { it.setCornerRadius(16f) }
        clipToOutline = true
        elevation = 16f.dip
        stateListAnimator = PushOnPressAnimator(this)
        updatePadding(left = 16.dip, right = 16.dip)

        contourHeightOf {
            maxOf(
                markdownTextWidget.bottom() + 16.ydip,
                expandImageView.bottom() + 16.ydip
            )
        }

        // TODO: Need to figure out why max lines doesn't get applied unless I collapse the widget
        markdownTextWidget.layoutBy(
            x = leftTo { 16.xdip }.rightTo { parent.right() },
            y = topTo {
                when {
                    isSelected -> parent.top() + 16.ydip
                    else -> markdownTextWidget.preferredHeight() / 2
                }
            }
        )
        expandImageView.layoutBy(
            x = centerHorizontallyTo { parent.centerX() },
            y = topTo { markdownTextWidget.bottom() + 16.ydip }
        )

        expandImageView.setOnClickListener {
            TransitionManager.beginDelayedTransition(
                parent as ViewGroup,
                AutoTransition()
                    .setInterpolator(FastOutSlowInInterpolator())
                    .setDuration(300)
            )

            isSelected = onExpandToggle(!isSelected)
        }
    }

    private companion object {
        const val collapsedLines = 4
    }
}