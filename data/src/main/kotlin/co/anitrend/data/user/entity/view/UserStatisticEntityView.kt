/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.user.entity.view

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.staff.entity.StaffEntity
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.user.entity.statistic.*

internal data class UserStatisticStaffEntityView(
    @Embedded val connection: UserStatisticStaffEntity,
    @Relation(parentColumn = "staff_id", entityColumn = "id") val staff: StaffEntity?,
)

internal data class UserStatisticStudioEntityView(
    @Embedded val connection: UserStatisticStudioEntity,
    @Relation(parentColumn = "studio_id", entityColumn = "id") val studio: StudioEntity?,
)

internal data class UserStatisticTagEntityView(
    @Embedded val connection: UserStatisticTagEntity,
    @Relation(parentColumn = "tag_id", entityColumn = "id") val tag: TagEntity?,
)

internal data class UserStatisticVoiceActorEntityView(
    @Embedded val connection: UserStatisticVoiceActorEntity,
    @Relation(parentColumn = "staff_id", entityColumn = "id") val voiceActor: StaffEntity?,
)

internal data class UserStatisticEntityView(
    @Embedded val statistic: UserWithStatisticEntity,
    @Relation(parentColumn = "user_id", entityColumn = "user_id") val countries: List<UserStatisticCountryEntity> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id") val formats: List<UserStatisticFormatEntity> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id") val genres: List<UserStatisticGenreEntity> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id") val lengths: List<UserStatisticLengthEntity> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id") val releaseYears: List<UserStatisticReleaseYearEntity> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id") val scores: List<UserStatisticScoreEntity> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id") val startYears: List<UserStatisticStartYearEntity> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id") val statuses: List<UserStatisticStatusEntity> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id", entity = UserStatisticStaffEntity::class) val staff:
        List<UserStatisticStaffEntityView> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id", entity = UserStatisticStudioEntity::class) val studios:
        List<UserStatisticStudioEntityView> = emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id", entity = UserStatisticTagEntity::class) val tags: List<UserStatisticTagEntityView> =
        emptyList(),
    @Relation(parentColumn = "user_id", entityColumn = "user_id", entity = UserStatisticVoiceActorEntity::class) val voiceActors:
        List<UserStatisticVoiceActorEntityView> = emptyList(),
)
