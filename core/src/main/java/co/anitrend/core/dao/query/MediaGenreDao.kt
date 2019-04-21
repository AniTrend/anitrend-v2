package co.anitrend.core.dao.query

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.core.model.response.general.media.MediaGenre
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface MediaGenreDao: ISupportQuery<MediaGenre?> {

    @Query("select count(genre) from MediaGenre")
    fun count(): Int

    @Query("select * from MediaGenre")
    fun findAll(): List<MediaGenre>?
}