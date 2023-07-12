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

package co.anitrend.data.medialist.usecase

import co.anitrend.arch.data.repository.contract.ISupportRepository
import co.anitrend.data.medialist.DeleteCustomMediaListInteractor
import co.anitrend.data.medialist.DeleteCustomMediaListRepository
import co.anitrend.data.medialist.DeleteMediaListEntryInteractor
import co.anitrend.data.medialist.GetCollectionMediaListInteractor
import co.anitrend.data.medialist.GetMediaListEntryInteractor
import co.anitrend.data.medialist.GetPagedMediaListInteractor
import co.anitrend.data.medialist.MediaListCollectionRepository
import co.anitrend.data.medialist.MediaListDeleteEntryRepository
import co.anitrend.data.medialist.MediaListEntryRepository
import co.anitrend.data.medialist.MediaListPagedRepository
import co.anitrend.data.medialist.MediaListSaveEntriesRepository
import co.anitrend.data.medialist.MediaListSaveEntryRepository
import co.anitrend.data.medialist.MediaListSyncRepository
import co.anitrend.data.medialist.SaveMediaListEntriesInteractor
import co.anitrend.data.medialist.SaveMediaListEntryInteractor
import co.anitrend.data.medialist.SyncMediaListEntryInteractor

internal interface MediaListInteractor {

    class Sync(
        repository: MediaListSyncRepository
    ) : SyncMediaListEntryInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class Entry(
        repository: MediaListEntryRepository
    ) : GetMediaListEntryInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class Collection(
        repository: MediaListCollectionRepository
    ) : GetCollectionMediaListInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class Paged(
        repository: MediaListPagedRepository
    ) : GetPagedMediaListInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class SaveEntry(
        repository: MediaListSaveEntryRepository
    ) : SaveMediaListEntryInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class SaveEntries(
        repository: MediaListSaveEntriesRepository
    ) : SaveMediaListEntriesInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class DeleteEntry(
        repository: MediaListDeleteEntryRepository
    ) : DeleteMediaListEntryInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class DeleteCustomList(
        repository: DeleteCustomMediaListRepository
    ) : DeleteCustomMediaListInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }
}
