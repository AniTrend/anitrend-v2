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

package co.anitrend.core.android.components.action

import android.content.Context
import android.util.AttributeSet
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.MenuRes
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.core.android.components.action.menu.IFloatingActionMenu
import co.anitrend.core.android.databinding.ActionFrameLayoutBinding
import co.anitrend.core.android.views.FrameLayoutWithBinding

class FloatingActionMenu @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayoutWithBinding<ActionFrameLayoutBinding>(context, attrs, defStyleAttr),
    CustomView, IFloatingActionMenu {

    init {
        onInit(context, attrs, defStyleAttr)
    }

    override fun onViewRecycled() {
        dismiss()
        binding?.actionMenuView?.setOnMenuItemClickListener(null)
        super<FrameLayoutWithBinding>.onViewRecycled()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onViewRecycled()
    }

    override fun findMenuItem(id: Int): MenuItem? {
        return binding?.actionMenuView?.menu?.findItem(id)
    }

    override fun inflate(
        menuInflater: MenuInflater?,
        @MenuRes menuRes: Int,
        clickListener: (item: MenuItem?) -> Boolean
    ) {
        val actionMenuView = binding?.actionMenuView
        if (actionMenuView?.menu?.size() == 0) {
            menuInflater?.inflate(menuRes, actionMenuView.menu)
            actionMenuView.setOnMenuItemClickListener {
                clickListener(it)
            }
        }
    }

    override fun show() {
        visible()
    }

    override fun dismiss() {
        gone()
    }

    override fun createBinding() =
        ActionFrameLayoutBinding.inflate(
            getLayoutInflater(),
            this,
            true
        )
}