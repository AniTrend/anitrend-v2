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
package co.anitrend.android.navigation.drawer.component.viewmodel

import androidx.annotation.IdRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.data.account.AccountInteractor
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.edge.config.GetConfigInteractor
import co.anitrend.android.navigation.drawer.component.viewmodel.mapper.UsersToAccountsMapper
import co.anitrend.android.navigation.drawer.model.account.Account
import co.anitrend.android.navigation.drawer.model.internal.DrawerConfigMapper
import co.anitrend.android.navigation.drawer.model.internal.DrawerDefaults
import co.anitrend.android.navigation.drawer.model.internal.DrawerDestination
import co.anitrend.android.navigation.drawer.model.internal.DrawerEntry
import co.anitrend.android.navigation.drawer.model.internal.DrawerEvent
import co.anitrend.android.navigation.drawer.model.internal.DrawerLegacyNavigationAdapter
import co.anitrend.android.navigation.drawer.model.internal.DrawerSelectionResolver
import co.anitrend.android.navigation.drawer.model.internal.DrawerUiState
import co.anitrend.domain.config.entity.Config
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

internal class DrawerViewModel(
    private val accountMapper: UsersToAccountsMapper,
    private val accountInteractor: AccountInteractor,
    private val configInteractor: GetConfigInteractor,
    private val authSettings: IAuthenticationSettings,
    override val savedStateHandle: SavedStateHandle,
    private val mapper: DrawerConfigMapper = DrawerConfigMapper(),
    private val defaultNavigation: List<Config.Navigation> = DrawerDefaults.navigation,
) : AniTrendViewModel() {
    private val mutableEvents = MutableSharedFlow<DrawerEvent>(extraBufferCapacity = 4)

    private val mutableUiState =
        MutableStateFlow(
            DrawerUiState(
                entries = emptyList(),
                selectedDestination = restoredSelection(),
            ),
        )

    val events = mutableEvents.asSharedFlow()
    val uiState = mutableUiState.asStateFlow()

    private var lastKnownNavigation = defaultNavigation
    private var hasObservedAuthentication = false

    init {
        applyNavigationState(
            navigation = defaultNavigation,
            requestedSelection = restoredSelection(),
            emitCompatibilityEvent = false,
        )
        observeAuthentication()
        observeAccounts()
        observeConfig()
    }

    fun setCheckedItem(
        @IdRes itemId: Int,
    ) {
        val destination = DrawerLegacyNavigationAdapter.fromLegacyMenuId(itemId) ?: return
        applyNavigationState(
            navigation = lastKnownNavigation,
            requestedSelection = destination,
            emitCompatibilityEvent = false,
        )
    }

    fun onLegacyMenuSelected(
        @IdRes itemId: Int,
    ) {
        val destination = DrawerLegacyNavigationAdapter.fromLegacyMenuId(itemId) ?: return
        onDestinationSelected(destination)
    }

    fun onDestinationSelected(destination: DrawerDestination) {
        val selectedItem = currentItemFor(destination) ?: return

        if (selectedItem.isCheckable) {
            applyNavigationState(
                navigation = lastKnownNavigation,
                requestedSelection = destination,
                emitCompatibilityEvent = false,
            )
        }

        mutableEvents.tryEmit(
            DrawerEvent.Navigate(
                item = currentItemFor(destination) ?: selectedItem,
            ),
        )
    }

    fun setSheetVisible(visible: Boolean) {
        mutableUiState.value =
            mutableUiState.value.copy(
                isSheetVisible = visible,
                isAccountSwitcherExpanded =
                    if (visible) {
                        mutableUiState.value.isAccountSwitcherExpanded
                    } else {
                        false
                    },
            )
    }

    fun setAccountSwitcherExpanded(expanded: Boolean) {
        mutableUiState.value =
            mutableUiState.value.copy(
                isAccountSwitcherExpanded = expanded,
            )
    }

    fun toggleAccountSwitcher() {
        setAccountSwitcherExpanded(!mutableUiState.value.isAccountSwitcherExpanded)
    }

    private fun observeAuthentication() {
        viewModelScope.launch {
            authSettings.isAuthenticated.flow
                .distinctUntilChanged()
                .collectLatest {
                    applyNavigationState(
                        navigation = lastKnownNavigation,
                        requestedSelection = mutableUiState.value.selectedDestination,
                        emitCompatibilityEvent = hasObservedAuthentication,
                    )
                    hasObservedAuthentication = true
                }
        }
    }

    private fun observeAccounts() {
        viewModelScope.launch {
            accountInteractor
                .getAuthorizedAccounts()
                .model
                .catch { throwable ->
                    Timber.e(throwable, "Unable to observe drawer accounts")
                }.collectLatest { users ->
                    val accounts = accountMapper(users)
                    mutableUiState.value =
                        mutableUiState.value.copy(
                            accounts = accounts,
                            activeAccount = accounts.firstOrNull { account -> account.isActiveUser },
                        )
                }
        }
    }

    private fun observeConfig() {
        viewModelScope.launch {
            val dataState = configInteractor()

            launch {
                dataState.model
                    .catch { throwable ->
                        Timber.e(throwable, "Unable to observe remote drawer config")
                    }.collectLatest { config ->
                        if (config.navigation.isEmpty()) {
                            Timber.w("Remote drawer config navigation was empty, keeping last known menu")
                            return@collectLatest
                        }

                        lastKnownNavigation = config.navigation
                        applyNavigationState(
                            navigation = config.navigation,
                            requestedSelection = mutableUiState.value.selectedDestination,
                            emitCompatibilityEvent = false,
                        )
                    }
            }

            launch {
                dataState.loadState
                    .onEach { loadState ->
                        if (loadState is LoadState.Error) {
                            Timber.w(loadState.details, "Remote drawer config refresh failed, keeping last known menu")
                        }
                    }.catch { throwable ->
                        Timber.e(throwable, "Unable to observe remote drawer config load state")
                    }.collectLatest { }
            }
        }
    }

    private fun applyNavigationState(
        navigation: List<Config.Navigation>,
        requestedSelection: DrawerDestination,
        emitCompatibilityEvent: Boolean,
    ) {
        val authenticated = authSettings.isAuthenticated.value
        val mappedEntries =
            mapper.map(
                navigation = navigation,
                authenticated = authenticated,
                selectedDestination = requestedSelection,
            )
        val resolvedSelection =
            DrawerSelectionResolver.resolve(
                currentSelection = requestedSelection,
                entries = mappedEntries,
            )
        val resolvedEntries =
            if (resolvedSelection == requestedSelection) {
                mappedEntries
            } else {
                mapper.map(
                    navigation = navigation,
                    authenticated = authenticated,
                    selectedDestination = resolvedSelection,
                )
            }

        mutableUiState.value =
            mutableUiState.value.copy(
                entries = resolvedEntries,
                selectedDestination = resolvedSelection,
            )
        saveSelection(resolvedSelection)

        if (emitCompatibilityEvent && resolvedSelection != requestedSelection) {
            currentItemFor(resolvedSelection)?.also { item ->
                mutableEvents.tryEmit(DrawerEvent.Navigate(item))
            }
        }
    }

    private fun currentItemFor(destination: DrawerDestination): DrawerEntry.Item? =
        mutableUiState.value.entries
            .filterIsInstance<DrawerEntry.Item>()
            .firstOrNull { item ->
                item.destination == destination
            }

    private fun restoredSelection(): DrawerDestination =
        savedStateHandle
            .get<Int>(SAVED_SELECTION_ID)
            ?.let(DrawerLegacyNavigationAdapter::fromLegacyMenuId)
            ?: DrawerDestination.Home

    private fun saveSelection(selection: DrawerDestination) {
        DrawerLegacyNavigationAdapter.legacyMenuIdFor(selection)?.also { legacyId ->
            savedStateHandle[SAVED_SELECTION_ID] = legacyId
        }
    }

    private companion object {
        const val SAVED_SELECTION_ID = "drawer_selected_menu_id"
    }
}
