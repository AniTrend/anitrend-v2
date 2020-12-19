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

package co.anitrend.buildSrc

import co.anitrend.buildSrc.common.Versions
import co.anitrend.buildSrc.common.*

object Libraries {

    const val liquidSwipe = "com.github.Chrisvin:LiquidSwipe:${Versions.liquidSwipe}"

    const val threeTenBp = "com.jakewharton.threetenabp:threetenabp:${Versions.threeTenBp}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val treessence = "fr.bipi.treessence:treessence:${Versions.treesSence}"
    const val debugDb = "com.amitshekhar.android:debug-db:${Versions.debugDB}"

    const val junit = "junit:junit:${Versions.junit}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"

    const val scalingImageView = "com.davemorrissey.labs:subsampling-scale-image-view-androidx:${Versions.scalingImageView}"

    const val prettyTime = "org.ocpsoft.prettytime:prettytime:${Versions.prettyTime}"

    const val retrofitSerializer = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.serializationConverter}"

    object Android {

        object Tools {
            private const val version = "4.1.1"
            const val buildGradle = "com.android.tools.build:gradle:${version}"
        }
    }

    object AndroidX {

        object Activity {
            private const val version = "1.2.0-beta02"
            const val activity = "androidx.activity:activity:$version"
            const val activityKtx = "androidx.activity:activity-ktx:$version"
        }

        object AppCompat {
            private const val version = "1.3.0-alpha01"
            const val appcompat = "androidx.appcompat:appcompat:$version"
            const val appcompatResources = "androidx.appcompat:appcompat-resources:$version"
        }

        object Browser {
            private const val version = "1.3.0"
            const val browser = "androidx.browser:browser:$version"
        }

        object Collection {
            private const val version = "1.1.0"
            const val collection = "androidx.collection:collection:$version"
            const val collectionKtx = "androidx.collection:collection-ktx:$version"
        }

        object Compose {
            internal const val version = "1.0.0-alpha02"

            object Foundation {
                const val foundation = "androidx.compose.foundation:foundation:$version"
            }

            object Material {
                const val material = "androidx.compose.material:material:$version"

                object Icons {
                    const val core = "androidx.compose.material:material-icons-core:$version"
                    const val extended = "androidx.compose.material:material-icons-extended:$version"
                }
            }

            object Runtime {
                const val liveData = "androidx.compose.runtime:runtime-livedata:$version"
            }

            object Test {
                const val test = "androidx.ui:ui-test:$version"
            }

            object Ui {
                const val ui = "androidx.compose.ui:ui:$version"
                const val tooling = "androidx.ui:ui-tooling:$version"
            }
        }

        object Core {
            private const val version = "1.5.0-alpha05"
            const val core = "androidx.core:core:$version"
            const val coreKtx = "androidx.core:core-ktx:$version"

            object Animation {
                private const val version = "1.0.0-alpha01"
                const val animation = "androidx.core:core-animation:${version}"
                const val animationTest = "androidx.core:core-animation-testing:${version}"
            }
        }

        object ConstraintLayout {
            private const val version = "2.1.0-alpha1"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:$version"
            const val constraintLayoutSolver = "androidx.constraintlayout:constraintlayout-solver:$version"
        }

        object DataStore {
            private const val version = "1.0.0-alpha02"
            const val core = "androidx.datastore:datastore-core:$version"
            const val preferences = "androidx.datastore:datastore-preferences:$version"
        }

        object Emoji {
            private const val version = "1.1.0-rc01"
            const val appCompat = "androidx.emoji:emoji-appcompat:$version"
        }

        object Fragment {
            private const val version = "1.3.0-beta02"
            const val fragment = "androidx.fragment:fragment:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
            const val test = "androidx.fragment:fragment-ktx:fragment-testing$version"
        }

        object Lifecycle {
            private const val version = "2.3.0-beta01"
            // TODO: Upgrade to 2.3.0-* when it is available for extensions
            const val extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
            const val runTimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveDataCoreKtx = "androidx.lifecycle:lifecycle-livedata-core-ktx:$version"
        }

        @Deprecated("Use Media2 instead")
        object Media {
            private const val version = "1.1.0"
            const val media = "androidx.media:media:$version"
        }

        object Media2 {
            private const val version = "1.0.0-alpha04"
            const val media2 = "androidx.media2:media2:$version"

            object Common {
                private const val version = "1.1.0-alpha01"
                const val common = "androidx.media2:media2-common:${version}"
            }

            object ExoPlayer {
                private const val version = "1.1.0-alpha01"
                const val exoPlayer = "androidx.media2:media2-exoplayer:${version}"
            }

            object Player {
                private const val version = "1.1.0-alpha01"
                const val player = "androidx.media2:media2-player:${version}"
            }

            object Session {
                private const val version = "1.1.0-alpha01"
                const val session = "androidx.media2:media2-session:${version}"
            }

            object Widget {
                private const val version = "1.1.0-alpha01"
                const val widget = "androidx.media2:media2-widget:${version}"
            }
        }

        object Navigation {
            private const val version = "2.3.0"
            const val common = "androidx.navigation:navigation-common:$version"
            const val commonKtx = "androidx.navigation:navigation-common-ktx:$version"

            const val dynamicFragment = "androidx.navigation:navigation-dynamic-features-fragment:$version"
            const val dynamicRuntime = "androidx.navigation:navigation-dynamic-features-runtime:$version"

            const val fragment = "androidx.navigation:navigation-fragment:$version"
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"

            const val runtime = "androidx.navigation:navigation-runtime:$version"
            const val runtimeKtx = "androidx.navigation:navigation-runtime-ktx:$version"

            const val safeArgsGenerator = "androidx.navigation:navigation-safe-args-generator:$version"
            const val safeArgsGradlePlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"

            const val test = "androidx.navigation:navigation-testing:$version"

            const val ui = "androidx.navigation:navigation-ui:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
        }

        object Paging {
            private const val version = "2.1.2"
            const val common = "androidx.paging:paging-common-ktx:$version"
            const val runtime = "androidx.paging:paging-runtime:$version"
            const val runtimeKtx = "androidx.paging:paging-runtime-ktx:$version"
        }

        object Palette {
            private const val version = "1.0.0"
            const val palette = "androidx.palette:palette:$version"
            const val paletteKtx = "androidx.palette:palette-ktx:$version"
        }

        object Preference {
            private const val version = "1.1.1"
            const val preference = "androidx.preference:preference:$version"
            const val preferenceKtx = "androidx.preference:preference-ktx:$version"
        }

        object Recycler {
            private const val version = "1.2.0-beta01"
            const val recyclerView = "androidx.recyclerview:recyclerview:$version"

            object Selection {
                private const val version = "1.1.0-rc03"
                const val selection = "androidx.recyclerview:recyclerview-selection:$version"
            }
        }

        object Room {
            private const val version = "2.2.5"
            const val compiler = "androidx.room:room-compiler:$version"
            const val runtime = "androidx.room:room-runtime:$version"
            const val test = "androidx.room:room-testing:$version"
            const val ktx = "androidx.room:room-ktx:$version"
        }

        object Slice {
            private const val version = "1.1.0-alpha01"
            const val core = "androidx.slice:slice-core:$version"
        }

        object StartUp {
            private const val version = "1.0.0"
            const val startUpRuntime = "androidx.startup:startup-runtime:$version"
        }

        object SwipeRefresh {
            private const val version = "1.2.0-alpha01"
            const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:$version"
        }

        object Test {
            private const val version = "1.3.0"
            const val core = "androidx.test:core:$version"
            const val coreKtx = "androidx.test:core-ktx:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"

            object Espresso {
                private const val version = "3.3.0"
                const val core = "androidx.test.espresso:espresso-core:$version"
            }

            object Extension {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit:$version"
                const val junitKtx = "androidx.test.ext:junit-ktx:$version"
            }
        }

        object Work {
            private const val version = "2.5.0-beta02"
            const val runtimeKtx = "androidx.work:work-runtime-ktx:$version"
            const val runtime = "androidx.work:work-runtime:$version"
            const val test = "androidx.work:work-test:$version"
        }
    }

    object AniTrend {

        object Arch {
            private const val version = "a73b564e05"
            const val ui = "com.github.anitrend.support-arch:support-ui:${version}"
            const val ext = "com.github.anitrend.support-arch:support-ext:${version}"
            const val core = "com.github.anitrend.support-arch:support-core:${version}"
            const val data = "com.github.anitrend.support-arch:support-data:${version}"
            const val theme = "com.github.anitrend.support-arch:support-theme:${version}"
            const val domain = "com.github.anitrend.support-arch:support-domain:${version}"
            const val recycler = "com.github.anitrend.support-arch:support-recycler:${version}"
        }

        object Emojify {
            private const val version = "1.6.0-alpha01"
            const val emojify = "com.github.anitrend:android-emojify:$version"
        }

        object CommonUi {
            const val character = ":$commonCharacterUi"
            const val forum = ":$commonForumUi"
            const val media = ":$commonMediaUi"
            const val recommendation = ":$commonRecommendationUi"
            const val review = ":$commonReviewUi"
            const val staff = ":$commonStaffUi"
            const val user = ":$commonUserUi"
            const val episode = ":$commonEpisodeUi"
            const val news = ":$commonNewsUi"
            const val editor = ":$commonEditorUi"
            const val feed = ":$commonFeedUi"
            const val studio = ":$commonStudioUi"
        }

        object Markdown {
            private const val version = "0.1.0"
            const val markdown = "com.github.anitrend:support-markdown:${version}"
        }

        object Material {
            private const val version = "a368f32ed8"
            const val multiSearch = "com.github.anitrend:material-multi-search:${version}"
        }

        object Retrofit {
            private const val version = "0.11.0-alpha02"
            //private const val version = "develop-SNAPSHOT" // testing development snapshot
            const val graphQL = "com.github.anitrend:retrofit-graphql:${version}"
        }

        object Sync {
            private const val version = "0.1.0-alpha01"
            const val plugin = "com.github.anitrend:support-sync-plugin:${version}"
        }
    }

    object AirBnB {

        object Lottie {
            private const val version = "3.5.0"
            const val lottie = "com.airbnb.android:lottie:$version"
        }

        object Epoxy {
            private const val version = "4.0.0-beta6"
            const val epoxy = "com.airbnb.android:epoxy:$version"
            const val paging = "com.airbnb.android:epoxy-paging:$version"
            const val dataBinding = "com.airbnb.android:epoxy-databinding:$version"
            const val processor = "com.airbnb.android:epoxy-processor:$version"
        }
    }

    object Blitz {
        private const val version = "1.0.9"
        const val blitz = "com.github.perfomer:blitz:$version"
    }

    object CashApp {
        object Copper {
            private const val version = "1.0.0"
            const val copper = "app.cash.copper:copper-flow:$version"
        }

        object Contour {
            private const val version = "0.1.7"
            const val contour = "app.cash.contour:contour:$version"
        }

        object Turbine {
            private const val version = "0.3.0"
            const val turbine = "app.cash.turbine:turbine:$version"
        }
    }

    object Chuncker {
        private const val version = "3.4.0"

        const val debug = "com.github.ChuckerTeam.Chucker:library:$version"
        const val release = "com.github.ChuckerTeam.Chucker:library-no-op:$version"
    }

    object Coil {
        private const val version = "1.1.0"
        const val coil = "io.coil-kt:coil:$version"
        const val base = "io.coil-kt:coil-base:$version"
        const val gif = "io.coil-kt:coil-gif:$version"
        const val svg = "io.coil-kt:coil-svg:$version"
        const val video = "io.coil-kt:coil-video:$version"
    }

    object Dropbox {
        private const val version = "4.0.0"
        const val store = "com.dropbox.mobile.store:store4:$version"
    }

    object Google {

        object Exo {
            private const val version = "2.12.2"
            const val workManager = "com.google.android.exoplayer:extension-workmanager:$version"
            const val okHttp = "com.google.android.exoplayer:extension-okhttp:$version"
            const val core = "com.google.android.exoplayer:extension-core:$version"
            const val dash = "com.google.android.exoplayer:extension-dash:$version"
            const val hls = "com.google.android.exoplayer:exoplayer-hls:$version"
            const val ui = "com.google.android.exoplayer:exoplayer-ui:$version"
        }

        object Firebase {
            private const val version = "17.4.4"
            const val firebaseCore = "com.google.firebase:firebase-core:$version"

            object Analytics {
                private const val version = "18.0.0"
                const val analytics = "com.google.firebase:firebase-analytics:$version"
                const val analyticsKtx = "com.google.firebase:firebase-analytics-ktx:$version"
            }

            object Crashlytics {
                private const val version = "17.3.0"
                const val crashlytics = "com.google.firebase:firebase-crashlytics:$version"

                object Gradle {
                    private const val version = "2.4.1"
                    const val plugin = "com.google.firebase:firebase-crashlytics-gradle:$version"
                }
            }
        }

        object FlexBox {
            private const val version = "2.0.1"
            const val flexBox = "com.google.android:flexbox:$version"
        }

        object Material {
            private const val version = "1.3.0-alpha04"
            const val material = "com.google.android.material:material:$version"
        }

        object Services {
            private const val version = "4.3.4"
            const val googleServices = "com.google.gms:google-services:$version"
        }
    }

    object Glide {
        private const val version = "4.11.0"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object JetBrains {
        object Dokka {
            private const val version = "1.4.20"
            const val gradlePlugin = "org.jetbrains.dokka:dokka-gradle-plugin:$version"
        }

        object Kotlin {
            private const val version = "1.4.21"
            const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
            const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"

            object Gradle {
                const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
            }

            object Android {
                const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
            }

            object Serialization {
                const val serialization = "org.jetbrains.kotlin:kotlin-serialization:$version"
            }
        }

        object KotlinX {
            object Coroutines {
                private const val version = "1.4.2"
                const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
                const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
                const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
            }

            object Serialization {
                private const val version = "1.0.1"
                const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
            }
        }
    }

    object Koin {
        private const val version = "2.2.1"
        const val core = "org.koin:koin-core:$version"
        const val extension = "org.koin:koin-core-ext:$version"
        const val test = "org.koin:koin-test:$version"

        object AndroidX {
            const val scope = "org.koin:koin-androidx-scope:$version"
            const val fragment = "org.koin:koin-androidx-fragment:$version"
            const val viewModel = "org.koin:koin-androidx-viewmodel:$version"
            const val workManager = "org.koin:koin-androidx-workmanager:$version"
            const val compose = "org.koin:koin-androidx-compose:$version"
        }

        object Gradle {
            const val plugin = "org.koin:koin-gradle-plugin:$version"
        }
    }

    object Markwon {
        private const val version = "4.6.0"
        const val core = "io.noties.markwon:core:$version"
        const val html = "io.noties.markwon:html:$version"
        const val image = "io.noties.markwon:image:$version"
        const val glide = "io.noties.markwon:image-glide:$version"
        const val coil = "io.noties.markwon:image-coil:$version"
        const val parser = "io.noties.markwon:inline-parser:$version"
        const val linkify = "io.noties.markwon:linkify:$version"
        const val simpleExt = "io.noties.markwon:simple-ext:$version"
        const val syntaxHighlight = "io.noties.markwon:syntax-highlight:$version"

        object Extension {
            const val taskList = "io.noties.markwon:ext-tasklist:$version"
            const val strikeThrough = "io.noties.markwon:ext-strikethrough:$version"
            const val tables = "io.noties.markwon:ext-tables:$version"
            const val latex = "io.noties.markwon:ext-latex:$version"
        }
    }

    object MaterialDialogs {
        private const val version = "3.3.0"
        const val core = "com.afollestad.material-dialogs:core:$version"
        const val input = "com.afollestad.material-dialogs:input:$version"
        const val files = "com.afollestad.material-dialogs:files:$version"
        const val colour = "com.afollestad.material-dialogs:colour:$version"
        const val dateTime = "com.afollestad.material-dialogs:dateTime:$version"
        const val lifecycle = "com.afollestad.material-dialogs:lifecycle:$version"
        const val bottomsheets = "com.afollestad.material-dialogs:bottomsheets:$version"
    }

    object Square {

        object LeakCanary {
            private const val version = "2.5"
            const val leakCanary = "com.squareup.leakcanary:leakcanary-android:$version"
        }

        object Retrofit {
            private const val version = "2.9.0"
            const val retrofit = "com.squareup.retrofit2:retrofit:$version"
            const val gsonConverter =  "com.squareup.retrofit2:converter-gson:$version"
            const val xmlConverter =  "com.squareup.retrofit2:converter-simplexml:$version"
        }

        object OkHttp {
            private const val version = "4.10.0-RC1"
            const val okhttp = "com.squareup.okhttp3:okhttp:$version"
            const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
            const val mockServer = "com.squareup.okhttp3:mockwebserver:$version"
        }

        object WorkFlow {
            private const val version = "1.0.0-alpha08"
            const val coreJvm = "com.squareup.workflow:workflow-core-jvm:$version"
            const val coreAndroid = "com.squareup.workflow:workflow-ui-core-android:$version"
            const val modalAndroid = "com.squareup.workflow:workflow-ui-modal-android:$version"
            const val backStackAndroid = "com.squareup.workflow:workflow-ui-backstack-android:$version"
            const val testJvm = "com.squareup.workflow:workflow-testing-jvm:$version"
        }
    }

    object Tinder {
        object StateMachine {
            private const val version = "0.2.0"
            const val stateMachine = "com.tinder.statemachine:statemachine:${version}"
        }
    }
}
