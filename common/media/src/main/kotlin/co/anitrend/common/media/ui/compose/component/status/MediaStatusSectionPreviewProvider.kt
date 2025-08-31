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
package co.anitrend.common.media.ui.compose.component.status

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.media.enums.MediaStatus
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit

data class MediaStatusSectionPreviewProvider(
    override val values: Sequence<IMedia> =
        sequenceOf(
            Media.Extended.empty().copy(
                status = MediaStatus.FINISHED,
                category =
                    Media.Category.Manga(
                        chapters = 64,
                        volumes = 8,
                    ),
            ),
            Media.Extended.empty().copy(
                status = MediaStatus.RELEASING,
                category =
                    Media.Category.Anime(
                        episodes = 12,
                        duration = 24,
                        broadcast = "Fridays 17:00 (JST)",
                        premiered = "Spring 2025",
                        schedule =
                            AiringSchedule(
                                airingAt = Instant.now().plus(2, ChronoUnit.HOURS).epochSecond,
                                episode = 10,
                                mediaId = 1,
                                timeUntilAiring = 62811,
                                id = 1,
                            ),
                    ),
            ),
            Media.Extended.empty().copy(
                status = MediaStatus.HIATUS,
                category =
                    Media.Category.Anime(
                        episodes = 12,
                        duration = 24,
                        broadcast = "",
                        premiered = "",
                        schedule = null,
                    ),
            ),
            Media.Extended.empty().copy(
                status = MediaStatus.CANCELLED,
                category =
                    Media.Category.Anime(
                        episodes = 12,
                        duration = 24,
                        broadcast = "",
                        premiered = "",
                        schedule = null,
                    ),
            ),
        ),
) : PreviewParameterProvider<IMedia>
