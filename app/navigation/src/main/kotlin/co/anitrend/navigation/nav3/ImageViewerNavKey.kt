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
package co.anitrend.navigation.nav3

import kotlinx.serialization.Serializable

/**
 * Nav3 key for the image viewer screen.
 *
 * Carries a stable list of image URLs and an initial page index.
 * URLs are kept compact — do not pass large base64 blobs through this key.
 */
@Serializable
data class ImageViewerNavKey(
    val imageSources: List<String>,
    val initialIndex: Int = 0,
) : AniTrendNavKey
