package co.anitrend.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.anitrend.data.BuildConfig
import co.anitrend.data.auth.model.JsonWebToken
import co.anitrend.data.dao.migration.MIGRATION_1_2
import co.anitrend.data.dao.query.JsonWebTokenDao
import co.anitrend.data.dao.query.MediaGenreDao
import co.anitrend.data.dao.query.MediaTagDao
import co.anitrend.data.model.response.general.media.MediaGenre
import co.anitrend.data.model.response.general.media.MediaTag

@Database(
    entities = [
        JsonWebToken::class, MediaTag::class, MediaGenre::class
    ],
    version = BuildConfig.DATABASE_SCHEMA_VERSION
)
abstract class DatabaseHelper: RoomDatabase() {

    abstract fun jsonWebTokenDao(): JsonWebTokenDao
    abstract fun mediaTagDao(): MediaTagDao
    abstract fun mediaGenreDao(): MediaGenreDao

    companion object {

        fun newInstance(applicationContext: Context): DatabaseHelper {
            return Room.databaseBuilder(
                applicationContext,
                DatabaseHelper::class.java,
                "anitrend-data"
            ).fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}
