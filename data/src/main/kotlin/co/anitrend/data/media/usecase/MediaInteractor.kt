/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.data.media.usecase

import co.anitrend.data.media.GetDetailMediaInteractor
import co.anitrend.data.media.GetMediaCharactersInteractor
import co.anitrend.data.media.GetPagingMediaInteractor
import co.anitrend.data.media.GetMediaRecommendationsInteractor
import co.anitrend.data.media.GetMediaRelationsInteractor
import co.anitrend.data.media.GetMediaStaffInteractor
import co.anitrend.data.media.GetMediaStatsInteractor
import co.anitrend.data.media.GetMediaStudiosInteractor
import co.anitrend.data.media.MediaCharactersRepository
import co.anitrend.data.media.MediaDetailRepository
import co.anitrend.data.media.MediaPagingRepository
import co.anitrend.data.media.MediaRecommendationsRepository
import co.anitrend.data.media.MediaRelationsRepository
import co.anitrend.data.media.MediaStaffRepository
import co.anitrend.data.media.MediaStatsRepository
import co.anitrend.data.media.MediaStudiosRepository

internal interface MediaInteractor {
    class Detail(
        repository: MediaDetailRepository,
    ) : GetDetailMediaInteractor(repository)

    class Relations(
        repository: MediaRelationsRepository,
    ) : GetMediaRelationsInteractor(repository)

    class Recommendations(
        repository: MediaRecommendationsRepository,
    ) : GetMediaRecommendationsInteractor(repository)

    class Paging(
        repository: MediaPagingRepository,
    ) : GetPagingMediaInteractor(repository)

    class Characters(
        repository: MediaCharactersRepository,
    ) : GetMediaCharactersInteractor(repository)

    class Staff(
        repository: MediaStaffRepository,
    ) : GetMediaStaffInteractor(repository)

    class Studios(
        repository: MediaStudiosRepository,
    ) : GetMediaStudiosInteractor(repository)

    class Stats(
        repository: MediaStatsRepository,
    ) : GetMediaStatsInteractor(repository)
}
