plugins {
    id("co.anitrend.plugin")
    id("kotlinx-serialization")
}


dependencies {
    implementation(libs.threeTenBp)

    implementation(libs.anitrend.retrofit.graphql)

    // Holding off on using xml util for now, seems not to work and probably needs more testing
    implementation(libs.devrieze.xmlutil.android.serialization)
    implementation(libs.jetbrains.kotlinx.serialization.json)

    implementation(libs.androidx.collection.ktx)

    implementation(libs.anitrend.querybuilder.annotation)
    implementation(libs.anitrend.querybuilder.core)
    implementation(libs.anitrend.querybuilder.core.ext)
}

android {
    namespace = "co.anitrend.data.edge"
}
