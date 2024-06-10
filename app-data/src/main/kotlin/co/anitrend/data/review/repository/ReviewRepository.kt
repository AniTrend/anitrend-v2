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

package co.anitrend.data.review.repository

import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.data.review.ReviewDeleteRepository
import co.anitrend.data.review.ReviewEntryRepository
import co.anitrend.data.review.ReviewPagedRepository
import co.anitrend.data.review.ReviewRateRepository
import co.anitrend.data.review.ReviewSaveRepository
import co.anitrend.data.review.source.contract.ReviewSource
import co.anitrend.domain.review.model.ReviewParam

internal sealed class ReviewRepository {

    class Entry(
        private val source: ReviewSource.Entry
    ) : ReviewRepository(), ReviewEntryRepository {
        override fun getEntry(param: ReviewParam.Entry) =
            source create source(param)
    }

    class Paged(
        private val source: ReviewSource.Paged
    ) : ReviewRepository(), ReviewPagedRepository {
        override fun getPaged(param: ReviewParam.Paged) =
            source create source(param)
    }

    class Rate(
        private val source: ReviewSource.Rate
    ) : ReviewRepository(), ReviewRateRepository {
        override fun rate(param: ReviewParam.Rate) =
            source create source(param)
    }

    class Save(
        private val source: ReviewSource.Save
    ) : ReviewRepository(), ReviewSaveRepository {
        override fun save(param: ReviewParam.Save) =
            source create source(param)
    }

    class Delete(
        private val source: ReviewSource.Delete
    ) : ReviewRepository(), ReviewDeleteRepository {
        override fun delete(param: ReviewParam.Delete) =
            source create source(param)
    }
}
