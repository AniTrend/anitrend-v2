/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.media.discover.component.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.android.core.views.compose.composable
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.core.component.content.compose.AniTrendComposition
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.media.discover.component.compose.MediaDiscoverCompose
import co.anitrend.media.discover.component.content.viewmodel.MediaDiscoverViewModel
import co.anitrend.navigation.MediaDiscoverFilterRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asBundle
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.fromBundle
import co.anitrend.navigation.extensions.startActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MediaDiscoverContent : AniTrendComposition() {
    private val settings by inject<Settings>()
    private val viewModel by viewModel<MediaDiscoverViewModel>()

    override val inflateMenu: Int = co.anitrend.android.core.R.menu.discover_menu

    private fun openMediaFilterDialog() {
        val fragmentItem =
            FragmentItem(
                fragment = MediaDiscoverFilterRouter.forSheet(),
                parameter = viewModel.getParam().asBundle(),
            )
        val dialog = fragmentItem.fragmentByTagOrNew(requireActivity())
        dialog.show(requireActivity().supportFragmentManager, fragmentItem.tag())
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     *
     * @see .onCreateOptionsMenu
     */
    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            co.anitrend.android.core.R.id.action_filter -> {
                openMediaFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        composable(requireActivity()) {
            AniTrendTheme3 {
                MediaDiscoverCompose(
                    settings = settings,
                    userSettings = settings,
                    onFilterClick = { openMediaFilterDialog() },
                    onMediaItemClick = { param ->
                        when (param) {
                            is MediaRouter.MediaParam ->
                                MediaRouter.startActivity(
                                    context = requireContext(),
                                    navPayload = param.asNavPayload(),
                                )

                            is MediaListEditorRouter.MediaListEditorParam ->
                                view?.openMediaListSheetFor(
                                    mediaListParam = param,
                                    settings = settings,
                                )

                            else -> Unit
                        }
                    },
                    viewModel = viewModel,
                    showBottomBar = false,
                )
            }
        }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            MediaDiscoverFilterRouter.RESULT_LISTENER_KEY,
            viewLifecycleOwner,
        ) { _, bundle ->
            val result = bundle.fromBundle<MediaDiscoverRouter.MediaDiscoverParam>()
            result?.also(viewModel::setParam)
            Timber.d("Received result for from fragment listener: $result")
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = null
}
