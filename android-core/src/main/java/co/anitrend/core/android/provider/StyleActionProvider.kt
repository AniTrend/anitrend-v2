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

package co.anitrend.core.android.provider

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ActionProvider
import androidx.core.view.setPadding
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.getDrawableAttr
import co.anitrend.core.android.R
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.extensions.lifecycleScope
import co.anitrend.core.android.provider.contract.AbstractActionProvider
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StyleActionProvider(context: Context) : AbstractActionProvider(context), KoinComponent {

    private val actionImageView = AppCompatImageView(context).apply {
        background = context.getDrawableAttr(
            R.attr.selectableItemBackgroundBorderless
        )
        isClickable = true
        isFocusable = true
        setPadding(2.dp)
        setOnClickListener {
            onActionClicked()
        }
    }

    private val container = FrameLayout(context).apply {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT,
            ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )
        setPadding(10.dp)
    }

    private val settings by inject<ICustomizationSettings>()

    private val viewModes = PreferredViewMode.values()

    private fun onActionClicked() {
        val current = settings.preferredViewMode.value
        val currentIndex = viewModes.indexOf(current)
        settings.preferredViewMode.value = when (currentIndex) {
            PreferredViewMode.DETAILED.ordinal -> PreferredViewMode.SUMMARY
            PreferredViewMode.SUMMARY.ordinal -> PreferredViewMode.COMPACT
            PreferredViewMode.COMPACT.ordinal -> PreferredViewMode.COMFORTABLE
            else -> PreferredViewMode.DETAILED
        }
    }

    private fun iconForSetting(preferredViewMode: PreferredViewMode) {
        val resource = when (preferredViewMode) {
            PreferredViewMode.DETAILED -> R.drawable.ic_view_detailed
            PreferredViewMode.SUMMARY -> R.drawable.ic_view_summary
            PreferredViewMode.COMPACT -> R.drawable.ic_view_compact
            else -> R.drawable.ic_view_comfortable
        }
        val drawable = context.getCompatDrawable(resource)
        actionImageView.setImageDrawable(drawable)
    }

    /**
     * Factory for creating the [ActionProvider] view
     *
     * @param forItem Optional menu item to create view for
     */
    override fun createWidget(forItem: MenuItem?): View {
        context.lifecycleScope()?.launchWhenResumed {
            iconForSetting(settings.preferredViewMode.value)
            container.addView(actionImageView)
            settings.preferredViewMode.flow.collect(::iconForSetting)
        }
        return container
    }

    /**
     * Factory method called by the Android framework to create new action views.
     * This method returns a new action view for the given MenuItem.
     *
     *
     * If your ActionProvider implementation overrides the deprecated no-argument overload
     * [onCreateActionView], overriding this method for devices running API 16 or later
     * is recommended but optional. The default implementation calls [onCreateActionView]
     * for compatibility with applications written for older platform versions.
     *
     * @param forItem MenuItem to create the action view for
     * @return the new action view
     */
    override fun onCreateActionView(forItem: MenuItem) = createWidget(forItem)

    /**
     * Factory method for creating new action views.
     *
     * @return A new action view.
     */
    override fun onCreateActionView() = createWidget()
}