package co.anitrend.data.dao.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.model.response.general.media.MediaTag
import io.wax911.support.data.dao.ISupportQuery

@Dao
interface MediaTagDao: ISupportQuery<MediaTag> {

    @Query("select count(id) from MediaTag")
    suspend fun count(): Int

    @Query("select * from MediaTag")
    suspend fun findAll(): List<MediaTag>?

    @Query("select * from MediaTag")
    fun findAllLiveData(): LiveData<List<MediaTag>?>

    @Query("delete from MediaTag")
    suspend fun deleteAll()
}