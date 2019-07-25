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

package co.anitrend.data.model.response.core.airing

/** [AiringProgression](https://anilist.github.io/ApiV2-GraphQL-Docs/airingprogression.doc.html)
 * Score & Watcher stats for airing anime by episode and mid-week
 *
 * @param episode The episode the stats were recorded at. .5 is the mid point between 2 episodes airing dates
 * @param score The average score for the media
 * @param watching The amount of users watching the anime
 */
data class AiringProgression(
    val episode: Float?,
    val score: Float?,
    val watching: Int?
)