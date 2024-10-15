package co.anitrend.data.edge.config.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.config.entity.view.EdgeConfigViewEntity
import co.anitrend.data.edge.config.model.remote.EdgeConfigModel
import co.anitrend.domain.config.entity.Config

internal class EdgeConfigModelConverter(
    override val fromType: (EdgeConfigModel) -> EdgeConfigEntity = {
        EdgeConfigEntity(
            settings = EdgeConfigEntity.Settings(
                analyticsEnabled= it.settings.analyticsEnabled,
                platformSource = it.settings.platformSource,
            ),
            image = EdgeConfigEntity.Image(
                banner = it.image.banner,
                poster = it.image.poster,
                loading = it.image.loading,
                error = it.image.error,
                info = it.image.info,
                standard = it.image.default,
            ),
        )
    },
    override val toType: (EdgeConfigEntity) -> EdgeConfigModel = { throw NotImplementedError() }
) : SupportConverter<EdgeConfigModel, EdgeConfigEntity>()

internal class EdgeConfigEntityConverter(
    override val fromType: (EdgeConfigEntity) -> Config = {
        Config(
            genres = emptyList(),
            navigation = emptyList(),
            settings = Config.Settings(
                analyticsEnabled = it.settings.analyticsEnabled,
                platformSource = it.settings.platformSource,
            ),
            image = Config.DefaultImage(
                banner = it.image.banner,
                poster = it.image.poster,
                loading = it.image.loading,
                error = it.image.error,
                info = it.image.info,
                default = it.image.standard,
            ),
        )
    },
    override val toType: (Config) -> EdgeConfigEntity = { throw NotImplementedError() }
) : SupportConverter<EdgeConfigEntity, Config>()

internal class EdgeConfigViewEntityConverter(
    override val fromType: (EdgeConfigViewEntity) -> Config = {
        Config(
            genres = it.genres.map { genre ->
                Config.Genre(
                    name = genre.name,
                    mediaId = genre.id
                )
            },
            navigation = it.navigation.map { navigation ->
                Config.Navigation(
                    id = navigation.id,
                    criteria = navigation.criteria,
                    destination = navigation.destination,
                    i18n = navigation.i18n,
                    icon = navigation.icon,
                    group = Config.Navigation.Group(
                        authenticated = navigation.group.authenticated,
                        i18n = navigation.group.i18n,
                    ),
                )
            },
            settings = Config.Settings(
                analyticsEnabled = it.config.settings.analyticsEnabled,
                platformSource = it.config.settings.platformSource,
            ),
            image = Config.DefaultImage(
                banner = it.config.image.banner,
                poster = it.config.image.poster,
                loading = it.config.image.loading,
                error = it.config.image.error,
                info = it.config.image.info,
                default = it.config.image.standard,
            ),
        )
    },
    override val toType: (Config) -> EdgeConfigViewEntity = { throw NotImplementedError() }
) : SupportConverter<EdgeConfigViewEntity, Config>()
