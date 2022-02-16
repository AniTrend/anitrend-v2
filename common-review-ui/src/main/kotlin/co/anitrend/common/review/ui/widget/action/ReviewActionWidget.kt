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

package co.anitrend.common.review.ui.widget.action

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Space
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.marginLeft
import androidx.core.view.updateLayoutParams
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.updateMargins
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.review.R
import co.anitrend.common.review.ui.widget.action.presenter.ReviewActionPresenter
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.android.widget.button.LoadingTextButton
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewRating

internal class ReviewActionWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), CustomView {

    private val upVote = LoadingTextButton(context, icon = R.drawable.ic_thumb_up)

    private val downVote = LoadingTextButton(context, icon = R.drawable.ic_thumb_down)

    private val delete = LoadingTextButton(context, icon = R.drawable.ic_delete)

    private val edit = LoadingTextButton(context, icon = R.drawable.ic_edit)

    init { onInit(context, attrs, defStyleAttr) }

    private fun setUpIconAndColours(presenter: ReviewActionPresenter) {
        when (presenter.controller.entity.userRating) {
            ReviewRating.DOWN_VOTE -> {
                downVote.setDrawable(
                    context.getCompatDrawable(
                        R.drawable.ic_thumb_down,
                        R.color.orange_A700
                    )
                )
                upVote.setDrawable(
                    context.getCompatDrawable(
                        R.drawable.ic_thumb_up
                    )
                )
            }
            ReviewRating.UP_VOTE -> {
                upVote.setDrawable(
                    context.getCompatDrawable(
                        R.drawable.ic_thumb_up,
                        R.color.green_A700
                    )
                )
                downVote.setDrawable(
                    context.getCompatDrawable(
                        R.drawable.ic_thumb_down
                    )
                )
            }
            ReviewRating.NO_VOTE -> {
                downVote.setDrawable(
                    context.getCompatDrawable(
                        R.drawable.ic_thumb_down
                    )
                )
                upVote.setDrawable(
                    context.getCompatDrawable(
                        R.drawable.ic_thumb_up
                    )
                )
            }
        }
    }

    private fun setUpClickHandlers(presenter: ReviewActionPresenter) {
        if (!presenter.isCurrentUser()) {
            with (upVote) {
                setOnClickListener {
                    when (presenter.controller.entity.userRating) {
                        ReviewRating.UP_VOTE -> presenter.rateReview(ReviewRating.NO_VOTE)
                        else -> presenter.rateReview(ReviewRating.UP_VOTE)
                    }
                    showLoading()
                }
            }
            with (downVote) {
                setOnClickListener {
                    when (presenter.controller.entity.userRating) {
                        ReviewRating.DOWN_VOTE -> presenter.rateReview(ReviewRating.NO_VOTE)
                        else -> presenter.rateReview(ReviewRating.DOWN_VOTE)
                    }
                    showLoading()
                }
            }
            delete.gone()
            edit.gone()
        } else {
            with (delete) {
                setOnClickListener {
                    presenter.deleteReview()
                    showLoading()
                }
                visible()
            }
            with (edit) {
                setOnClickListener {
                    // TODO: Open review editor screen
                }
                visible()
            }
        }
    }

    fun updateUsing(review: Review, settings: Settings) {
        val presenter = ReviewActionPresenter(context, settings, review)
        upVote.setText(presenter.controller.upVoteCount())
        downVote.setText(presenter.controller.downVoteCount())
        setUpIconAndColours(presenter)
        setUpClickHandlers(presenter)
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        val layoutParameters = LayoutParams(24.dp, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        addView(edit)
        addView(
            Space(context).also { space ->
                space.layoutParams = layoutParameters
            }
        )
        addView(delete)
        addView(
            Space(context).also { space ->
                space.layoutParams = layoutParameters
            }
        )
        addView(upVote)
        addView(
            Space(context).also { space ->
                space.layoutParams = layoutParameters
            }
        )
        addView(downVote)

        if (isInEditMode) {
            upVote.setText("10")
            downVote.setText("1")
            delete.visible()
            edit.visible()
        } else {
            delete.gone()
            edit.gone()
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
        upVote.onViewRecycled()
        downVote.onViewRecycled()
        delete.onViewRecycled()
        edit.onViewRecycled()
    }
}