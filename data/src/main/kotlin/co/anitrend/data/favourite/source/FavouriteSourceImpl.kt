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
package co.anitrend.data.favourite.source

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.favourite.FavouriteToggleController
import co.anitrend.data.favourite.datasource.remote.FavouriteRemoteSource
import co.anitrend.data.favourite.source.contract.FavouriteSource
import co.anitrend.data.graphql.anilist.ToggleAnimeFavourite
import co.anitrend.data.graphql.anilist.ToggleAnimeFavouriteVariables
import co.anitrend.data.graphql.anilist.ToggleMangaFavourite
import co.anitrend.data.graphql.anilist.ToggleMangaFavouriteVariables
import co.anitrend.domain.favourite.model.FavouriteInput
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow

internal class FavouriteSourceImpl {
    class Toggle(
        private val remoteSource: FavouriteRemoteSource,
        private val controller: FavouriteToggleController,
        override val dispatcher: ISupportDispatcher,
    ) : FavouriteSource.Toggle() {
        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun toggleFavourite(requestCallback: RequestCallback) {
            val deferred =
                deferred {
                    when (val action = mutation) {
                        is FavouriteInput.ToggleAnime ->
                            remoteSource.toggleAnimeFavorite(
                                ToggleAnimeFavourite.request(
                                    animeId = action.animeId?.toInt(),
                                ),
                            )
                        is FavouriteInput.ToggleManga ->
                            remoteSource.toggleMangaFavorite(
                                ToggleMangaFavourite.request(
                                    mangaId = action.mangaId?.toInt(),
                                ),
                            )
                        else -> error("Unsupported favourite mutation: ${action::class.simpleName}")
                    }
                }

            val result = controller(deferred, requestCallback)
            observable.value = result == true
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
        }
    }
}
