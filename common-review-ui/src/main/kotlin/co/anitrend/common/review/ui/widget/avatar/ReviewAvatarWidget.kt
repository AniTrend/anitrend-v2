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

package co.anitrend.common.review.ui.widget.avatar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Space
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.helpers.image.using
import co.anitrend.domain.review.entity.Review
import coil.request.Disposable
import coil.transform.CircleCropTransformation
import com.google.android.material.textview.MaterialTextView

internal class ReviewAvatarWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(
    context, attrs, defStyleAttr
), CustomView {

    private val userImage = AppCompatImageView(context).apply {
        layoutParams = LayoutParams(18.dp, 18.dp).also {
            it.gravity = Gravity.CENTER_VERTICAL
        }
    }

    private val spacing = Space(context).apply {
        layoutParams = LayoutParams(8.dp, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private val userName = MaterialTextView(context).apply {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).also {
            it.gravity = Gravity.CENTER_VERTICAL
        }
        setTextColor(
            context.getCompatColor(co.anitrend.core.android.R.color.white_1000)
        )
    }

    private var disposable: Disposable? = null

    init {
        onInit(context, attrs, defStyleAttr)
    }

    fun updateUsing(review: Review) {
        userName.text = review.user.name
        disposable = userImage.using(
            review.user.avatar,
            listOf(CircleCropTransformation())
        )
    }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        background = context.getCompatDrawable(co.anitrend.core.android.R.drawable.bubble_background)
        addView(userImage)
        addView(spacing)
        addView(userName)
        if (isInEditMode) {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            userImage.setImageDrawable(
                context.getCompatDrawable(co.anitrend.core.R.drawable.ic_anitrend_notification_logo)
            )
            userName.text = "Groenboys"
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        disposable?.dispose()
        disposable = null
    }

    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }
}
