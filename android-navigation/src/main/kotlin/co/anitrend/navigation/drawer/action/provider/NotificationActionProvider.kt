/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.navigation.drawer.action.provider

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ActionProvider
import androidx.core.view.setPadding
import co.anitrend.arch.extension.ext.*
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.extensions.lifecycleScope
import co.anitrend.core.android.extensions.requireLifecycleOwner
import co.anitrend.core.android.provider.contract.AbstractActionProvider
import co.anitrend.core.ui.sharedViewModel
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.NotificationRouter
import co.anitrend.navigation.drawer.R
import co.anitrend.navigation.drawer.action.provider.viewmodel.NotificationProviderViewModel
import co.anitrend.navigation.extensions.startActivity
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import kotlinx.coroutines.flow.collect
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class NotificationActionProvider(context: Context) : AbstractActionProvider(context), KoinComponent {

    private val viewModel by sharedViewModel<NotificationProviderViewModel>()

    private val notificationBadge = BadgeDrawable.create(context).apply {
        badgeGravity = BadgeDrawable.TOP_END
        maxCharacterCount = 2
    }

    private val actionImageView = AppCompatImageView(context).apply {
        background = context.getDrawableAttr(
            R.attr.selectableItemBackgroundBorderless
        )
        isClickable = true
        isFocusable = true
        setPadding(2.dp)
        setImageDrawable(
            context.getCompatDrawable(
                R.drawable.ic_notifications_24
            )
        )
        setOnClickListener {
            NotificationRouter.startActivity(it.context)
        }
    }

    private val container by lazy(UNSAFE) {
        FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            )
            setPadding(10.dp)
        }
    }

    private val settings by inject<IAuthenticationSettings>()

    private fun toggleVisibility(forItem: MenuItem?, shouldShow: Boolean) {
        forItem?.isVisible = shouldShow
        if (!shouldShow)
            container.gone()
        else container.visible()
    }

    private fun setUpVisibility(forItem: MenuItem?) {
        val isAuthenticated = settings.isAuthenticated.value
        toggleVisibility(forItem, isAuthenticated)
        actionImageView.contentDescription = forItem?.title
    }

    /**
     * Factory for creating the [ActionProvider] view
     *
     * @param forItem Optional menu item to create view for
     */
    @com.google.android.material.badge.ExperimentalBadgeUtils
    override fun createWidget(forItem: MenuItem?): View {
        setUpVisibility(forItem)
        context.lifecycleScope()?.launchWhenResumed {
            container.addView(actionImageView)
            settings.isAuthenticated.flow.collect {
                toggleVisibility(forItem, it)
                viewModel.fetchUser()
            }
        }

        viewModel.state.model.observe(context.requireLifecycleOwner()) {
            Timber.v("Notification change event triggered")
            val user = it as User.Authenticated
            val unread = user.unreadNotifications
            if (unread > 0) {
                notificationBadge.number = unread
                BadgeUtils.attachBadgeDrawable(
                    notificationBadge, actionImageView, container
                )
            } else {
                BadgeUtils.detachBadgeDrawable(
                    notificationBadge, actionImageView
                )
            }
        }
        return container
    }
}