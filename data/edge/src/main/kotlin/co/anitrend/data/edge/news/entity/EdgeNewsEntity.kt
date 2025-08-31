package co.anitrend.data.edge.news.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_news",
    indices = [Index(value = ["cursor"], unique = true)],
)
@EntitySchema
data class EdgeNewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "cursor") val cursor: String,
    @ColumnInfo(name = "news_id") val newsId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "published_at") val publishedAt: Long?,
    @ColumnInfo(name = "description") val description: String?,
)
