/*
 * Copyright (C) 2022 AniTrend
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
package co.anitrend.media.component.action

import android.content.Context
import android.view.MenuItem
import android.view.View
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.core.android.extensions.requireLifecycleOwner
import co.anitrend.core.android.provider.contract.AbstractActionProvider
import co.anitrend.core.ui.sharedViewModel
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.media.component.viewmodel.MediaViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ManageMediaProvider(context: Context) : AbstractActionProvider(context), KoinComponent {
    private val viewModel by sharedViewModel<MediaViewModel>()
    private val settings by inject<IUserSettings>()

    private fun iconForState(mediaListStatus: MediaListStatus?) {
        val icon =
            if (mediaListStatus == null) {
                co.anitrend.common.shared.R.drawable.ic_bookmark
            } else {
                co.anitrend.common.shared.R.drawable.ic_edit
            }

        actionImageView.setImageDrawable(
            context.getCompatDrawable(icon),
        )
    }

    /**
     * Factory for creating the [androidx.core.view.ActionProvider] view
     *
     * @param forItem Optional menu item to create view for
     */
    override fun createWidget(forItem: MenuItem?): View {
        if (!actionImageView.hasOnClickListeners()) {
            actionImageView.setOnClickListener { view ->
                viewModel.state.model.value?.let {
                    view.openMediaListSheetFor(it, settings)
                }
            }
        }
        viewModel.state.model.observe(context.requireLifecycleOwner()) { media ->
            iconForState(media.mediaList?.status)
        }
        container.addView(actionImageView)
        return container
    }
}
