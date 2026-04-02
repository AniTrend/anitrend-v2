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
package co.anitrend.data.edge.theme.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.theme.EdgeThemeEmbedded
import co.anitrend.data.edge.theme.entity.EdgeThemeEntity
import co.anitrend.data.edge.theme.model.EdgeThemeModel

/**
 * Converts a (mediaId, ThemeModel) pair to [EdgeThemeEntity].
 */
internal class EdgeThemeConverter : SupportConverter<EdgeThemeEmbedded, EdgeThemeEntity>() {
    override val fromType: (EdgeThemeEmbedded) -> EdgeThemeEntity = { pair ->
        val (mediaId, model) = pair
        EdgeThemeEntity(
            mediaId = mediaId,
            themeId = model.id.orEmpty(),
            name = model.name.orEmpty(),
            audio = model.audio,
            video = model.video.orEmpty(),
            meta =
                EdgeThemeEntity.ThemeMeta(
                    number = model.meta?.number ?: 0,
                    type = model.meta?.type.orEmpty(),
                    version = model.meta?.version ?: 0,
                ),
        )
    }
    override val toType: (EdgeThemeEntity) -> EdgeThemeEmbedded = { throw NotImplementedError() }
}
