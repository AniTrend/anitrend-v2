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

package co.anitrend.common.review.ui.widget.vote

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.appcompat.widget.LinearLayoutCompat
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.review.R
import co.anitrend.common.review.ui.widget.vote.presenter.ReviewVotePresenter
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.settings.Settings
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewRating
import com.airbnb.paris.extensions.style
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView

internal class ReviewVoteWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(
    context, attrs, defStyleAttr
), CustomView {

    private val progressIndicator = CircularProgressIndicator(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).also { params ->
            params.gravity = Gravity.CENTER_VERTICAL
            params.marginEnd = 8.dp
        }
        indicatorSize = 16.dp
        trackCornerRadius = 8.dp
        trackThickness = 4.dp
        isIndeterminate = true
    }

    private val upVoteCount = MaterialTextView(context).apply {
        applyDefaultTextStyle {
            setCompoundDrawablesWithIntrinsicBounds(
                context.getCompatDrawable(R.drawable.ic_thumb_up),
                null, null, null
            )
        }
    }

    private val downVoteCount = MaterialTextView(context).apply {
        applyDefaultTextStyle {
            setCompoundDrawablesWithIntrinsicBounds(
                context.getCompatDrawable(R.drawable.ic_thumb_down),
                null, null, null
            )
        }
    }

    private val spacing = Space(context).apply {
        layoutParams = LayoutParams(24.dp, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    init { onInit(context, attrs, defStyleAttr) }

    private fun setUpIconAndColours(presenter: ReviewVotePresenter) {
        when (presenter.controller.entity.userRating) {
            ReviewRating.DOWN_VOTE -> {
                downVoteCount.setCompoundDrawablesWithIntrinsicBounds(
                    context.getCompatDrawable(
                        R.drawable.ic_thumb_down,
                        R.color.orange_A700
                    ),
                    null, null, null
                )
                upVoteCount.setCompoundDrawablesWithIntrinsicBounds(
                    context.getCompatDrawable(R.drawable.ic_thumb_up),
                    null, null, null
                )
            }
            ReviewRating.UP_VOTE -> {
                upVoteCount.setCompoundDrawablesWithIntrinsicBounds(
                    context.getCompatDrawable(
                        R.drawable.ic_thumb_up,
                        R.color.green_A700
                    ),
                    null, null, null
                )
                downVoteCount.setCompoundDrawablesWithIntrinsicBounds(
                    context.getCompatDrawable(R.drawable.ic_thumb_down),
                    null, null, null
                )
            }
            ReviewRating.NO_VOTE -> {
                downVoteCount.setCompoundDrawablesWithIntrinsicBounds(
                    context.getCompatDrawable(R.drawable.ic_thumb_down),
                    null, null, null
                )
                upVoteCount.setCompoundDrawablesWithIntrinsicBounds(
                    context.getCompatDrawable(R.drawable.ic_thumb_up),
                    null, null, null
                )
            }
        }
        //TODO have some sort of general field to identify if something belongs to this user
    }

    private fun isLoading() = progressIndicator.visibility == View.VISIBLE

    private fun setUpClickHandlers(presenter: ReviewVotePresenter) {
        upVoteCount.setOnClickListener {
            if (!isLoading()) {
                progressIndicator.visible()
                if (presenter.controller.entity.userRating == ReviewRating.UP_VOTE)
                    presenter.rateReview(ReviewRating.NO_VOTE)
                else presenter.rateReview(ReviewRating.UP_VOTE)
            }
        }
        downVoteCount.setOnClickListener {
            if (!isLoading()) {
                progressIndicator.visible()
                if (presenter.controller.entity.userRating == ReviewRating.DOWN_VOTE)
                    presenter.rateReview(ReviewRating.NO_VOTE)
                else presenter.rateReview(ReviewRating.DOWN_VOTE)
            }
        }
    }

    fun updateUsing(review: Review, settings: Settings) {
        val presenter = ReviewVotePresenter(context, settings, review)
        upVoteCount.text = presenter.controller.upVoteCount()
        downVoteCount.text = presenter.controller.downVoteCount()
        setUpIconAndColours(presenter)
        setUpClickHandlers(presenter)
        progressIndicator.gone()
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        orientation = HORIZONTAL
        addView(progressIndicator)
        addView(upVoteCount)
        addView(spacing)
        addView(downVoteCount)

        if (isInEditMode) {
            upVoteCount.text = "10"
            downVoteCount.text = "1"
        }
    }

    /**
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider calling this in [android.view.View.onDetachedFromWindow]
     */
    override fun onViewRecycled() {
        super.onViewRecycled()
        upVoteCount.setOnClickListener(null)
        downVoteCount.setOnClickListener(null)
    }

    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }

    private companion object {
        fun MaterialTextView.applyDefaultTextStyle(
            unit: MaterialTextView.() -> Unit
        ) {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).also { param ->
                param.gravity = Gravity.CENTER_VERTICAL
            }
            compoundDrawablePadding = 8.dp
            style(R.style.AppTheme_Material_TextBody_Primary)
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(context.getCompatColor(R.color.white_1000))
            unit()
        }
    }
}