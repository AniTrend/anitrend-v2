package co.anitrend.data.edge.config.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.config.model.remote.EdgeConfigModel
import co.anitrend.domain.config.entity.Config

internal class EdgeConfigModelConverter(
    override val fromType: (EdgeConfigModel) -> EdgeConfigEntity = {
        EdgeConfigEntity(
            settings = EdgeConfigEntity.Settings(
                analyticsEnabled= it.settings.analyticsEnabled,
            ),
            defaultImage = EdgeConfigEntity.Image(
                banner = it.defaultImage.banner,
                poster = it.defaultImage.poster,
                loading = it.defaultImage.loading,
                error = it.defaultImage.error,
                info = it.defaultImage.info,
                standard = it.defaultImage.default,
            ),
            id = it.id,
        )
    },
    override val toType: (EdgeConfigEntity) -> EdgeConfigModel = { throw NotImplementedError() }
) : SupportConverter<EdgeConfigModel, EdgeConfigEntity>()

internal class EdgeConfigEntityConverter(
    override val fromType: (EdgeConfigEntity) -> Config = {
        Config(
            /*genres = it.genres.map { genre ->
                Config.Genre(
                    name = genre.name,
                    mediaId = genre.id
                )
            },
            navigation = it.navigation.map { navigation ->
                Config.Navigation(
                    id = navigation.id,
                    destination = navigation.destination,
                    label = navigation.label,
                    icon = navigation.icon,
                    group = Config.Group(
                        authenticated = navigation.group.authenticated,
                        label = navigation.group.label,
                        id = navigation.group.id,
                    ),
                )
            },*/
            settings = Config.Settings(
                analyticsEnabled = it.settings.analyticsEnabled,
            ),
            defaultImage = Config.DefaultImage(
                banner = it.defaultImage.banner,
                poster = it.defaultImage.poster,
                loading = it.defaultImage.loading,
                error = it.defaultImage.error,
                info = it.defaultImage.info,
                default = it.defaultImage.standard,
            ),
        )
    },
    override val toType: (Config) -> EdgeConfigEntity = { throw NotImplementedError() }
) : SupportConverter<EdgeConfigEntity, Config>()
