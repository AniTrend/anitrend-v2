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

package co.anitrend.common.review.ui.widget.rating

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RatingBar
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.view.setPadding
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.mutate
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.review.R
import co.anitrend.core.android.extensions.dp
import co.anitrend.domain.review.entity.Review
import com.airbnb.paris.extensions.style

internal class ReviewRatingWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr
), CustomView {

    private val ratingBar = AppCompatRatingBar(
        context,
        null,
        android.R.attr.ratingBarStyleSmall
    ).apply {
        max = 100
        setIsIndicator(true)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        progressDrawable?.setTint(
            context.getCompatColor(R.color.white_1000)
        )
    }

    init {
        onInit(context, attrs, defStyleAttr)
    }

    fun updateUsing(review: Review) {
        ratingBar.progress = review.score
    }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        background = context.getCompatDrawable(R.drawable.bubble_background)
        setPadding(8.dp)
        addView(ratingBar)
        if (isInEditMode) {
            ratingBar.progress = 70
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}