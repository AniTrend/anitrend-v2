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

package co.anitrend.data.android.database.converter

import androidx.room.TypeConverter
import co.anitrend.data.android.database.extensions.fromCommaSeparatedValues
import co.anitrend.data.android.database.extensions.toCommaSeparatedValues
import co.anitrend.data.core.extensions.koinOf
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.model.statistics.UserStatisticModel
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.threeten.bp.Instant

internal class TypeConverterObject {

    @TypeConverter fun fromListString(value: List<String>) = value.toCommaSeparatedValues()
    @TypeConverter fun toListString(value: String): List<String> = value.fromCommaSeparatedValues()

    @TypeConverter fun fromListLong(value: List<Long>) = value.toCommaSeparatedValues()
    @TypeConverter fun toListLong(value: String): List<Long> =
        value.fromCommaSeparatedValues().map(String::toLong)

    @TypeConverter fun fromListFloat(value: List<Float>) = value.toCommaSeparatedValues()
    @TypeConverter fun toListFloat(value: String): List<Float> =
        value.fromCommaSeparatedValues().map(String::toFloat)

    @TypeConverter fun fromInstant(date: Instant?) = date?.toEpochMilli()
    @TypeConverter fun toInstant(value: Long?) = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter fun fromCharSequence(value: CharSequence?) = value?.toString()
    @TypeConverter fun toCharSequence(value: String?) = value as? CharSequence

    @TypeConverter fun fromUserNotificationSettingList(
        value: List<UserGeneralOptionEntity.NotificationOption>
    ): String {
        val serializer = ListSerializer(
            UserGeneralOptionEntity.NotificationOption.serializer()
        )
        return koinOf<Json>().encodeToString(serializer, value)
    }
    @TypeConverter fun toUserNotificationSettingList(
        value: String
    ): List<UserGeneralOptionEntity.NotificationOption> {
        val serializer = ListSerializer(
            UserGeneralOptionEntity.NotificationOption.serializer()
        )
        return koinOf<Json>().decodeFromString(serializer, value)
    }

    @TypeConverter fun fromStatisticAnime(value: UserStatisticModel.Anime): String {
        val serializer = UserStatisticModel.Anime.serializer()
        return koinOf<Json>().encodeToString(serializer, value)
    }

    @TypeConverter fun toStatisticAnime(value: String): UserStatisticModel.Anime {
        val serializer = UserStatisticModel.Anime.serializer()
        return koinOf<Json>().decodeFromString(serializer, value)
    }

    @TypeConverter fun fromStatisticManga(value: UserStatisticModel.Manga): String {
        val serializer = UserStatisticModel.Manga.serializer()
        return koinOf<Json>().encodeToString(serializer, value)
    }

    @TypeConverter fun toStatisticManga(value: String): UserStatisticModel.Manga {
        val serializer = UserStatisticModel.Manga.serializer()
        return koinOf<Json>().decodeFromString(serializer, value)
    }
}