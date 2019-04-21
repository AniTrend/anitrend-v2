package co.anitrend.core.dao.query

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.core.model.response.general.media.MediaTag
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface MediaTagDao: ISupportQuery<MediaTag?> {

    @Query("select count(id) from MediaTag")
    fun count(): Int

    @Query("select * from MediaTag")
    fun findAll(): List<MediaTag>?
}