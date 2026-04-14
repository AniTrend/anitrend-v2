/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.studio.converter

import co.anitrend.data.edge.network.entity.EdgeNetworkEntity
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.studio.entity.Studio
import kotlin.math.min

internal class MediaStudioEntryEnricher {
    fun enrich(
        entries: List<MediaStudioEntry>,
        networks: List<EdgeNetworkEntity>,
    ): List<MediaStudioEntry> {
        if (entries.isEmpty() || networks.isEmpty()) {
            return entries
        }

        val availableNetworks = networks.toMutableList()

        return entries.map { entry ->
            val bestMatch =
                availableNetworks
                    .asSequence()
                    .map { network -> network to similarity(entry.studio.name, network.name) }
                    .filter { (_, score) -> score >= MATCH_THRESHOLD }
                    .sortedWith(
                        compareByDescending<Pair<EdgeNetworkEntity, Float>> { it.second }
                            .thenByDescending { it.first.isPrimary }
                            .thenBy { normalizedDistance(entry.studio.name, it.first.name) }
                            .thenBy(String.CASE_INSENSITIVE_ORDER) { it.first.name },
                    ).firstOrNull()

            if (bestMatch == null) {
                entry
            } else {
                availableNetworks.remove(bestMatch.first)
                val resolvedLogoPath = resolveLogoPath(bestMatch.first.logoPath)
                entry.copy(
                    studio = entry.studio.withImage(resolvedLogoPath),
                    networkMatch =
                        MediaStudioEntry.StudioNetworkMatch(
                            networkId = bestMatch.first.networkId,
                            name = bestMatch.first.name,
                            category = bestMatch.first.category,
                            originCountry = bestMatch.first.originCountry,
                            logoPath = resolvedLogoPath,
                            isPrimary = bestMatch.first.isPrimary,
                            similarity = bestMatch.second,
                        ),
                )
            }
        }
    }

    private fun similarity(
        studioName: String,
        networkName: String,
    ): Float {
        val normalizedStudioName = normalize(studioName)
        val normalizedNetworkName = normalize(networkName)

        if (normalizedStudioName.isBlank() || normalizedNetworkName.isBlank()) {
            return 0f
        }

        if (normalizedStudioName == normalizedNetworkName) {
            return 1f
        }

        val maxLength = maxOf(normalizedStudioName.length, normalizedNetworkName.length)
        val distance = levenshtein(normalizedStudioName, normalizedNetworkName)

        return (1f - distance.toFloat() / maxLength.toFloat()).coerceIn(0f, 1f)
    }

    private fun normalizedDistance(
        studioName: String,
        networkName: String,
    ): Int =
        levenshtein(
            normalize(studioName),
            normalize(networkName),
        )

    private fun normalize(value: String): String =
        buildString(value.length) {
            value.forEach { character ->
                if (character.isLetterOrDigit()) {
                    append(character.lowercaseChar())
                }
            }
        }

    private fun levenshtein(
        left: String,
        right: String,
    ): Int {
        if (left == right) {
            return 0
        }
        if (left.isEmpty()) {
            return right.length
        }
        if (right.isEmpty()) {
            return left.length
        }

        var previous = IntArray(right.length + 1) { it }
        var current = IntArray(right.length + 1)

        left.forEachIndexed { leftIndex, leftCharacter ->
            current[0] = leftIndex + 1

            right.forEachIndexed { rightIndex, rightCharacter ->
                val substitutionCost = if (leftCharacter == rightCharacter) 0 else 1
                current[rightIndex + 1] =
                    min(
                        min(
                            current[rightIndex] + 1,
                            previous[rightIndex + 1] + 1,
                        ),
                        previous[rightIndex] + substitutionCost,
                    )
            }

            val swap = previous
            previous = current
            current = swap
        }

        return previous[right.length]
    }

    private fun resolveLogoPath(value: String?): String? =
        when {
            value.isNullOrBlank() -> null
            value.startsWith(HTTP_SCHEME) || value.startsWith(HTTPS_SCHEME) -> value
            value.startsWith(PATH_PREFIX) -> "$TMDB_IMAGE_BASE_URL$value"
            else -> null
        }

    private fun Studio.withImage(logoPath: String?): Studio {
        val coverImage = logoPath?.let { CoverImage(large = it, medium = it) } ?: image

        return when (this) {
            is Studio.Core -> copy(image = coverImage)
            is Studio.Extended -> copy(image = coverImage)
        }
    }

    private companion object {
        const val MATCH_THRESHOLD = 0.95f
        const val HTTP_SCHEME = "http://"
        const val HTTPS_SCHEME = "https://"
        const val PATH_PREFIX = "/"
        const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"
    }
}
