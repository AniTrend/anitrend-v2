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

package co.anitrend.common.shared.ui.view.text

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.shared.R
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.common.entity.contract.IEntityName
import com.google.android.material.textview.MaterialTextView

@SuppressLint("SetTextI18n")
class SharedNameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr), CustomView {

    init { onInit(context, attrs, defStyleAttr) }

    fun setEntityName(entity: IEntityName) {
        text = if (!entity.native.isNullOrBlank())
            "${entity.full} $CHARACTER_SEPARATOR ${entity.native}"
        else entity.full
    }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        if (isInEditMode) {
            setEntityName(
                object : IEntityName {
                    override val alternative: List<String> = emptyList()
                    override val alternativeSpoiler: List<String> = emptyList()
                    override val first: String = "Ruka"
                    override val full: String = "Ruka Sarashina"
                    override val last: String = "Sarashina"
                    override val middle: String? = null
                    override val native: String = "更科るか"
                }
            )
            setTextColor(context.getCompatColor(R.color.primaryTextColor))
        }
    }
}