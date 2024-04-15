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

package co.anitrend.data.favourite.model.mutation

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.favourite.model.FavouriteInput

internal sealed class FavouriteMutation {

    data class ToggleAnime(
        val param: FavouriteInput.ToggleAnime
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "animeId" to param.animeId,
            "animeIds" to param.animeIds
        )
    }

    data class ToggleManga(
        val param: FavouriteInput.ToggleManga
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "mangaId" to param.mangaId,
            "mangaIds" to param.mangaIds
        )
    }

    data class ToggleCharacter(
        val param: FavouriteInput.ToggleCharacter
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "characterId" to param.characterId,
            "characterIds" to param.characterIds
        )
    }

    data class ToggleStaff(
        val param: FavouriteInput.ToggleStaff
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "staffId" to param.staffId,
            "staffIds" to param.staffIds
        )
    }

    data class ToggleStudio(
        val param: FavouriteInput.ToggleStudio
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "studioId" to param.studioId,
            "studioIds" to param.studioIds
        )
    }

    internal data class UpdateOrderAnime(
        val param: FavouriteInput.UpdateOrderAnime
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "animeOrder" to param.animeOrder
        )
    }

    internal data class UpdateOrderManga(
        val param: FavouriteInput.UpdateOrderManga
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "mangaOrder" to param.mangaOrder
        )
    }

    internal data class UpdateOrderCharacter(
        val param: FavouriteInput.UpdateOrderCharacter
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "characterOrder" to param.characterOrder,
        )
    }

    internal data class UpdateOrderStaff(
        val param: FavouriteInput.UpdateOrderStaff
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "staffOrder" to param.staffOrder,
        )
    }

    internal data class UpdateOrderStudio(
        val param: FavouriteInput.UpdateOrderStudio
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "studioOrder" to param.studioOrder
        )
    }
}
