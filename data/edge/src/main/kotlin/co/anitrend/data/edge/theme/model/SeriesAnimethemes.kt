/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.theme.model

import co.anitrend.data.edge.graphql.GetMediaByIdData

// Theme computed behavior preserved from the former hand-written transport model,
// expressed against the generated [GetMediaByIdData.SeriesAnimethemes] path type.

/** Whether the theme carries any persistable content (a song title or a video link). */
internal val GetMediaByIdData.SeriesAnimethemes.isPersistable: Boolean
    get() = !name.isNullOrBlank() || !video.isNullOrBlank()

/** Song title of the theme. */
internal val GetMediaByIdData.SeriesAnimethemes.name: String?
    get() = song?.title

/** First video link found across the theme entries. */
internal val GetMediaByIdData.SeriesAnimethemes.video: String?
    get() =
        animethemeentries
            .orEmpty()
            .filterNotNull()
            .firstNotNullOfOrNull { entry ->
                entry.videos
                    .orEmpty()
                    .filterNotNull()
                    .firstOrNull()
                    ?.link
            }
