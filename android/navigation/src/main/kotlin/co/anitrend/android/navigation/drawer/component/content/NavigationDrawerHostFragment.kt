/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.android.navigation.drawer.component.content

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.android.core.components.sheet.action.contract.OnSlideAction
import co.anitrend.android.core.components.sheet.action.contract.OnStateChangedAction
import co.anitrend.data.settings.developer.IDeveloperSettings
import co.anitrend.android.navigation.compose.drawer.component.content.ComposeNavigationDrawerSheet
import co.anitrend.android.navigation.drawer.R
import co.anitrend.android.navigation.drawer.component.content.contract.INavigationDrawer
import co.anitrend.android.navigation.drawer.component.viewmodel.DrawerViewModel
import co.anitrend.android.navigation.drawer.model.internal.DrawerEvent
import co.anitrend.android.navigation.drawer.model.internal.DrawerLegacyNavigationAdapter
import co.anitrend.android.navigation.drawer.model.navigation.Navigation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.koin.android.ext.android.inject
import org.koin.androidx.fragment.android.replace
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigationDrawerHostFragment :
    Fragment(R.layout.navigation_drawer_host),
    INavigationDrawer {
    private val developerSettings by inject<IDeveloperSettings>()

    private val drawerViewModel by viewModel<DrawerViewModel>(
        ownerProducer = { requireActivity() },
    )

    private val useComposeDrawer by lazy(UNSAFE) {
        developerSettings.experimentalComposeUi.value
    }

    private val pendingSlideActions = linkedSetOf<OnSlideAction>()
    private val pendingStateActions = linkedSetOf<OnStateChangedAction>()

    override val navigationFlow: Flow<Navigation.Menu>
        get() =
            drawerViewModel.events.mapNotNull { event ->
                when (event) {
                    is DrawerEvent.Navigate -> DrawerLegacyNavigationAdapter.toLegacy(event.item)
                }
            }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (!useComposeDrawer) {
            ensureLegacyDrawer()
        }
    }

    override fun isShowing(): Boolean =
        if (useComposeDrawer) {
            composeDrawer()?.isShowing() == true
        } else {
            legacyDrawer()?.isShowing() == true
        }

    override fun toggleDrawer() {
        if (useComposeDrawer) {
            val currentSheet = composeDrawer()
            if (currentSheet != null) {
                currentSheet.toggleDrawer()
            } else {
                show()
            }
            return
        }

        ensureLegacyDrawer()
        legacyDrawer()?.toggleDrawer()
    }

    override fun show() {
        if (useComposeDrawer) {
            showComposeDrawer()
            return
        }

        ensureLegacyDrawer()
        legacyDrawer()?.show()
    }

    override fun dismiss() {
        if (useComposeDrawer) {
            composeDrawer()?.dismiss()
            return
        }

        legacyDrawer()?.dismiss()
    }

    override fun addOnSlideAction(action: OnSlideAction) {
        pendingSlideActions += action
        if (useComposeDrawer) {
            composeDrawer()?.addOnSlideAction(action)
        } else {
            legacyDrawer()?.addOnSlideAction(action)
        }
    }

    override fun removeOnSlideAction(action: OnSlideAction) {
        pendingSlideActions -= action
        if (useComposeDrawer) {
            composeDrawer()?.removeOnSlideAction(action)
        } else {
            legacyDrawer()?.removeOnSlideAction(action)
        }
    }

    override fun addOnStateChangedAction(action: OnStateChangedAction) {
        pendingStateActions += action
        if (useComposeDrawer) {
            composeDrawer()?.addOnStateChangedAction(action)
        } else {
            legacyDrawer()?.addOnStateChangedAction(action)
        }
    }

    override fun removeOnStateChangedAction(action: OnStateChangedAction) {
        pendingStateActions -= action
        if (useComposeDrawer) {
            composeDrawer()?.removeOnStateChangedAction(action)
        } else {
            legacyDrawer()?.removeOnStateChangedAction(action)
        }
    }

    override suspend fun setCheckedItem(
        @IdRes selectedItem: Int,
    ) {
        drawerViewModel.setCheckedItem(selectedItem)
    }

    override fun toggleMenuVisibility(showDrawerMenu: Boolean) {
        if (useComposeDrawer) {
            setMenuVisibility(showDrawerMenu)
            composeDrawer()?.toggleMenuVisibility(showDrawerMenu)
        } else {
            legacyDrawer()?.toggleMenuVisibility(showDrawerMenu)
        }
    }

    private fun ensureLegacyDrawer() {
        if (legacyDrawer() != null) {
            applyPendingActionsTo(legacyDrawer())
            return
        }

        childFragmentManager
            .beginTransaction()
            .replace<BottomDrawerContent>(
                R.id.navigationDrawerHostContainer,
                tag = LEGACY_DRAWER_TAG,
            ).commitNow()

        applyPendingActionsTo(legacyDrawer())
    }

    private fun showComposeDrawer() {
        val currentSheet = composeDrawer()
        if (currentSheet != null) {
            currentSheet.show()
            applyPendingActionsTo(currentSheet)
            return
        }

        val sheet = ComposeNavigationDrawerSheet()
        sheet.showNow(childFragmentManager, COMPOSE_DRAWER_TAG)
        applyPendingActionsTo(sheet)
    }

    private fun applyPendingActionsTo(target: INavigationDrawer?) {
        if (target == null) {
            return
        }

        pendingSlideActions.forEach(target::addOnSlideAction)
        pendingStateActions.forEach(target::addOnStateChangedAction)
    }

    private fun legacyDrawer(): INavigationDrawer? = childFragmentManager.findFragmentByTag(LEGACY_DRAWER_TAG) as? INavigationDrawer

    private fun composeDrawer(): ComposeNavigationDrawerSheet? =
        childFragmentManager.findFragmentByTag(COMPOSE_DRAWER_TAG) as? ComposeNavigationDrawerSheet

    private companion object {
        const val LEGACY_DRAWER_TAG = "legacy_navigation_drawer"
        const val COMPOSE_DRAWER_TAG = "compose_navigation_drawer"
    }
}
