/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.common.media.ui.widget.progress

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.ViewCompat
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.updateMargins
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.core.android.extensions.dp
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView

@SuppressLint("SetTextI18n")
internal class MediaProgressWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(
    context, attrs, defStyleAttr
), CustomView {

    private val progressText = MaterialTextView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).also { params ->
            params.gravity = Gravity.CENTER_VERTICAL
        }
        setTextColor(context.getCompatColor(R.color.white_1000))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        setTypeface(typeface, Typeface.BOLD)
    }

    private val incrementIndicator = AppCompatImageView(context).apply {
        layoutParams = LayoutParams(14.dp, 14.dp).also { params ->
            params.gravity = Gravity.CENTER_VERTICAL
        }
        setImageDrawable(
            this.context.getCompatDrawable(
                R.drawable.ic_add,
                R.color.white_1000
            )
        )
    }

    private val progressSpinner = CircularProgressIndicator(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).also { params ->
            params.gravity = Gravity.CENTER_VERTICAL
        }
        indicatorSize = 10.dp
        trackCornerRadius = 8.dp
        trackThickness = 2.dp
        isIndeterminate = false
    }

    init { onInit(context, attrs, defStyleAttr) }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        addView(progressSpinner)
        addView(progressText)
        addView(incrementIndicator)

        //if (isInEditMode) {
            progressText.text = "5 / 25"
            progressSpinner.max = 25
            progressSpinner.progress = 5
            background = context.getCompatDrawable(R.drawable.widget_background)
        //}

        progressText.updateMargins(start = 8.dp)
        incrementIndicator.updateMargins(start = 8.dp)
    }
}