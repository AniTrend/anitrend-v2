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

package co.anitrend.common.medialist.ui.widget.date

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Space
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.medialist.ui.R
import co.anitrend.common.medialist.ui.widget.date.controller.FuzzyDateController
import co.anitrend.common.medialist.ui.widget.date.presenter.FuzzyDatePresenter
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.helpers.date.AniTrendDateHelper
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.entity.shared.FuzzyDate.Companion.orEmpty
import com.airbnb.paris.extensions.style
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FuzzyDateWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), CustomView, KoinComponent {

    private val thumbnailImageView = AppCompatImageView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        setImageDrawable(
            context.getCompatDrawable(
                R.drawable.ic_edit_calendar,
                R.color.colorAccent
            )
        )
    }

    private val fuzzyDateTextView = MaterialTextView(context).apply {
        style(R.style.Widget_MaterialComponents_TextView)
        TextViewCompat.setTextAppearance(
            this,
            R.style.TextAppearance_MaterialComponents_Subtitle1
        )
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).also {
            it.gravity = Gravity.CENTER_VERTICAL
        }
    }

    private val dateHelper by inject<AniTrendDateHelper>()

    private val controller by lazy (UNSAFE) {
        FuzzyDateController(dateHelper)
    }

    private val presenter by lazy (UNSAFE) {
        FuzzyDatePresenter(
            fuzzyDateTextView,
            controller,
            dateHelper
        )
    }

    val dateChangeFlow: Flow<FuzzyDate> = presenter.model

    init { onInit(context, attrs, defStyleAttr) }

    fun setUpUsing(fuzzyDate: FuzzyDate?) {
        presenter.updateFuzzyDate(fuzzyDate.orEmpty())
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        setPadding(8.dp)
        addView(thumbnailImageView)
        addView(
            Space(context).apply {
                this.layoutParams = LayoutParams(
                    8.dp, LayoutParams.WRAP_CONTENT
                )
            }
        )
        addView(fuzzyDateTextView)

        background = context.getCompatDrawable(
            R.drawable.outline_button_frame
        )

        setOnClickListener(presenter::createDatePicker)

        if (isInEditMode)
            fuzzyDateTextView.text = "---"
    }
}