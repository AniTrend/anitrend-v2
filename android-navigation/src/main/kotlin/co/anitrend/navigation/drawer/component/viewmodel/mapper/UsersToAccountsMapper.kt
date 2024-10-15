/*
 * Copyright (C) 2023 AniTrend
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
package co.anitrend.navigation.drawer.component.viewmodel.mapper

import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.drawer.R
import co.anitrend.navigation.drawer.model.account.Account

internal class UsersToAccountsMapper(settings: IAuthenticationSettings) {
    private val unAuthorised =
        listOf(
            Account.Group(
                titleRes = R.string.account_header_active,
                groupId = R.id.account_group_active,
            ),
            Account.Anonymous(
                titleRes = R.string.label_account_anonymous,
                imageRes = co.anitrend.core.R.mipmap.ic_launcher,
                isActiveUser = true,
            ),
            Account.Group(
                titleRes = R.string.account_header_other,
                groupId = R.id.account_group_other,
            ),
            Account.Authorize(
                titleRes = R.string.label_account_add_new,
            ),
        )

    private val otherSection =
        listOf(
            Account.Group(
                titleRes = R.string.account_header_other,
                groupId = R.id.account_group_other,
            ),
            Account.Authorize(
                titleRes = R.string.label_account_add_new,
            ),
        )

    private val activeUserOrNull: (List<User>.() -> User?) = {
        firstOrNull { user ->
            settings.authenticatedUserId.value == user.id
        }
    }

    operator fun invoke(users: List<User>): List<Account> {
        if (users.isEmpty()) {
            return unAuthorised
        }

        val activeUser = users.activeUserOrNull()

        val accounts =
            mutableListOf<Account>(
                Account.Group(
                    titleRes = R.string.account_header_active,
                    groupId = R.id.account_group_active,
                ),
            )

        if (activeUser != null) {
            accounts.add(
                Account.Authenticated(
                    id = activeUser.id,
                    isActiveUser = true,
                    userName = activeUser.name,
                    coverImage = activeUser.avatar,
                ),
            )

            if (users.size > 1) {
                accounts.add(
                    Account.Group(
                        titleRes = R.string.account_header_inactive,
                        groupId = R.id.account_group_inactive,
                    ),
                )
                accounts +=
                    users.filter {
                        activeUser.id != it.id
                    }.map { user ->
                        Account.Authenticated(
                            id = user.id,
                            isActiveUser = false,
                            userName = user.name,
                            coverImage = user.avatar,
                        )
                    }
            }
        }

        accounts += otherSection
        return accounts
    }
}
