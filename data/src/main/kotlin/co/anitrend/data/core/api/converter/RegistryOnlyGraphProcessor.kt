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

import co.anitrend.retrofit.graphql.annotation.processor.contract.AbstractGraphProcessor
import co.anitrend.retrofit.graphql.annotation.processor.fragment.FragmentPatcher
import co.anitrend.retrofit.graphql.annotation.GraphQuery
import co.anitrend.retrofit.graphql.logger.DefaultGraphLogger
import co.anitrend.retrofit.graphql.logger.contract.ILogger
import co.anitrend.retrofit.graphql.logger.core.AbstractLogger

/**
 * Registry-only graph processor used after codegen migration.
 *
 * Any attempt to fall back to asset discovery indicates the migration is incomplete
 * or a generated registry entry is missing for the requested operation.
 */
internal class RegistryOnlyGraphProcessor : AbstractGraphProcessor() {
    override val defaultExtension: String = ".graphql"

    override val defaultDirectory: String = "graphql"

    override val logger: AbstractLogger = DefaultGraphLogger(ILogger.Level.NONE)

    override val fragmentPatcher: FragmentPatcher = FragmentPatcher(defaultExtension, logger = logger)

    override val graphFiles: Map<String, String> = emptyMap()

    override fun getQuery(annotations: Array<out Annotation>): String? {
        val legacyAsset =
            annotations
                .filterIsInstance<GraphQuery>()
                .firstOrNull()
                ?.value
                ?.takeIf(String::isNotBlank)

        check(legacyAsset == null) {
            "GraphQL asset fallback is disabled. Missing generated registry entry for asset path: $legacyAsset"
        }

        return null
    }

    override fun patchQueries() = Unit
}
