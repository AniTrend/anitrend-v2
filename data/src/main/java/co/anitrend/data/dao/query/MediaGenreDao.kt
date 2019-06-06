package co.anitrend.data.dao.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.model.response.general.media.MediaGenre
import io.wax911.support.data.dao.ISupportQuery

@Dao
interface MediaGenreDao: ISupportQuery<MediaGenre> {

    @Query("select count(genre) from MediaGenre")
    suspend fun count(): Int

    @Query("select * from MediaGenre order by genre asc")
    suspend fun findAll(): List<MediaGenre>?

    @Query("select * from MediaGenre order by genre asc")
    fun findAllLiveData(): LiveData<List<MediaGenre>?>

    @Query("delete from MediaGenre")
    suspend fun deleteAll()
}