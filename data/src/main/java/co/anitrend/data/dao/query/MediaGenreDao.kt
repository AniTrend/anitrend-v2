package co.anitrend.data.dao.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.model.response.general.media.MediaGenre
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface MediaGenreDao: ISupportQuery<MediaGenre?> {

    @Query("select count(genre) from MediaGenre")
    fun count(): Int

    @Query("select * from MediaGenre")
    fun findAll(): List<MediaGenre>?

    @Query("select * from MediaGenre")
    fun findAllLiveData(): LiveData<List<MediaGenre>?>
}