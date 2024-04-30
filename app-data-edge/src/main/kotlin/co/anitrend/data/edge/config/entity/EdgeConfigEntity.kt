package co.anitrend.data.edge.config.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_config",
    primaryKeys = ["id"]
)
@EntitySchema
data class EdgeConfigEntity(
    @Embedded(prefix = "settings_") val settings: Settings,
    @Embedded(prefix = "image_") val image: Image,
    @ColumnInfo(name = "id") override val id: Long = DEFAULT_ID,
) : Identity {

    data class Settings(
        @ColumnInfo(name = "analytics_enabled") val analyticsEnabled: Boolean,
        @ColumnInfo(name = "platform_source") val platformSource: String?,
    )

    data class Image(
        @ColumnInfo(name = "banner") val banner: String,
        @ColumnInfo(name = "poster") val poster: String,
        @ColumnInfo(name = "loading") val loading: String,
        @ColumnInfo(name = "error") val error: String,
        @ColumnInfo(name = "info") val info: String,
        @ColumnInfo(name = "standard") val standard: String,
    )

    companion object {
        const val DEFAULT_ID: Long = 1
    }
}
