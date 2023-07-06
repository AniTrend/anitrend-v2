package co.anitrend.data.edge.home.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_home",
    primaryKeys = ["id"],
)
@EntitySchema
data class EdgeHomeEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "id") override val id: Long
) : Identity
