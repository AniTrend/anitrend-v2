/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.theme.model.serializer

import co.anitrend.data.edge.theme.model.EdgeThemeModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject

internal object EdgeThemeListSerializer : KSerializer<List<EdgeThemeModel>> {
    private val delegate = ListSerializer(EdgeThemeModel.serializer())

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(
        encoder: Encoder,
        value: List<EdgeThemeModel>,
    ) = delegate.serialize(encoder, value)

    override fun deserialize(decoder: Decoder): List<EdgeThemeModel> {
        val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("EdgeThemeListSerializer only supports JSON")
        return parse(
            json = jsonDecoder.json,
            payload = jsonDecoder.decodeJsonElement(),
        )
    }

    private fun parse(
        json: kotlinx.serialization.json.Json,
        payload: JsonElement,
    ): List<EdgeThemeModel> =
        when (payload) {
            JsonNull -> emptyList()
            is JsonArray -> payload.flatMap { parse(json, it) }
            is JsonObject ->
                runCatching {
                    json.decodeFromJsonElement(EdgeThemeModel.serializer(), payload)
                }.getOrNull()?.let(::listOf) ?: payload.values.flatMap { parse(json, it) }
            else -> emptyList()
        }
}
