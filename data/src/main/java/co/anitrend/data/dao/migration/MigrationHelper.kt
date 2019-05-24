package co.anitrend.data.dao.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 by lazy {
    object : Migration(1, 2) {
        /**
         * Should run the necessary migrations.
         *
         * This class cannot access any generated Dao in this method.
         *
         * This method is already called inside a transaction and that transaction might actually be a
         * composite transaction of all necessary `Migration`s.
         *
         * @param database The database instance
         */
        override fun migrate(database: SupportSQLiteDatabase) {
            database.apply {

            }
        }
    }
}