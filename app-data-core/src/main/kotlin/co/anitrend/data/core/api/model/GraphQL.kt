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

package co.anitrend.data.core.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName

/**
 * Location describing which part of GraphQL document caused an exception.
 *
 * @see [GraphQL Specification](http://spec.graphql.org/June2018/#sec-Errors) for additional details
 */
@Serializable
data class SourceLocation(
    @SerialName("line") val line: Int,
    @SerialName("column") val column: Int
)

/**
 * GraphQL error representation that is spec complaint
 *
 * @param message Description of the error.
 * @param path Path of the the response field that encountered the error, as segments that
 * represent fields should be strings, and path segments that represent list indices
 * should be 0‐indexed integers. If the error happens in an aliased field, the path to the
 * error should use the aliased name, since it represents a path in the response, not in the query.
 * @param locations List of locations within the GraphQL document at which the exception occurred.
 * @param extensions Additional information about the error.
 */
@Serializable
data class GraphQLError(
    @SerialName("message") val message: String,
    @SerialName("locations") val locations: List<SourceLocation>? = null,
    @SerialName("path") val path: List<@Contextual Any>? = null,
    @SerialName("extensions") val extensions: Map<String, @Contextual Any?>? = null
)

/**
 * GraphQL response that is spec complaint with serialization and deserialization.
 *
 * @see [GraphQL Specification](http://spec.graphql.org/June2018/#sec-Data) for additional details
 */
@Serializable
data class GraphQLResponse<T>(
    @SerialName("data") val data: T? = null,
    @SerialName("errors") val errors: List<GraphQLError>? = null,
    @SerialName("extensions") val extensions: Map<@Contextual Any, @Contextual Any>? = null
)
