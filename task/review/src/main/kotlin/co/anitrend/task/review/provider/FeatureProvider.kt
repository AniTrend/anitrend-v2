/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.task.review.provider

import co.anitrend.navigation.ReviewTaskRouter
import co.anitrend.task.review.component.ReviewDeleteEntryWorker
import co.anitrend.task.review.component.ReviewSaveEntryWorker
import co.anitrend.task.review.component.ReviewVoteEntryWorker

class FeatureProvider : ReviewTaskRouter.Provider {
    override fun reviewVoteEntryWorker() = ReviewVoteEntryWorker::class.java

    override fun reviewSaveEntryWorker() = ReviewSaveEntryWorker::class.java

    override fun reviewDeleteEntryWorker() = ReviewDeleteEntryWorker::class.java
}
