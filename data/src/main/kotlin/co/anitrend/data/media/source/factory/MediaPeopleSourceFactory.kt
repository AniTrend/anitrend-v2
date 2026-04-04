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
package co.anitrend.data.media.source.factory

import androidx.paging.DataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.paging.legacy.source.live.SupportPagingLiveDataSource
import co.anitrend.data.media.MediaCharactersController
import co.anitrend.data.media.MediaStaffController
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.source.MediaPeopleSourceImpl
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.Delegates

internal sealed class MediaPeopleSourceFactory<Key : Any, Value : Any> : DataSource.Factory<Key, Value>() {
    protected abstract val stateSourceFlow: MutableStateFlow<SupportPagingLiveDataSource<Key, Value>?>

    class Characters(
        private val remoteSource: MediaRemoteSource,
        private val controller: MediaCharactersController,
        private val dispatcher: ISupportDispatcher,
    ) : MediaPeopleSourceFactory<MediaParam.Characters, MediaPerson.Character>() {
        var initialKey by Delegates.notNull<MediaParam.Characters>()

        override val stateSourceFlow = MutableStateFlow<SupportPagingLiveDataSource<MediaParam.Characters, MediaPerson.Character>?>(null)

        override fun create() =
            MediaPeopleSourceImpl
                .Characters(
                    remoteSource = remoteSource,
                    controller = controller,
                    dispatcher = dispatcher,
                    initialKey = initialKey,
                ).also { stateSourceFlow.value = it }
    }

    class Staff(
        private val remoteSource: MediaRemoteSource,
        private val controller: MediaStaffController,
        private val dispatcher: ISupportDispatcher,
    ) : MediaPeopleSourceFactory<MediaParam.Staff, MediaPerson.Staff>() {
        var initialKey by Delegates.notNull<MediaParam.Staff>()

        override val stateSourceFlow = MutableStateFlow<SupportPagingLiveDataSource<MediaParam.Staff, MediaPerson.Staff>?>(null)

        override fun create() =
            MediaPeopleSourceImpl
                .Staff(
                    remoteSource = remoteSource,
                    controller = controller,
                    dispatcher = dispatcher,
                    initialKey = initialKey,
                ).also { stateSourceFlow.value = it }
    }
}
