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

package co.anitrend.task.medialist.provider

import co.anitrend.navigation.MediaListTaskRouter
import co.anitrend.task.medialist.component.MediaListCollectionWorker
import co.anitrend.task.medialist.component.MediaListMutationWorker
import co.anitrend.task.medialist.component.MediaListSyncWorker

internal class FeatureProvider : MediaListTaskRouter.Provider {
    override fun mediaListCollectionWorker() = MediaListCollectionWorker::class.java

    override fun mediaListMutationWorker() = MediaListMutationWorker::class.java

    override fun mediaListSyncWorker() = MediaListSyncWorker::class.java
}