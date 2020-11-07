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
import co.anitrend.data.arch.database.extensions.fromCommaSeparatedValues
import co.anitrend.data.arch.database.extensions.toCommaSeparatedValues
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.koin.core.context.GlobalContext
import org.threeten.bp.Instant

internal class TypeConverterObject {

    @TypeConverter fun fromListString(value: List<String>) = value.toCommaSeparatedValues()
    @TypeConverter fun toListString(value: String): List<String> = value.fromCommaSeparatedValues()

    @TypeConverter fun fromListLong(value: List<Long>) = value.toCommaSeparatedValues()
    @TypeConverter fun toListLong(value: String): List<Long> = value.fromCommaSeparatedValues().map { it.toLong() }

    @TypeConverter fun fromInstant(date: Instant?) = date?.toEpochMilli()
    @TypeConverter fun toInstant(value: Long?) = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter fun fromCharSequence(value: CharSequence?) = value?.toString()
    @TypeConverter fun toCharSequence(value: String?) = value as? CharSequence

    @TypeConverter fun fromMediaLink(value: List<MediaEntity.Link>): String {
        val serializer = ListSerializer(
            MediaEntity.Link.serializer()
        )
        return json.encodeToString(serializer, value)
    }
    @TypeConverter fun toMediaLink(value: String): List<MediaEntity.Link> {
        val serializer = ListSerializer(
            MediaEntity.Link.serializer()
        )
        return json.decodeFromString(serializer, value)
    }

    @TypeConverter fun fromMediaRank(value: List<MediaEntity.Rank>): String {
        val serializer = ListSerializer(
            MediaEntity.Rank.serializer()
        )
        return json.encodeToString(serializer, value)
    }
    @TypeConverter fun toMediaRank(value: String): List<MediaEntity.Rank> {
        val serializer = ListSerializer(
            MediaEntity.Rank.serializer()
        )
        return json.decodeFromString(serializer, value)
    }

    @TypeConverter fun fromMediaTagList(value: List<MediaEntity.Tag>): String {
        val serializer = ListSerializer(
            MediaEntity.Tag.serializer()
        )
        return json.encodeToString(serializer, value)
    }
    @TypeConverter fun toMediaTagList(value: String): List<MediaEntity.Tag> {
        val serializer = ListSerializer(
            MediaEntity.Tag.serializer()
        )
        return json.decodeFromString(serializer, value)
    }

    @TypeConverter fun fromMediaGenreList(value: List<MediaEntity.Genre>): String {
        val serializer = ListSerializer(
            MediaEntity.Genre.serializer()
        )
        return json.encodeToString(serializer, value)
    }
    @TypeConverter fun toMediaGenreList(value: String): List<MediaEntity.Genre> {
        val serializer = ListSerializer(
            MediaEntity.Genre.serializer()
        )
        return json.decodeFromString(serializer, value)
    }

    @TypeConverter fun fromUserNotificationSettingList(
        value: List<UserGeneralOptionEntity.NotificationOption>
    ): String {
        val serializer = ListSerializer(
            UserGeneralOptionEntity.NotificationOption.serializer()
        )
        return json.encodeToString(serializer, value)
    }
    @TypeConverter fun toUserNotificationSettingList(
        value: String
    ): List<UserGeneralOptionEntity.NotificationOption> {
        val serializer = ListSerializer(
            UserGeneralOptionEntity.NotificationOption.serializer()
        )
        return json.decodeFromString(serializer, value)
    }

    companion object {
        internal val json by lazy {
            GlobalContext.get().get(Json::class)
        }
    }
}