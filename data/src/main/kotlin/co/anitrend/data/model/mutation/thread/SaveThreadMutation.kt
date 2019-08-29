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

package co.anitrend.data.model.mutation.thread

import co.anitrend.data.model.response.contract.IGraphQuery

/** [SaveThread mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Create or update a forum thread
 *
 * @param id The thread id, required for updating
 * @param title The title of the thread
 * @param body The main text body of the thread
 * @param categories Forum categories the thread should be within
 * @param mediaCategories Media related to the contents of the thread
 */
data class SaveThreadMutation(
    val id: Int? = null,
    val title: String,
    val body: String,
    val categories: List<Int>,
    val mediaCategories: List<Int>
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "title" to title,
        "body" to body,
        "categories" to categories,
        "mediaCategories" to mediaCategories
    )
}