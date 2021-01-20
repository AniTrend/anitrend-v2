/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.navigation

import androidx.fragment.app.Fragment
import androidx.work.ListenableWorker
import co.anitrend.navigation.provider.INavigationProvider
import co.anitrend.navigation.router.NavigationRouter
import org.koin.core.component.inject

object MainRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object NavigationDrawerRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object SplashRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object OnBoardingRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object SearchRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object SettingsRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object AboutRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}


object AuthRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object MediaRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun discover(): Class<out Fragment>?
        fun carousel(): Class<out Fragment>?

        companion object {
            fun MediaRouter.forDiscover() = provider.discover()
            fun MediaRouter.forCarousel() = provider.carousel()
        }
    }
}

object CharacterRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object StaffRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object ProfileRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object ForumRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object SocialFeedRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object NotificationRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object UpdaterRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider
}

object GenreTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun worker(): Class<out ListenableWorker>

        companion object {
            fun GenreTaskRouter.forWorker() = provider.worker()
        }
    }
}

object TagTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun worker(): Class<out ListenableWorker>

        companion object {
            fun TagTaskRouter.forWorker() = provider.worker()
        }
    }
}

object UserTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun accountSyncWorker(): Class<out ListenableWorker>
        fun followToggleWorker(): Class<out ListenableWorker>
        fun sendMessageWorker(): Class<out ListenableWorker>
        fun statisticSyncWorker(): Class<out ListenableWorker>

        companion object {
            fun UserTaskRouter.forAccountSyncWorker() = provider.accountSyncWorker()
            fun UserTaskRouter.forFollowToggleWorker() = provider.followToggleWorker()
            fun UserTaskRouter.forSendMessageWorker() = provider.sendMessageWorker()
            fun UserTaskRouter.forStatisticSyncWorker() = provider.statisticSyncWorker()
        }
    }
}

object MediaListTaskRouter : NavigationRouter() {
    override val provider by inject<Provider>()

    interface Provider : INavigationProvider {
        fun mediaListCollectionWorker(): Class<out ListenableWorker>
        fun mediaListMutationWorker(): Class<out ListenableWorker>
        fun mediaListSyncWorker(): Class<out ListenableWorker>

        companion object {
            fun MediaListTaskRouter.forMediaListCollectionWorker() = provider.mediaListCollectionWorker()
            fun MediaListTaskRouter.forMediaListMutationWorker() = provider.mediaListMutationWorker()
            fun MediaListTaskRouter.forMediaListSyncWorker() = provider.mediaListSyncWorker()
        }
    }
}