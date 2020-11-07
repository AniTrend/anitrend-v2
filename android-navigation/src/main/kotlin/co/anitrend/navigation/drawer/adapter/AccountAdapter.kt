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

package co.anitrend.navigation.drawer.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.SupportListAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import co.anitrend.navigation.drawer.controller.helpers.AccountDiffUtil
import co.anitrend.navigation.drawer.controller.model.account.AnonymousAccountItem
import co.anitrend.navigation.drawer.controller.model.account.AnonymousAccountItem.Companion.createAnonymousAccountViewHolder
import co.anitrend.navigation.drawer.controller.model.account.AuthenticatedAccountItem
import co.anitrend.navigation.drawer.controller.model.account.AuthenticatedAccountItem.Companion.createAuthenticatedAccountViewHolder
import co.anitrend.navigation.drawer.controller.model.account.AuthorizeAccountItem
import co.anitrend.navigation.drawer.controller.model.account.AuthorizeAccountItem.Companion.createAuthorizeAccountViewHolder
import co.anitrend.navigation.drawer.controller.model.account.GroupAccountItem
import co.anitrend.navigation.drawer.controller.model.account.GroupAccountItem.Companion.createAccountGroupViewHolder
import co.anitrend.navigation.drawer.model.account.Account
import co.anitrend.navigation.drawer.model.account.Account.Companion.toAccountType

class AccountAdapter(
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override val mapper: (Account) -> IRecyclerItem = {
        when (it) {
            is Account.Authenticated -> AuthenticatedAccountItem(it)
            is Account.Anonymous -> AnonymousAccountItem(it)
            is Account.Authorize -> AuthorizeAccountItem(it)
            is Account.Group -> GroupAccountItem(it)
        }
    },
    override val customSupportAnimator: AbstractAnimator? = null,
    override val supportAction: ISupportSelectionMode<Long>? = null
) : SupportListAdapter<Account>(AccountDiffUtil) {

    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    override fun getStableIdFor(item: Account?): Long {
        return item?.id ?: RecyclerView.NO_ID
    }

    /**
     * Return the view type of the item at [position] for the purposes of view recycling.
     *
     * The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * [position]. Type codes need not be contiguous.
     */
    override fun getItemViewType(position: Int): Int {
        return when (val viewType = super.getItemViewType(position)) {
            ISupportAdapter.DEFAULT_VIEW_TYPE -> {
                val item = getItem(position)
                item.toAccountType() ?: viewType
            }
            else -> viewType
        }
    }

    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ) = when (viewType) {
        Account.ANONYMOUS -> layoutInflater.createAnonymousAccountViewHolder(parent)
        Account.AUTHENTICATED -> layoutInflater.createAuthenticatedAccountViewHolder(parent)
        Account.GROUP -> layoutInflater.createAccountGroupViewHolder(parent)
        else -> layoutInflater.createAuthorizeAccountViewHolder(parent)
    }
}