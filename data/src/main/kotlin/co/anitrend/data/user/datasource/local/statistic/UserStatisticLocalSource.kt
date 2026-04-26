/*
 * Copyright (C) 2021 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package co.anitrend.data.user.datasource.local.statistic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.user.entity.statistic.UserStatisticCountryEntity
import co.anitrend.data.user.entity.statistic.UserStatisticFormatEntity
import co.anitrend.data.user.entity.statistic.UserStatisticGenreEntity
import co.anitrend.data.user.entity.statistic.UserStatisticLengthEntity
import co.anitrend.data.user.entity.statistic.UserStatisticReleaseYearEntity
import co.anitrend.data.user.entity.statistic.UserStatisticScoreEntity
import co.anitrend.data.user.entity.statistic.UserStatisticStaffEntity
import co.anitrend.data.user.entity.statistic.UserStatisticStartYearEntity
import co.anitrend.data.user.entity.statistic.UserStatisticStatusEntity
import co.anitrend.data.user.entity.statistic.UserStatisticStudioEntity
import co.anitrend.data.user.entity.statistic.UserStatisticTagEntity
import co.anitrend.data.user.entity.statistic.UserStatisticVoiceActorEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import co.anitrend.data.user.entity.view.UserStatisticEntityView
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class UserStatisticLocalSource : AbstractLocalSource<UserWithStatisticEntity>() {
    @Query(
        """
            select count(id) from user_statistic
        """,
    )
    abstract override suspend fun count(): Int

    @Query(
        """
        delete from user_statistic
        """,
    )
    abstract override suspend fun clear()

    @Query(
        """
        select * from user_statistic where user_id = :userId limit 1
        """,
    )
    @Transaction
    abstract fun entryByUserIdFlow(userId: Long): Flow<UserStatisticEntityView?>

    @Query(
        """
        insert into user_statistic(
            id,
            user_id,
            statistic_anime_count,
            statistic_anime_mean_score,
            statistic_anime_standard_deviation,
            statistic_anime_minutes_watched,
            statistic_anime_episodes_watched,
            statistic_manga_count,
            statistic_manga_mean_score,
            statistic_manga_standard_deviation,
            statistic_manga_chapters_read,
            statistic_manga_volumes_read
        )
        select :userId, :userId, null, null, null, null, null, null, null, null, null, null
        where not exists(
            select 1 from user_statistic
            where user_id = :userId
        )
        """,
    )
    abstract suspend fun ensurePlaceholder(userId: Long)

    @Query("delete from user_statistic_country where user_id = :userId")
    abstract suspend fun clearCountriesByUserId(userId: Long)

    @Query("delete from user_statistic_format where user_id = :userId")
    abstract suspend fun clearFormatsByUserId(userId: Long)

    @Query("delete from user_statistic_genre where user_id = :userId")
    abstract suspend fun clearGenresByUserId(userId: Long)

    @Query("delete from user_statistic_length where user_id = :userId")
    abstract suspend fun clearLengthsByUserId(userId: Long)

    @Query("delete from user_statistic_release_year where user_id = :userId")
    abstract suspend fun clearReleaseYearsByUserId(userId: Long)

    @Query("delete from user_statistic_score where user_id = :userId")
    abstract suspend fun clearScoresByUserId(userId: Long)

    @Query("delete from user_statistic_staff where user_id = :userId")
    abstract suspend fun clearStaffByUserId(userId: Long)

    @Query("delete from user_statistic_start_year where user_id = :userId")
    abstract suspend fun clearStartYearsByUserId(userId: Long)

    @Query("delete from user_statistic_status where user_id = :userId")
    abstract suspend fun clearStatusesByUserId(userId: Long)

    @Query("delete from user_statistic_studio where user_id = :userId")
    abstract suspend fun clearStudiosByUserId(userId: Long)

    @Query("delete from user_statistic_tag where user_id = :userId")
    abstract suspend fun clearTagsByUserId(userId: Long)

    @Query("delete from user_statistic_voice_actor where user_id = :userId")
    abstract suspend fun clearVoiceActorsByUserId(userId: Long)

    @Query("delete from user_statistic where user_id = :userId")
    abstract suspend fun clearParentByUserId(userId: Long)

    @Transaction
    open suspend fun clearByUserId(userId: Long) {
        clearCountriesByUserId(userId)
        clearFormatsByUserId(userId)
        clearGenresByUserId(userId)
        clearLengthsByUserId(userId)
        clearReleaseYearsByUserId(userId)
        clearScoresByUserId(userId)
        clearStaffByUserId(userId)
        clearStartYearsByUserId(userId)
        clearStatusesByUserId(userId)
        clearStudiosByUserId(userId)
        clearTagsByUserId(userId)
        clearVoiceActorsByUserId(userId)
        clearParentByUserId(userId)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertCountries(attribute: List<UserStatisticCountryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertFormats(attribute: List<UserStatisticFormatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertGenres(attribute: List<UserStatisticGenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertLengths(attribute: List<UserStatisticLengthEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertReleaseYears(attribute: List<UserStatisticReleaseYearEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertScores(attribute: List<UserStatisticScoreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertStaff(attribute: List<UserStatisticStaffEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertStartYears(attribute: List<UserStatisticStartYearEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertStatuses(attribute: List<UserStatisticStatusEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertStudios(attribute: List<UserStatisticStudioEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertTags(attribute: List<UserStatisticTagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertVoiceActors(attribute: List<UserStatisticVoiceActorEntity>)
}
