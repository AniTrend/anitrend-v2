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

package co.anitrend.data.medialist.repository

import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.data.medialist.DeleteCustomMediaListRepository
import co.anitrend.data.medialist.MediaListCollectionRepository
import co.anitrend.data.medialist.MediaListDeleteEntryRepository
import co.anitrend.data.medialist.MediaListEntryRepository
import co.anitrend.data.medialist.MediaListPagedRepository
import co.anitrend.data.medialist.MediaListSaveEntriesRepository
import co.anitrend.data.medialist.MediaListSaveEntryRepository
import co.anitrend.data.medialist.MediaListSyncRepository
import co.anitrend.data.medialist.source.contract.MediaListSource
import co.anitrend.domain.medialist.model.MediaListParam

internal sealed class MediaListRepository{

    class Sync(
        private val source: MediaListSource.Sync
    ) : MediaListRepository(), MediaListSyncRepository {
        override fun sync(param: MediaListParam.Collection) =
            source create source(param)
    }

    class Entry(
        private val source: MediaListSource.Entry
    ) : MediaListRepository(), MediaListEntryRepository {
        override fun getEntry(param: MediaListParam.Entry) =
            source create source(param)
    }

    class Collection(
        private val source: MediaListSource.Collection
    ) : MediaListRepository(), MediaListCollectionRepository {
        override fun getCollection(param: MediaListParam.Collection) =
            source create source(param)
    }

    class Paged(
        private val source: MediaListSource.Paged
    ) : MediaListRepository(), MediaListPagedRepository {
        override fun getPaged(param: MediaListParam.Paged) =
            source create source(param)
    }

    class SaveEntry(
        private val source: MediaListSource.SaveEntry
    ) : MediaListRepository(), MediaListSaveEntryRepository {
        override fun saveEntry(param: MediaListParam.SaveEntry) =
            source create source(param)
    }

    class SaveEntries(
        private val source: MediaListSource.SaveEntries
    ) : MediaListRepository(), MediaListSaveEntriesRepository {
        override fun saveEntries(param: MediaListParam.SaveEntries) =
            source create source(param)
    }

    class DeleteEntry(
        private val source: MediaListSource.DeleteEntry
    ) : MediaListRepository(), MediaListDeleteEntryRepository {
        override fun deleteEntry(param: MediaListParam.DeleteEntry) =
            source create source(param)
    }

    class DeleteCustomList(
        private val source: MediaListSource.DeleteCustomList
    ) : MediaListRepository(), DeleteCustomMediaListRepository {
        override fun deleteCustomList(param: MediaListParam.DeleteCustomList) =
            source create source(param)
    }
}
