package co.anitrend.data.edge.navigation.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity
import co.anitrend.data.edge.navigation.model.EdgeNavigationModel


internal class EdgeNavigationModelConverter(
    override val fromType: (EdgeNavigationModel) -> EdgeNavigationEntity = {
        EdgeNavigationEntity(
            id = it.id,
            destination = it.destination,
            label = it.label,
            icon = it.icon,
            group = EdgeNavigationEntity.Group(
                authenticated = it.group.authenticated,
                label = it.group.label,
                id = it.group.id,
            ),
        )
    },
    override val toType: (EdgeNavigationEntity) -> EdgeNavigationModel = { throw NotImplementedError() }
) : SupportConverter<EdgeNavigationModel, EdgeNavigationEntity>()

