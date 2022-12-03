/*
 * Copyright (C) 2022  AniTrend
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
import co.anitrend.core.extensions.runIfAuthenticated
import co.anitrend.core.ui.sharedViewModel
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.media.component.viewmodel.MediaViewModel
import co.anitrend.navigation.FavouriteTaskRouter
import co.anitrend.navigation.extensions.createOneTimeUniqueWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MediaFavouriteActionProvider(context: Context) : AbstractActionProvider(context), KoinComponent {

    private val viewModel by sharedViewModel<MediaViewModel>()
    private val settings by inject<IAuthenticationSettings>()

    private fun onActionClick(view: View) {
        view.runIfAuthenticated(settings) {
            // TODO: Add FavouriteTaskRouter to handle favourites of multiple types of media
            FavouriteTaskRouter.forWorker()
                .createOneTimeUniqueWorker(view.context, params = null)
                .enqueue()
        }
    }

    private fun iconForState(isFavourite: Boolean) {
        with (actionImageView) {
            setImageDrawable(
                context.getCompatDrawable(
                    when (isFavourite) {
                        true -> co.anitrend.core.android.R.drawable.ic_heart_filled
                        else -> co.anitrend.core.android.R.drawable.ic_heart_outline
                    }
                )
            )
        }
    }

    /**
     * Factory for creating the [androidx.core.view.ActionProvider] view
     *
     * @param forItem Optional menu item to create view for
     */
    override fun createWidget(forItem: MenuItem?): View {
        if (!actionImageView.hasOnClickListeners())
            actionImageView.setOnClickListener(::onActionClick)
        viewModel.state.model.observe(context.requireLifecycleOwner()) { media ->
            iconForState(media.isFavourite)
        }
        container.addView(actionImageView)
        return container
    }
}
