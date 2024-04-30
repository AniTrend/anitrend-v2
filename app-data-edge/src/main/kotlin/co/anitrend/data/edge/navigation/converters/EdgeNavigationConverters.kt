package co.anitrend.data.edge.navigation.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity
import co.anitrend.data.edge.navigation.model.EdgeNavigationModel

internal class EdgeNavigationModelConverter(
    override val fromType: (EdgeNavigationModel) -> EdgeNavigationEntity = {
        EdgeNavigationEntity(
            criteria = it.criteria,
            destination = it.destination,
            i18n = it.i18n,
            icon = it.icon,
            group = EdgeNavigationEntity.Group(
                authenticated = it.group.authenticated,
                i18n = it.group.i18n,
            ),
            configId = EdgeConfigEntity.DEFAULT_ID,
        )
    },
    override val toType: (EdgeNavigationEntity) -> EdgeNavigationModel = { throw NotImplementedError() }
) : SupportConverter<EdgeNavigationModel, EdgeNavigationEntity>()
