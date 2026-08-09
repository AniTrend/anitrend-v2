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
package co.anitrend.data.edge.config.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.config.entity.EdgeConfigImageEntity
import co.anitrend.data.edge.config.entity.EdgeConfigSettingsEntity
import co.anitrend.data.edge.config.entity.view.EdgeConfigViewEntity
import co.anitrend.data.edge.graphql.GetConfigData
import co.anitrend.domain.config.entity.Config

internal class EdgeConfigModelConverter(
    override val fromType: (GetConfigData) -> EdgeConfigEntity = { model ->
        val config =
            model.config
                ?: throw IllegalStateException("Config payload did not contain a config root")
        EdgeConfigEntity(
            settings =
                EdgeConfigSettingsEntity(
                    analyticsEnabled = config.settings.analyticsEnabled,
                    platformSource = config.settings.platformSource,
                ),
            image =
                EdgeConfigImageEntity(
                    banner = config.image.banner,
                    poster = config.image.poster,
                    loading = config.image.loading,
                    error = config.image.error,
                    info = config.image.info,
                    standard = config.image.default,
                ),
        )
    },
    override val toType: (EdgeConfigEntity) -> GetConfigData = { throw NotImplementedError() },
) : SupportConverter<GetConfigData, EdgeConfigEntity>()

internal class EdgeConfigEntityConverter(
    override val fromType: (EdgeConfigEntity) -> Config = {
        Config(
            genres = emptyList(),
            navigation = emptyList(),
            settings =
                Config.Settings(
                    analyticsEnabled = it.settings.analyticsEnabled,
                    platformSource = it.settings.platformSource,
                ),
            image =
                Config.DefaultImage(
                    banner = it.image.banner,
                    poster = it.image.poster,
                    loading = it.image.loading,
                    error = it.image.error,
                    info = it.image.info,
                    default = it.image.standard,
                ),
        )
    },
    override val toType: (Config) -> EdgeConfigEntity = { throw NotImplementedError() },
) : SupportConverter<EdgeConfigEntity, Config>()

internal class EdgeConfigViewEntityConverter(
    override val fromType: (EdgeConfigViewEntity) -> Config = {
        Config(
            genres =
                it.genres.map { genre ->
                    Config.Genre(
                        name = genre.name,
                        mediaId = genre.id,
                    )
                },
            navigation =
                it.navigation.map { navigation ->
                    Config.Navigation(
                        id = navigation.id,
                        criteria = navigation.criteria,
                        destination = navigation.destination,
                        i18n = navigation.i18n,
                        icon = navigation.icon,
                        group =
                            Config.Navigation.Group(
                                authenticated = navigation.group.authenticated,
                                i18n = navigation.group.i18n,
                            ),
                    )
                },
            settings =
                Config.Settings(
                    analyticsEnabled = it.config.settings.analyticsEnabled,
                    platformSource = it.config.settings.platformSource,
                ),
            image =
                Config.DefaultImage(
                    banner = it.config.image.banner,
                    poster = it.config.image.poster,
                    loading = it.config.image.loading,
                    error = it.config.image.error,
                    info = it.config.image.info,
                    default = it.config.image.standard,
                ),
        )
    },
    override val toType: (Config) -> EdgeConfigViewEntity = { throw NotImplementedError() },
) : SupportConverter<EdgeConfigViewEntity, Config>()
