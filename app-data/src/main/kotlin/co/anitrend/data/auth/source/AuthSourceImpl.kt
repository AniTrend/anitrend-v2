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

package co.anitrend.data.auth.source

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.account.action.AccountAction
import co.anitrend.data.android.cache.datasource.CacheLocalSource
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.auth.AuthController
import co.anitrend.data.auth.datasource.local.AuthLocalSource
import co.anitrend.data.auth.datasource.remote.AuthRemoteSource
import co.anitrend.data.auth.entity.AuthEntity
import co.anitrend.data.auth.helper.AuthenticationHelper
import co.anitrend.data.auth.helper.contract.IAuthenticationHelper
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.auth.source.contract.AuthSource
import co.anitrend.data.medialist.datasource.local.MediaListLocalSource
import co.anitrend.data.user.converter.UserEntityConverter
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.account.model.AccountParam
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class AuthSourceImpl(
    private val remoteSource: AuthRemoteSource,
    private val localSource: AuthLocalSource,
    private val clearDataHelper: IClearDataHelper,
    private val controller: AuthController,
    private val settings: IAuthenticationSettings,
    private val converter: UserEntityConverter,
    private val userLocalSource: UserLocalSource,
    private val authenticationHelper: IAuthenticationHelper,
    override val dispatcher: ISupportDispatcher
) : AuthSource() {

    private val userIdFlow = MutableSharedFlow<Long?>()

    override val observable = flow {
        userIdFlow
            .filterNotNull()
            .onEach { id ->
                val userFlow = userLocalSource.userByIdFlow(id)
                    .filterNotNull()
                    .map(converter::convertFrom)
                emitAll(userFlow)
            }.collect()
    }

    override val observables = flow {
        val userIds = localSource.userIds()
        val entities = userLocalSource.userById(userIds)
        emit(converter.convertFrom(entities))
    }

    private suspend fun persistChanges(action: AccountAction.SignIn, user: UserEntity) {
        withContext(dispatcher.io) {
            localSource.upsert(
                AuthEntity(
                    userId = user.id,
                    expiresOn = action.expiresAtTime,
                    tokenType = action.param.tokenType,
                    accessToken = action.param.accessToken
                )
            )

            settings.authenticatedUserId.value = user.id
            settings.isAuthenticated.value = true
            userIdFlow.emit(user.id)
        }
    }

    override fun signOut(param: AccountParam.SignOut) {
        launch (dispatcher.io) {
            authenticationHelper.invalidateAuthenticationState()
            userIdFlow.emit(IAuthenticationSettings.INVALID_USER_ID)
        }
    }

    override fun signIn(param: AccountParam.Activate) {
        launch(dispatcher.io) {
            settings.authenticatedUserId.value = param.userId
            settings.isAuthenticated.value = true
            userIdFlow.emit(param.userId)
        }
    }

    override suspend fun getAuthorizedUser(param: AccountAction.SignIn, callback: RequestCallback) {
        val deferred = async {
            remoteSource.getAuthenticatedUser(
                param.accessTokenBearer,
                QueryContainerBuilder()
            )
        }

        val user = controller(deferred, callback)

        if (user != null)
            persistChanges(param, user)
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            localSource.clear()
        }
    }
}