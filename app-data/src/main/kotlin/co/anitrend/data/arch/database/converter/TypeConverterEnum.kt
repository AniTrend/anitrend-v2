/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.arch.database.converter

import androidx.room.TypeConverter
import co.anitrend.data.arch.database.extensions.fromEnum
import co.anitrend.data.arch.database.extensions.toEnum
import co.anitrend.data.cache.model.CacheRequest
import co.anitrend.domain.media.enums.*
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.staff.enums.StaffLanguage
import co.anitrend.domain.user.enums.UserTitleLanguage

internal class TypeConverterEnum {
    @TypeConverter fun fromMediaFormat(value: MediaFormat?) = value?.fromEnum()
    @TypeConverter fun toMediaFormat(value: String?) = value?.toEnum<MediaFormat>()

    @TypeConverter fun fromMediaSeason(value: MediaSeason?) = value?.fromEnum()
    @TypeConverter fun toMediaSeason(value: String?) = value?.toEnum<MediaSeason>()

    @TypeConverter fun fromMediaSource(value: MediaSource?) = value?.fromEnum()
    @TypeConverter fun toMediaSource(value: String?) = value?.toEnum<MediaSource>()

    @TypeConverter fun fromMediaStatus(value: MediaStatus?) = value?.fromEnum()
    @TypeConverter fun toMediaStatus(value: String?) = value?.toEnum<MediaStatus>()

    @TypeConverter fun fromMediaType(value: MediaType?) = value?.fromEnum()
    @TypeConverter fun toMediaType(value: String?) = value?.toEnum<MediaType>()

    @TypeConverter fun fromMediaRankType(value: MediaRankType?) = value?.fromEnum()
    @TypeConverter fun toMediaRankType(value: String?) = value?.toEnum<MediaRankType>()

    @TypeConverter fun fromCacheRequest(value: CacheRequest?) = value?.fromEnum()
    @TypeConverter fun toCacheRequest(value: String?) = value?.toEnum<CacheRequest>()

    @TypeConverter fun fromScoreFormat(value: ScoreFormat?) = value?.fromEnum()
    @TypeConverter fun toScoreFormat(value: String?) = value?.toEnum<ScoreFormat>()

    @TypeConverter fun fromUserTitleLanguage(value: UserTitleLanguage?) = value?.fromEnum()
    @TypeConverter fun toUserTitleLanguage(value: String?) = value?.toEnum<UserTitleLanguage>()

    @TypeConverter fun fromMediaListStatus(value: MediaListStatus?) = value?.fromEnum()
    @TypeConverter fun toMediaListStatus(value: String?) = value?.toEnum<MediaListStatus>()

    @TypeConverter fun fromStaffLanguage(value: StaffLanguage?) = value?.fromEnum()
    @TypeConverter fun toStaffLanguage(value: String?) = value?.toEnum<StaffLanguage>()
}