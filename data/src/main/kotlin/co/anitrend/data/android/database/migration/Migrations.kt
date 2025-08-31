@file:Suppress("ClassName")

package co.anitrend.data.android.database.migration

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

@DeleteTable.Entries(
    DeleteTable(tableName = "relation"),
    DeleteTable(tableName = "jikan"),
)
internal class AUTO_MIGRATION_10_11 : AutoMigrationSpec
