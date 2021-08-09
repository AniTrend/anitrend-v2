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

package co.anitrend.common.medialist.ui.widget.date.presenter

import android.view.View
import co.anitrend.common.medialist.ui.R
import co.anitrend.common.medialist.ui.widget.date.controller.FuzzyDateController
import co.anitrend.core.android.extensions.fragmentManager
import co.anitrend.core.android.helpers.date.AniTrendDateHelper
import co.anitrend.domain.common.entity.shared.FuzzyDate
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class FuzzyDatePresenter(
    private val fuzzyDateTextView: MaterialTextView,
    private val controller: FuzzyDateController,
    private val dateHelper: AniTrendDateHelper
) {

    val model = MutableStateFlow(FuzzyDate.empty())

    fun updateFuzzyDate(fuzzyDate: FuzzyDate) {
        model.value = fuzzyDate
        fuzzyDateTextView.text = controller.asDateFormatted(
            "---", fuzzyDate
        )
    }

    fun createDatePicker(view: View) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.label_dialog_select_date)
            .setSelection(
                if (model.value.isDateNotSet())
                    MaterialDatePicker.todayInUtcMilliseconds()
                else dateHelper.convertToUnixTimeStamp(model.value)
            )
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val fuzzyDate = dateHelper.convertToFuzzyDate(it)
            updateFuzzyDate(fuzzyDate)
        }

        datePicker.addOnNegativeButtonClickListener {

        }

        datePicker.show(
            view.fragmentManager(),
            "FuzzyDatePicker"
        )
    }
}