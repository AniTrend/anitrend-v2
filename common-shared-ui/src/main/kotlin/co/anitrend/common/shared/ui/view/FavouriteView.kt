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
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.updateMargins
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.shared.R
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.extensions.toHumanReadableQuantity
import co.anitrend.domain.common.entity.contract.IFavourable
import com.google.android.material.textview.MaterialTextView

class FavouriteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), CustomView {

    private val favouriteImageView = AppCompatImageView(context).apply {
        layoutParams = LayoutParams(14.dp, 14.dp).also { param ->
            param.gravity = Gravity.CENTER_VERTICAL
        }
    }

    private val favouriteTextView = MaterialTextView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).also { param ->
            param.gravity = Gravity.CENTER_VERTICAL
        }
        setTextColor(context.getCompatColor(R.color.white_1000))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        setTypeface(typeface, Typeface.BOLD)
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

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        addView(favouriteImageView)
        addView(favouriteTextView)

        background = context.getCompatDrawable(
            R.drawable.widget_background,
            R.color.bubble_color
        )

        favouriteTextView.updateMargins(start = 8.dp)

        if (isInEditMode)
            setFavouriteState(
                object : IFavourable {
                    override val favourites: Int = 11650
                    override val isFavourite: Boolean = false
                    override val isFavouriteBlocked: Boolean = false
                }
            )
    }
}