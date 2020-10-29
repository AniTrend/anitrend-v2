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

package co.anitrend.domain.media.entity

import co.anitrend.domain.common.entity.contract.IEntity
import co.anitrend.domain.media.entity.base.IMediaExtendedWithMediaList
import co.anitrend.domain.media.enums.MediaType

data class MediaCarousel(
    val mediaType: MediaType,
    val carouselType: CarouselType,
    val mediaItems: List<IMediaExtendedWithMediaList>
): IEntity {

    override val id: Long = hashCode().toLong()

    enum class CarouselType {
        AIRING_SOON,
        ALL_TIME_POPULAR,
        TRENDING_RIGHT_NOW,
        POPULAR_THIS_SEASON,
        RECENTLY_ADDED,
        ANTICIPATED_NEXT_SEASON,
        POPULAR_MANHWA
    }

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is MediaCarousel -> other.id == id
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = mediaType.hashCode()
        result = 31 * result + carouselType.hashCode()
        result = 31 * result + mediaItems.hashCode()
        return result
    }
}