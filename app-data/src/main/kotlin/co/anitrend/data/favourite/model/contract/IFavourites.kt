/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.favourite.model.contract

import co.anitrend.data.arch.common.entity.IEntityConnection
import co.anitrend.data.arch.common.entity.IEntityEdge
import co.anitrend.data.character.model.contract.ICharacter
import co.anitrend.data.media.model.contract.IMedia
import co.anitrend.data.staff.model.contract.IStaff
import co.anitrend.data.studio.model.contract.IStudio


/** [Favourites](https://anilist.github.io/ApiV2-GraphQL-Docs/favourites.doc.html)
 * User's favourite anime, manga, characters, staff & studios
 */
interface IFavourites {
    val anime: IEntityConnection<IEntityEdge<IMedia>, IMedia>?
    val characters: IEntityConnection<IEntityEdge<ICharacter>, ICharacter>?
    val manga: IEntityConnection<IEntityEdge<IMedia>, IMedia>?
    val staff: IEntityConnection<IEntityEdge<IStaff>, IStaff>?
    val studios: IEntityConnection<IEntityEdge<IStudio>, IStudio>?
}