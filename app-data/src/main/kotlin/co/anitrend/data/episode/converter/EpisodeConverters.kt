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

package co.anitrend.data.episode.converter

import androidx.annotation.VisibleForTesting
import androidx.core.text.isDigitsOnly
import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.episode.entity.EpisodeEntity
import co.anitrend.data.episode.model.EpisodeModelItem
import co.anitrend.data.rss.RssLocale
import co.anitrend.data.rss.extensions.rcf822ToUnixTime
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.episode.entity.Episode
import java.util.*
import java.util.concurrent.TimeUnit

internal class EpisodeModelConverter(
    override val fromType: (EpisodeModelItem) -> EpisodeEntity = ::transform,
    override val toType: (EpisodeEntity) -> EpisodeModelItem = { throw NotImplementedError() }
) : SupportConverter<EpisodeModelItem, EpisodeEntity>() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    companion object : ISupportTransformer<EpisodeModelItem, EpisodeEntity> {

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun List<EpisodeModelItem.ThumbnailModel>?.toCoverImage(): EpisodeEntity.CoverImage {
            if (!isNullOrEmpty()) {
                val imageUrls = sortedBy(EpisodeModelItem.ThumbnailModel::width)
                    .map(EpisodeModelItem.ThumbnailModel::url)
                    .let { thumbnails ->
                        if (thumbnails.size > 2)
                            thumbnails.takeLast(2)
                        else thumbnails
                    }

                return EpisodeEntity.CoverImage(
                    medium = imageUrls.firstOrNull(),
                    large = imageUrls.lastOrNull()
                )
            }

            return EpisodeEntity.CoverImage(null, null)
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun EpisodeModelItem.durationFormatted() : String {
            return duration?.let {
                val minutes = TimeUnit.SECONDS.toMinutes(it.toLong())
                val seconds = it - TimeUnit.MINUTES.toSeconds(minutes)
                "${if (minutes < 10) "0$minutes" else minutes}:${if (seconds < 10) "0$seconds" else seconds}"
            } ?: "--:--"
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun extractSubTitles(subtitles: String?): List<RssLocale> {
            return subtitles?.split(',')?.map {
                val segments = it.replace(" ", "").split('-')
                val language = segments.first()
                val country = segments.last()
                "${language}${country.toUpperCase(Locale.ROOT)}"
            }.orEmpty()
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun extractKeyWords(keywords: String?): List<String> {
            return keywords?.split(',')
                ?.map { it.trim() }
                ?.filterNot { keyword ->
                    keyword.toCharArray().all { it.isDigit() }
                }.orEmpty()
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun stripOutImages(description: String?): String? {
            val image = "<img .*?>".toRegex()
            val lineBreak = "<br .>".toRegex()
            return description?.replace(image, "")
                ?.replaceFirst(lineBreak,"")
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun extractSeriesTitle(title: String): String {
            val filter = "(.Season|.- Episode).*".toRegex()
            return title.replace(filter, "")
        }

        override fun transform(source: EpisodeModelItem) = EpisodeEntity(
            title = source.title,
            guid = source.guid,
            mediaId = source.mediaId.toLong(),
            description = stripOutImages(source.description),
            subtitles = extractSubTitles(source.subtitleLanguages),
            coverImage = source.thumbnails.toCoverImage(),
            availability = EpisodeEntity.Availability(
                freeTime = source.freeAvailableDate.rcf822ToUnixTime(),
                premiumTime = source.premiumAvailableDate.rcf822ToUnixTime(),
            ),
            about = EpisodeEntity.Information(
                episodeDuration = source.durationFormatted(),
                episodeTitle = source.episodeTitle,
                episodeNumber = source.episodeNumber,
            ),
            series = EpisodeEntity.Series(
                seriesTitle = extractSeriesTitle(source.title),
                seriesPublisher = source.publisher,
                seriesSeason = source.season,
                keywords = extractKeyWords(source.keywords),
                rating = source.rating
            ),
            id = source.mediaId.toLong()
        )
    }
}

internal class EpisodeEntityConverter(
    override val fromType: (EpisodeEntity) -> Episode = ::transform,
    override val toType: (Episode) -> EpisodeEntity = { throw NotImplementedError() }
) : SupportConverter<EpisodeEntity, Episode>() {
    private companion object : ISupportTransformer<EpisodeEntity, Episode> {

        override fun transform(source: EpisodeEntity) = Episode(
            id = source.id,
            title = source.title,
            guid = source.guid,
            mediaId = source.mediaId,
            description = source.description,
            subtitles = source.subtitles,
            availability = Episode.Availability(
                freeTime = source.availability.freeTime,
                premiumTime = source.availability.premiumTime
            ),
            thumbnail = CoverImage(
                large = source.coverImage?.large,
                medium = source.coverImage?.medium
            ),
            about = Episode.About(
                episodeDuration = source.about.episodeDuration,
                episodeTitle = source.about.episodeTitle,
                episodeNumber = source.about.episodeNumber
            ),
            series = Episode.Series(
                seriesTitle = source.series.seriesTitle,
                seriesPublisher = source.series.seriesPublisher,
                seriesSeason = source.series.seriesSeason,
                keywords = source.series.keywords,
                rating = source.series.rating,
            )
        )
    }
}