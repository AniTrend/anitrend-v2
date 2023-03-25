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

package co.anitrend.common.medialist.ui.widget.counter

import android.content.Context
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import co.anitrend.common.medialist.ui.widget.contract.AbstractEditWidget
import co.anitrend.common.medialist.ui.widget.counter.controller.CounterEditController
import co.anitrend.common.medialist.ui.widget.counter.model.CounterEditModel

class CounterEditWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AbstractEditWidget(context, attrs, defStyleAttr) {

    private lateinit var controller: CounterEditController

    override val supportsMaximumWidget: Boolean = false

    init { onInit(context, attrs, defStyleAttr) }

    /**
     * Called after [currentWidget] text has been changed
     */
    override fun onCurrentTextChanged(number: String) {
        controller.changeValue(number)
    }

    private fun decorateCurrentWidget() {
        val digitsKeyListener = DigitsKeyListener.getInstance("0123456789")
        currentWidget.keyListener = digitsKeyListener
    }

    private fun onChanged(controller: CounterEditController) {
        val decoratedText = controller.currentFormatted()
        currentWidget.setText(decoratedText)
        currentWidget.setSelection(decoratedText.length)
    }

    fun setUpUsing(counterEditModel: CounterEditModel) {
        controller = CounterEditController(counterEditModel)
        incrementWidget.setOnClickListener {
            controller.incrementCount()
            onChanged(controller)
        }
        decrementWidget.setOnClickListener {
            controller.decrementCount()
            onChanged(controller)
        }
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
        decorateCurrentWidget()
        if (isInEditMode)
            setUpUsing(
                CounterEditModel(
                    current = 7
                )
            )
    }
}