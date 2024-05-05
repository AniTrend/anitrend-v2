/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.feed.news.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.arch.extension.ext.empty
import co.anitrend.data.feed.extensions.rcf822ToUnixTime
import co.anitrend.data.feed.news.entity.NewsEntity
import co.anitrend.data.feed.news.model.NewsModelItem
import co.anitrend.domain.news.entity.News
import okhttp3.HttpUrl.Companion.toHttpUrl

internal class NewsModelConverter(
    override val fromType: (NewsModelItem) -> NewsEntity = ::transform,
    override val toType: (NewsEntity) -> NewsModelItem = { throw NotImplementedError() },
) : SupportConverter<NewsModelItem, NewsEntity>() {
    private companion object : ISupportTransformer<NewsModelItem, NewsEntity> {
        private val imageRegex = Regex("<img src\\s*=\\s*\\\\*\"(.+?)\\\\*\"\\s*/>")
        private val breakLineRegex = Regex("<br.*?>")

        fun getImageSrc(content: String): String? {
            val matchResult = imageRegex.find(content)
            if (matchResult != null) {
                return matchResult.groupValues[1]
            }
            return null
        }

        fun getContentWithoutImage(content: String): List<String> {
            return content.replace(breakLineRegex, String.empty())
                .split(imageRegex)
        }

        fun getSubTitle(content: List<String>): String {
            return content.firstOrNull()?.replace(
                breakLineRegex,
                String.empty(),
            ) ?: String.empty()
        }

        fun getShortDescription(content: List<String>): String {
            return if (content.size == 2) {
                content[1].replace(imageRegex, String.empty())
            } else {
                String.empty()
            }
        }

        fun generateId(referenceLink: String): String {
            val url = referenceLink.toHttpUrl()
            val segments = url.pathSegments
            if (segments.size >= 4) {
                return url.pathSegments[4]
            }
            throw UnsupportedOperationException(
                "Path segments are less than 4",
                Throwable(referenceLink),
            )
        }

        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: NewsModelItem): NewsEntity {
            val content = getContentWithoutImage(source.description)

            return NewsEntity(
                id = generateId(source.referenceLink),
                title = source.title,
                image = getImageSrc(source.description),
                author = source.author,
                subTitle = getSubTitle(content),
                description = getShortDescription(content),
                content = source.contentEncoded,
                originalLink = source.referenceLink,
                publishedOn = source.publishedOn.rcf822ToUnixTime(),
            )
        }
    }
}

internal class NewsEntityConverter(
    override val fromType: (NewsEntity) -> News = ::transform,
    override val toType: (News) -> NewsEntity = { throw NotImplementedError() },
) : SupportConverter<NewsEntity, News>() {
    private companion object : ISupportTransformer<NewsEntity, News> {
        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: NewsEntity) =
            News(
                id = source.id.hashCode().toLong(),
                guid = source.id,
                link = source.originalLink,
                title = source.title,
                image = source.image,
                author = source.author,
                subTitle = source.subTitle,
                description = source.description,
                content = source.content,
                publishedOn = source.publishedOn,
            )
    }
}
