package co.anitrend.core.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.anitrend.core.BuildConfig
import co.anitrend.core.R
import co.anitrend.core.auth.model.JsonWebToken
import co.anitrend.core.dao.migration.MIGRATION_1_2
import co.anitrend.core.dao.query.JsonWebTokenDao
import co.anitrend.core.dao.query.MediaGenreDao
import co.anitrend.core.dao.query.MediaTagDao
import co.anitrend.core.model.response.general.media.MediaGenre
import co.anitrend.core.model.response.general.media.MediaTag
import io.wax911.support.core.factory.InstanceCreator

@Database(
    entities = [
        JsonWebToken::class, MediaTag::class, MediaGenre::class
    ],
    version = BuildConfig.DATABASE_SCHEMA_VERSION
)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun jsonWebTokenDao(): JsonWebTokenDao
    abstract fun mediaTagDao(): MediaTagDao
    abstract fun mediaGenreDao(): MediaGenreDao

    companion object : InstanceCreator<DatabaseHelper, Context>({ applicationContext ->
        Room.databaseBuilder(
            applicationContext,
            DatabaseHelper::class.java,
            applicationContext.getString(R.string.app_name)
        )
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    })
}