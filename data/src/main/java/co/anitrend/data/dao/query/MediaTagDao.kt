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

    @Query("select * from MediaTag where id = :id order by name asc")
    suspend fun find(id: Long): List<MediaTag>?

    @Query("select * from MediaTag order by name asc")
    suspend fun findAll(): List<MediaTag>?

    @Query("delete from MediaTag")
    suspend fun deleteAll()

    @Query("select * from MediaTag where id = :id order by name asc")
    fun findLiveData(id: Long): LiveData<List<MediaTag>>?

    @Query("select * from MediaTag order by name asc")
    fun findAllLiveData(): LiveData<List<MediaTag>?>
}