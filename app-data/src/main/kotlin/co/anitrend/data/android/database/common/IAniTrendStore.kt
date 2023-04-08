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

package co.anitrend.data.android.database.common

import androidx.room.RoomDatabase
import co.anitrend.data.airing.datasource.local.IAiringStore
import co.anitrend.data.android.cache.datasource.ICacheStore
import co.anitrend.data.auth.datasource.local.IAuthStore
import co.anitrend.data.carousel.datasource.local.ICarouselStore
import co.anitrend.data.character.datasource.local.ICharacterStore
import co.anitrend.data.customlist.datasource.ICustomListStore
import co.anitrend.data.customscore.datasource.ICustomScoreStore
import co.anitrend.data.edge.config.datasource.local.IEdgeConfigStore
import co.anitrend.data.feed.episode.datasource.local.IEpisodeStore
import co.anitrend.data.feed.news.datasource.local.INewsStore
import co.anitrend.data.genre.datasource.local.IGenreStore
import co.anitrend.data.jikan.media.datasource.local.IJikanStore
import co.anitrend.data.link.datasource.ILinkStore
import co.anitrend.data.media.datasource.local.IMediaStore
import co.anitrend.data.medialist.datasource.local.IMediaListStore
import co.anitrend.data.rank.datasource.IRankStore
import co.anitrend.data.relation.datasource.local.IRelationStore
import co.anitrend.data.review.datasource.local.IReviewStore
import co.anitrend.data.staff.datasource.local.IStaffStore
import co.anitrend.data.studio.datasource.local.IStudioStore
import co.anitrend.data.tag.datasource.local.ITagStore
import co.anitrend.data.user.datasource.local.IUserStore

internal interface IAniTrendStore : IMediaStore, IGenreStore, ITagStore,
    IRelationStore, IAuthStore, IAiringStore, ICarouselStore, ICacheStore, IUserStore,
    IMediaListStore, INewsStore, IEpisodeStore, ICharacterStore, IStaffStore, IStudioStore,
    ILinkStore, IRankStore, IJikanStore, ICustomListStore, ICustomScoreStore, IReviewStore,
    IEdgeConfigStore {

    companion object {

        /** Binding types for [IAniTrendStore] */
        val BINDINGS = listOf(
            IMediaStore::class, ITagStore::class, IGenreStore::class,
            IRelationStore::class, IAuthStore::class, IAiringStore::class,
            ICarouselStore::class, ICacheStore::class, IUserStore::class,
            IMediaListStore::class, INewsStore::class, IEpisodeStore::class,
            ICharacterStore::class, IStaffStore::class, IStudioStore::class,
            ILinkStore::class, IRankStore::class, IJikanStore::class,
            ICustomListStore::class, ICustomScoreStore::class,
            IReviewStore::class, IEdgeConfigStore::class, RoomDatabase::class
        )
    }
}
