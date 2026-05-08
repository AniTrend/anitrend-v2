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
package co.anitrend.android.navigation.compose.drawer.component.content

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.anitrend.android.core.components.sheet.action.contract.OnSlideAction
import co.anitrend.android.core.components.sheet.action.contract.OnStateChangedAction
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.android.core.views.compose.composable
import co.anitrend.core.component.sheet.compose.AniTrendSheetComposition
import co.anitrend.android.navigation.compose.drawer.component.screen.NavigationDrawerSheetScreen
import co.anitrend.android.navigation.drawer.component.content.contract.INavigationDrawer
import co.anitrend.android.navigation.drawer.component.viewmodel.DrawerViewModel
import co.anitrend.android.navigation.drawer.model.internal.DrawerEvent
import co.anitrend.android.navigation.drawer.model.internal.DrawerLegacyNavigationAdapter
import co.anitrend.android.navigation.drawer.model.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeNavigationDrawerSheet :
    AniTrendSheetComposition(),
    INavigationDrawer {
    private val drawerViewModel by viewModel<DrawerViewModel>(
        ownerProducer = { requireActivity() },
    )

    private val closeDrawerOnBackPressed =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                if (!collapseAccountSwitcherIfExpanded()) {
                    dismiss()
                }
            }
        }

    private fun collapseAccountSwitcherIfExpanded(): Boolean {
        if (!drawerViewModel.uiState.value.isAccountSwitcherExpanded) {
            return false
        }

        drawerViewModel.setAccountSwitcherExpanded(false)
        return true
    }

    override val navigationFlow: Flow<Navigation.Menu>
        get() =
            drawerViewModel.events.mapNotNull { event ->
                when (event) {
                    is DrawerEvent.Navigate -> DrawerLegacyNavigationAdapter.toLegacy(event.item)
                }
            }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        super.initializeComponents(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            closeDrawerOnBackPressed,
        )
        bottomSheetCallback.addOnStateChangedAction(
            object : OnStateChangedAction {
                override fun onStateChanged(
                    sheet: View,
                    newState: Int,
                ) {
                    val isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
                    closeDrawerOnBackPressed.isEnabled = isVisible
                    drawerViewModel.setSheetVisible(isVisible)
                    if (!isVisible) {
                        drawerViewModel.setAccountSwitcherExpanded(false)
                    }
                }
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        composable(requireActivity()) {
            val uiState by drawerViewModel.uiState.collectAsStateWithLifecycle()

            AniTrendTheme3 {
                NavigationDrawerSheetScreen(
                    uiState = uiState,
                    onHeaderClick = {
                        drawerViewModel.toggleAccountSwitcher()
                    },
                    onAccountClick = {
                        drawerViewModel.setAccountSwitcherExpanded(false)
                    },
                    onNavigationClick = { item ->
                        drawerViewModel.onDestinationSelected(item.destination)
                    },
                )
            }
        }

    override fun onDismiss(dialog: DialogInterface) {
        drawerViewModel.setSheetVisible(false)
        drawerViewModel.setAccountSwitcherExpanded(false)
        super.onDismiss(dialog)
    }

    override fun isShowing(): Boolean = dialog?.isShowing == true

    override fun toggleDrawer() {
        if (collapseAccountSwitcherIfExpanded()) {
            return
        }

        if (isShowing()) {
            dismiss()
        } else {
            show()
        }
    }

    override fun show() {
        drawerViewModel.setSheetVisible(true)
        runCatching {
            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    override fun dismiss() {
        if (collapseAccountSwitcherIfExpanded()) {
            return
        }

        drawerViewModel.setSheetVisible(false)
        super.dismiss()
    }

    override fun addOnSlideAction(action: OnSlideAction) {
        bottomSheetCallback.addOnSlideAction(action)
    }

    override fun removeOnSlideAction(action: OnSlideAction) {
        bottomSheetCallback.removeOnSlideAction(action)
    }

    override fun addOnStateChangedAction(action: OnStateChangedAction) {
        bottomSheetCallback.addOnStateChangedAction(action)
    }

    override fun removeOnStateChangedAction(action: OnStateChangedAction) {
        bottomSheetCallback.removeOnStateChangedAction(action)
    }

    override suspend fun setCheckedItem(
        @IdRes selectedItem: Int,
    ) {
        drawerViewModel.setCheckedItem(selectedItem)
    }

    override fun toggleMenuVisibility(showDrawerMenu: Boolean) {
        setMenuVisibility(showDrawerMenu)
    }
}
