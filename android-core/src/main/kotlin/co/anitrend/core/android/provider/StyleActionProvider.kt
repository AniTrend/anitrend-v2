/*
 * Copyright (C) 2021 AniTrend
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.core.android.R
import co.anitrend.core.android.extensions.lifecycleOwner
import co.anitrend.core.android.extensions.lifecycleScope
import co.anitrend.core.android.provider.contract.AbstractActionProvider
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class StyleActionProvider(context: Context) : AbstractActionProvider(context), KoinComponent {
    private val settings by inject<ICustomizationSettings>()

    private val viewModes = PreferredViewMode.values()

    private fun onActionClicked(view: View) {
        val current = settings.preferredViewMode.value
        val currentIndex = viewModes.indexOf(current)
        settings.preferredViewMode.value =
            when (currentIndex) {
                PreferredViewMode.DETAILED.ordinal -> PreferredViewMode.SUMMARY
                PreferredViewMode.SUMMARY.ordinal -> PreferredViewMode.COMPACT
                PreferredViewMode.COMPACT.ordinal -> PreferredViewMode.COMFORTABLE
                else -> PreferredViewMode.DETAILED
            }
    }

    private fun iconForSetting(preferredViewMode: PreferredViewMode) {
        val resource =
            when (preferredViewMode) {
                PreferredViewMode.DETAILED -> R.drawable.ic_view_detailed
                PreferredViewMode.SUMMARY -> R.drawable.ic_view_summary
                PreferredViewMode.COMPACT -> R.drawable.ic_view_compact
                else -> R.drawable.ic_view_comfortable
            }
        val drawable = context.getCompatDrawable(resource)
        actionImageView.setImageDrawable(drawable)
    }

    /**
     * Factory for creating the [androidx.core.view.ActionProvider] view
     *
     * @param forItem Optional menu item to create view for
     */
    override fun createWidget(forItem: MenuItem?): View {
        if (!actionImageView.hasOnClickListeners()) {
            actionImageView.setOnClickListener(::onActionClicked)
        }

        context.lifecycleScope()?.launch {
            context.lifecycleOwner()?.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                iconForSetting(settings.preferredViewMode.value)
                runCatching {
                    container.addView(actionImageView)
                }.onFailure(Timber::w)
                settings.preferredViewMode.flow.collect(::iconForSetting)
            }
        }
        return container
    }
}
