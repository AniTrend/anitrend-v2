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

package co.anitrend.core.android.widget.button

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import androidx.appcompat.widget.LinearLayoutCompat
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.core.android.R
import co.anitrend.core.android.extensions.dp
import com.airbnb.paris.extensions.style
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView

/**
 * A loading text button that supports icons
 *
 * @param icon Optional icon for this text button
 */
class LoadingTextButton @JvmOverloads constructor(
    context: Context,
    icon: Int? = null,
    attrs: AttributeSet? = null
) : ViewFlipper(context, attrs), CustomView {

    private val indicator = CircularProgressIndicator(context).apply {
        layoutParams = LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        ).also { params ->
            params.gravity = Gravity.CENTER_VERTICAL
            params.marginEnd = 8.dp
        }
        indicatorSize = 16.dp
        trackCornerRadius = 8.dp
        trackThickness = 4.dp
        isIndeterminate = true
    }

    private val primaryText = MaterialTextView(context).apply {
        applyDefaultTextStyle {
            setCompoundDrawablesWithIntrinsicBounds(
                icon?.let { context.getCompatDrawable(it) },
                null, null, null
            )
        }
    }

    init { onInit(context, attrs) }

    /**
     * @see MaterialTextView.setText
     */
    fun setText(text: CharSequence?) {
        primaryText.text = text
        showContent()
    }

    /**
     * @see MaterialTextView.setCompoundDrawables
     */
    fun setDrawable(drawable: Drawable?) {
        primaryText.setCompoundDrawablesWithIntrinsicBounds(
            drawable, null, null, null
        )
        showContent()
    }

    fun showLoading() {
        if (displayedChild != LOADING_INDEX)
            displayedChild = LOADING_INDEX
    }

    fun showContent() {
        if (displayedChild != CONTENT_INDEX)
            displayedChild = CONTENT_INDEX
    }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        inAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        outAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        layoutParams = LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
        addView(primaryText)
        addView(indicator)

        if (isInEditMode)
            displayedChild = CONTENT_INDEX
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        displayedChild = CONTENT_INDEX
        setOnClickListener(null)
    }

    private companion object {
        const val CONTENT_INDEX = 0
        const val LOADING_INDEX = 1

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