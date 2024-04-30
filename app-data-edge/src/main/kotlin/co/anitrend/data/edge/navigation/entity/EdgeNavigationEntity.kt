package co.anitrend.data.edge.navigation.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.Identity
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.support.query.builder.annotation.EntitySchema


@Entity(
    tableName = "edge_config_navigation",
    foreignKeys = [
        ForeignKey(
        entity = EdgeConfigEntity::class,
        parentColumns = ["id"],
        childColumns = ["config_id"],
        onDelete = ForeignKey.CASCADE
    )
    ],
    indices = [
        Index(value = ["config_id", "id"], unique = true),
        Index(value = ["config_id"]),
    ],
)
@EntitySchema
data class EdgeNavigationEntity(
    @ColumnInfo(name = "config_id") val configId: Long,
    @ColumnInfo(name = "criteria") val criteria: String,
    @ColumnInfo(name = "destination") val destination: String,
    @Embedded(prefix = "group_") val group: Group,
    @ColumnInfo(name = "i18n") val i18n: String,
    @ColumnInfo(name = "icon") val icon: String,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : Identity {

    data class Group(
        @ColumnInfo(name = "authenticated") val authenticated: Boolean,
        @ColumnInfo(name = "i18n") val i18n: String,
    )
}

