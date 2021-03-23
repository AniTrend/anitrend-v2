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
import android.graphics.Color.DKGRAY
import android.graphics.drawable.PaintDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.core.widget.TextViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import co.anitrend.arch.extension.ext.getColorFromAttr
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.markdown.R
import co.anitrend.common.markdown.ui.animator.PushOnPressAnimator
import co.anitrend.core.android.themeStyle
import co.anitrend.domain.common.entity.contract.ISynopsis
import com.squareup.contour.ContourLayout

class MarkdownSynopsisWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ContourLayout(context, attrs), CustomView {

    private val textWidget = MarkdownTextWidget(context).apply {
        val textAppearance = context.themeStyle(R.attr.textAppearanceBody1)
        TextViewCompat.setTextAppearance(this, textAppearance)
    }

    init { onInit(context, attrs) }

    fun setSynopsis(synopsis: ISynopsis) {
        textWidget.setText(synopsis)
    }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        val color = context.getColorFromAttr(R.attr.cardColor)
        background = PaintDrawable(color).also { it.setCornerRadius(8f) }
        clipToOutline = true
        elevation = 16f.dip
        stateListAnimator = PushOnPressAnimator(this)
        updatePadding(left = 16.dip, right = 16.dip)

        contourHeightOf { maxOf(textWidget.bottom() + 24.ydip, textWidget.bottom() + 16.ydip) }
        textWidget.layoutBy(
            x = leftTo { 16.xdip }.rightTo { parent.right() },
            y = topTo {
                when {
                    isSelected -> parent.top() + 16.ydip
                    else -> textWidget.preferredHeight() / 2
                }
            }
        )

        val collapsedLines = textWidget.maxLines
        setOnClickListener {
            TransitionManager.beginDelayedTransition(parent as ViewGroup, AutoTransition()
                .setInterpolator(FastOutSlowInInterpolator())
                .setDuration(300)
            )

            isSelected = !isSelected
            textWidget.maxLines = if (isSelected) Int.MAX_VALUE else collapsedLines
        }
    }
}