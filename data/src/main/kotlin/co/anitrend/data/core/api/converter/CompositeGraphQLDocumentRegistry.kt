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
package co.anitrend.data.core.api.converter

import co.anitrend.retrofit.graphql.model.GraphQLDocumentRegistry

/**
 * Combines two [GraphQLDocumentRegistry] instances into one.
 *
 * Resolution order:
 * 1. [primary] is checked first
 * 2. [fallback] is checked if [primary] returns null
 *
 * Used to combine AniList and Edge codegen registries into a single
 * composite registry for the shared [co.anitrend.retrofit.graphql.converter.GraphQLConverterFactory].
 */
internal class CompositeGraphQLDocumentRegistry(
    private val primary: GraphQLDocumentRegistry,
    private val fallback: GraphQLDocumentRegistry,
) : GraphQLDocumentRegistry {
    override fun document(operationName: String): String? = primary.document(operationName) ?: fallback.document(operationName)

    override fun hash(operationName: String): String? = primary.hash(operationName) ?: fallback.hash(operationName)
}
