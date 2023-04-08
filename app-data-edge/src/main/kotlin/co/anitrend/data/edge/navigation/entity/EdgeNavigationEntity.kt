package co.anitrend.data.edge.navigation.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_navigation",
    primaryKeys = ["id"],
)
@EntitySchema
data class EdgeNavigationEntity(
    @ColumnInfo(name = "destination") val destination: String,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "icon") val icon: String,
    @Embedded(prefix = "group_") val group: Group,
    @ColumnInfo(name = "id") override val id: Long,
) : Identity {
    data class Group(
        @ColumnInfo(name = "authenticated") val authenticated: Boolean,
        @ColumnInfo(name = "label") val label: String,
        @ColumnInfo(name = "id") val id: Long,
    )
}
