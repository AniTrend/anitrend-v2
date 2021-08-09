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

package co.anitrend.common.medialist.ui.widget.score

import android.content.Context
import android.text.Editable
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import co.anitrend.common.medialist.ui.widget.contract.AbstractEditWidget
import co.anitrend.common.medialist.ui.widget.score.controller.ScoreEditController
import co.anitrend.common.medialist.ui.widget.score.model.ScoreEditModel
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.medialist.enums.ScoreFormat
import java.text.DecimalFormatSymbols

class ScoreEditWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AbstractEditWidget(context, attrs, defStyleAttr) {

    private lateinit var controller: ScoreEditController

    init { onInit(context, attrs, defStyleAttr) }

    private fun decorateCurrentWidget(settings: IUserSettings) {
        if (settings.scoreFormat.value == ScoreFormat.POINT_10_DECIMAL) {
            val separator: Char = DecimalFormatSymbols.getInstance().decimalSeparator
            val digitsKeyListener = DigitsKeyListener.getInstance("0123456789${separator}")
            currentWidget.keyListener = digitsKeyListener
        }
    }

    private fun onChange(controller: ScoreEditController) {
        maximumWidget.text = controller.maximumFormatted()
        currentWidget.setText(controller.currentFormatted())
    }

    fun setUpUsing(scoreEditModel: ScoreEditModel, settings: IUserSettings) {
        controller = ScoreEditController(scoreEditModel, settings)
        decorateCurrentWidget(settings)
        incrementWidget.setOnClickListener {
            controller.incrementScore()
            onChange(controller)
        }
        decrementWidget.setOnClickListener {
            controller.decrementScore()
            onChange(controller)
        }
        onChange(controller)
    }

    /**
     * Called after [currentWidget] text has been changed
     */
    override fun onCurrentTextChanged(number: String) {
        controller.changeScore(number)
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        super.onInit(context, attrs, styleAttr)
        if (isInEditMode) {
            maximumWidget.text = "${10f}"
            currentWidget.setText("${6.5f}")
        }
    }
}