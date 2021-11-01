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

package co.anitrend.data.review.model.container

import co.anitrend.data.common.model.delete.DeletedModel
import co.anitrend.data.common.model.paging.data.IPageModel
import co.anitrend.data.common.model.paging.info.PageInfo
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.data.medialist.model.container.MediaListContainerModel
import co.anitrend.data.review.model.remote.ReviewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class ReviewContainerModel {

    abstract class Single : ReviewContainerModel() {
        abstract val entry: ReviewModel
    }

    abstract class Deleted : ReviewContainerModel() {
        abstract val entry: DeletedModel
    }

    @Serializable
    data class Entry(
        @SerialName("Review") override val entry: ReviewModel.Extended
    ) : Single()

    @Serializable
    data class SavedEntry(
        @SerialName("SaveReview") override val entry: ReviewModel.Core
    ) : Single()

    @Serializable
    data class RatedEntry(
        @SerialName("RateReview") override val entry: ReviewModel.Core
    ) : Single()

    @Serializable
    data class DeletedEntry(
        @SerialName("DeleteReview") override val entry: DeletedModel
    ) : Deleted()

    @Serializable
    data class Paged(
        @SerialName("Page") val page: Page = Page()
    ) {
        @Serializable
        data class Page(
            @SerialName("pageInfo") override val pageInfo: PageInfo? = null,
            @SerialName("review") val entries: List<ReviewModel.Extended> = emptyList()
        ) : IPageModel
    }
}