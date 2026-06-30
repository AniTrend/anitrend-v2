/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.data.core.api.converter.request

import co.anitrend.data.BuildConfig
import co.anitrend.data.core.AniTrendExperimentalFeature
import co.anitrend.data.util.GraphUtil.minify
import com.google.gson.Gson
import co.anitrend.retrofit.graphql.annotation.processor.contract.AbstractGraphProcessor
import co.anitrend.retrofit.graphql.converter.request.GraphRequestConverter
import okhttp3.MediaType.Companion.toMediaType

internal class AniRequestConverter(
    methodAnnotations: Array<out Annotation>,
    processor: AbstractGraphProcessor,
    gson: Gson,
) : GraphRequestConverter(methodAnnotations, processor, gson) {
    /**
     * Resolves the raw GraphQL query string from the method annotations,
     * applying minification in release builds for smaller payloads.
     *
     * @return The resolved query string, or null if no query is found.
     * @see GraphRequestConverter.resolveQuery
     */
    @OptIn(AniTrendExperimentalFeature::class)
    override fun resolveQuery(): String? {
        // Use parent's resolution (registry-first, then asset fallback),
        // then apply minification in release builds
        return super
            .resolveQuery()
            ?.minify(!BuildConfig.DEBUG)
    }

    companion object {
        internal val JSON_MIME_TYPE = "application/json".toMediaType()
        internal val XML_MIME_TYPE = "application/xml".toMediaType()
    }
}
