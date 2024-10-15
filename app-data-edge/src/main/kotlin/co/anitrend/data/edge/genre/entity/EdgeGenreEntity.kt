package co.anitrend.data.edge.genre.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import co.anitrend.data.core.common.Identity
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_config_genre",
    foreignKeys = [ForeignKey(
        entity = EdgeConfigEntity::class,
        parentColumns = ["id"],
        childColumns = ["config_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
    )],
    primaryKeys = ["id"],
    indices = [
        Index(value = ["config_id", "id"], unique = true),
        Index(value = ["config_id"]),
    ],
)
@EntitySchema
data class EdgeGenreEntity(
    @ColumnInfo(name = "config_id") val configId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "id") override val id: Long,
) : Identity
