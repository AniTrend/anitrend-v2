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
import co.anitrend.data.media.model.MediaModelExtended
import com.google.gson.GsonBuilder
import org.threeten.bp.Instant
import kotlin.reflect.typeOf

internal class TypeConverterObject {

    @TypeConverter fun fromListString(value: List<String>) = value.toCommaSeparatedValues()
    @TypeConverter fun toListString(value: String): List<String> = value.fromCommaSeparatedValues()

    @TypeConverter fun fromListLong(value: List<Long>) = value.toCommaSeparatedValues()
    @TypeConverter fun toListLong(value: String): List<Long> = value.fromCommaSeparatedValues().map { it.toLong() }

    @TypeConverter fun fromInstant(date: Instant?) = date?.toEpochMilli()
    @TypeConverter fun toInstant(value: Long?) = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter fun fromCharSequence(value: CharSequence?) = value?.toString()
    @TypeConverter fun toCharSequence(value: String?) = value as? CharSequence

    @TypeConverter fun fromMediaCover(value: MediaModelExtended.CoverImage?) = value?.let { gson.toJson(value) }
    @OptIn(ExperimentalStdlibApi::class)
    @TypeConverter fun toMediaCover(value: String?) = value?.let {
        val type = typeOf<MediaModelExtended.CoverImage>().javaClass
        gson.fromJson<MediaModelExtended.CoverImage>(it, type)
    }

    @TypeConverter fun fromMediaTitle(value: MediaModelExtended.Title?) = value?.let { gson.toJson(value) }
    @OptIn(ExperimentalStdlibApi::class)
    @TypeConverter fun toMediaTitle(value: String?) = value?.let {
        val type = typeOf<MediaModelExtended.Title>().javaClass
        gson.fromJson<MediaModelExtended.Title>(it, type)
    }

    companion object {
        internal val gson = GsonBuilder()
            .setLenient()
            .create()
    }
}