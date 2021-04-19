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

package co.anitrend.common.shared.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Space
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.TextViewCompat
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.shared.R
import co.anitrend.core.android.extensions.toHumanReadableQuantity
import co.anitrend.core.android.themeStyle
import co.anitrend.domain.common.entity.contract.IFavourable
import com.google.android.material.textview.MaterialTextView

class FavouriteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), CustomView {

    private val favouriteImageView = AppCompatImageView(context).apply {
        val size = context.resources.getDimensionPixelSize(R.dimen.size_16dp)
        layoutParams = ViewGroup.LayoutParams(size, size)
        gravity = Gravity.CENTER_VERTICAL
    }

    private val space = Space(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            context.resources.getDimensionPixelSize(R.dimen.md_margin),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private val favouriteTextView = MaterialTextView(context).apply {
        val textAppearance = context.themeStyle(R.attr.textAppearanceHeadline6)
        TextViewCompat.setTextAppearance(this, textAppearance)
        setTextColor(context.getCompatColor(R.color.white_1000))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        gravity = Gravity.CENTER_VERTICAL
    }

    init { onInit(context, attrs, defStyleAttr) }

    /**
     * Set the number of favourites
     */
    fun setFavouriteState(favourite: IFavourable) {
        favouriteTextView.text = favourite.favourites.toHumanReadableQuantity()
        val drawableType = if (favourite.isFavourite)
            R.drawable.ic_favorite
        else R.drawable.ic_not_favorite
        favouriteImageView.setImageDrawable(
            context.getCompatDrawable(drawableType)
        )
    }

    private fun addChildrenItems() {
        addView(favouriteImageView)
        addView(space)
        addView(favouriteTextView)
    }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        layoutParams = LayoutParams(context, attrs)
        background = context.getCompatDrawable(R.drawable.widget_background, R.color.bubble_color)
        val padding = context.resources.getDimensionPixelSize(R.dimen.md_margin)
        setPadding(padding, padding, padding, padding)
        addChildrenItems()
        if (isInEditMode)
            setFavouriteState(
                object : IFavourable {
                    override val favourites: Int = 1650
                    override val isFavourite: Boolean = false
                    override val isFavouriteBlocked: Boolean = false
                }
            )
    }
}