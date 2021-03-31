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

package co.anitrend.navigation.drawer.component.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Main
import co.anitrend.data.account.AccountInteractor
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.account.model.AccountParam
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.drawer.R
import co.anitrend.navigation.drawer.model.account.Account
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

internal class AccountState(
    settings: IAuthenticationSettings,
    private val useCase: AccountInteractor
) : ISupportViewModelState<List<Account>>, ISupportCoroutine by Main() {

    var context by Delegates.notNull<CoroutineContext>()

    private val useCaseResult = MutableLiveData<DataState<List<User>>>()

    override val model = Transformations.switchMap(useCaseResult) { dataState ->
            dataState.model.asLiveData(context).map { users ->
                if (users.isNullOrEmpty())
                    listOf(
                        Account.Group(
                            titleRes = R.string.account_header_active,
                            groupId = R.id.account_group_active
                        ),
                        Account.Anonymous(
                            titleRes = R.string.label_account_anonymous,
                            imageRes = R.mipmap.ic_launcher,
                            isActiveUser = true
                        ),
                        Account.Group(
                            titleRes = R.string.account_header_other,
                            groupId = R.id.account_group_other
                        ),
                        Account.Authorize(
                            titleRes = R.string.label_account_add_new
                        )
                    )
                else {
                    val activeUser = users.firstOrNull { user ->
                        settings.authenticatedUserId.value == user.id
                    }

                    val accounts = mutableListOf<Account>(
                        Account.Group(
                            titleRes = R.string.account_header_active,
                            groupId = R.id.account_group_active
                        )
                    )

                    if (activeUser != null) {
                        accounts.add(
                            Account.Authenticated(
                                id = activeUser.id,
                                isActiveUser = true,
                                userName = activeUser.name,
                                coverImage = activeUser.avatar
                            )
                        )

                        if (users.size > 1) {
                            accounts.add(
                                Account.Group(
                                    titleRes = R.string.account_header_inactive,
                                    groupId = R.id.account_group_inactive
                                )
                            )
                            accounts += users.filter {
                                activeUser.id != it.id
                            }.map { user ->
                                Account.Authenticated(
                                    id = user.id,
                                    isActiveUser = false,
                                    userName = user.name,
                                    coverImage = user.avatar
                                )
                            }
                        }
                    }

                    accounts += listOf(
                        Account.Group(
                            titleRes = R.string.account_header_other,
                            groupId = R.id.account_group_other
                        ),
                        Account.Authorize(
                            titleRes = R.string.label_account_add_new
                        )
                    )
                    accounts
                }
            }
        }

    override val loadState = Transformations.switchMap(useCaseResult) {
        it.loadState.asLiveData(context)
    }

    override val refreshState = Transformations.switchMap(useCaseResult) {
        it.refreshState.asLiveData(context)
    }

    init {
        launch {
            settings.isAuthenticated.flow.onEach {
                invoke()
            }.catch { cause ->
                Timber.w(cause)
            }.collect()
        }
    }

    operator fun invoke() {
        val result = useCase.getAuthorizedAccounts()
        useCaseResult.postValue(result)
    }

    fun signOut(account: Account) {
        useCase.signOut(
            AccountParam.SignOut(
                userId = account.id
            )
        )
    }

    /**
     * Called upon [androidx.lifecycle.ViewModel.onCleared] and should optionally
     * call cancellation of any ongoing jobs.
     *
     * If your use case source is of type [co.anitrend.arch.domain.common.IUseCase]
     * then you could optionally call [co.anitrend.arch.domain.common.IUseCase.onCleared] here
     */
    override fun onCleared() {
        useCase.onCleared()
        cancelAllChildren()
    }

    /**
     * Triggers use case to perform refresh operation
     */
    override suspend fun refresh() {
        val uiModel = useCaseResult.value
        uiModel?.refresh?.invoke()
    }

    /**
     * Triggers use case to perform a retry operation
     */
    override suspend fun retry() {
        val uiModel = useCaseResult.value
        uiModel?.retry?.invoke()
    }
}