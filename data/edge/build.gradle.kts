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
import co.anitrend.retrofit.graphql.codegen.config.SerializationBackend

plugins {
    id("co.anitrend.plugin")
    id("kotlinx-serialization")
    alias(libs.plugins.retrofit.graphql.codegen)
}


dependencies {
    implementation(libs.threeTenBp)

    implementation(libs.anitrend.retrofit.graphql.runtime)
    implementation(libs.anitrend.retrofit.graphql.api)

    implementation(libs.jetbrains.kotlinx.serialization.json)

    implementation(libs.androidx.collection.ktx)

    implementation(libs.anitrend.querybuilder.annotation)
    implementation(libs.anitrend.querybuilder.core)
    implementation(libs.anitrend.querybuilder.core.ext)

    // Needed for database store (IAniTrendStore) & controller infrastructure
    // Using direct project path to avoid visibility issue with internal Modules object
    implementation(project(":data:android"))

    testImplementation(kotlin("test-junit5"))
}

android {
    buildFeatures.buildConfig = true
    namespace = "co.anitrend.data.edge"
}

    retrofitGraphQL {
        common {
            // Codegen surface for the edge schema: operation constants, documents,
            // hashes, typed variables (with input objects and enums), and response
            // models are all generated.
            generateOperationConstants.set(true)
            generateDocuments.set(true)
            generateHashes.set(true)
            generateVariables.set(true)
            generateResponses.set(true)
            // Request bodies are serialized through KotlinxGraphQLTransportCodec, so
            // generated variables, input objects, enums, and response models need
            // kotlinx metadata.
            serializationBackend.set(SerializationBackend.KOTLINX)
        }
    packageName.set("co.anitrend.data.edge.graphql")
    schema.set(file("../anitrend.schema.graphql"))
    operations.from(fileTree("src/main/graphql") {
        include("**/*.graphql")
    })
    scalars {
        map("JSON", "kotlin.String")
        map("NonEmptyString", "kotlin.String")
        map("NonNegativeFloat", "kotlin.Double")
        map("NonNegativeInt", "kotlin.Int")
        map("ObjMap", "kotlin.String")
        map("PositiveFloat", "kotlin.Double")
        map("PositiveInt", "kotlin.Int")
        map("URL", "kotlin.String")
        map("_DirectiveExtensions", "kotlin.String")
        map("NewsFeedLocale", "kotlin.String")
    }
}
