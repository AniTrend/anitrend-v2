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
import co.anitrend.buildSrc.module.Modules

object Libraries {

    const val liquidSwipe = "com.github.Chrisvin:LiquidSwipe:${Versions.liquidSwipe}"

    const val threeTenBp = "com.jakewharton.threetenabp:threetenabp:${Versions.threeTenBp}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val treessence = "com.github.bastienpaulfr:Treessence:${Versions.treesSence}"
    const val debugDb = "com.amitshekhar.android:debug-db:${Versions.debugDB}"

    const val junit = "junit:junit:${Versions.junit}"

    const val elements = "com.otaliastudios:elements:${Versions.elements}"
    const val tmdb = "com.uwetrottmann.tmdb2:tmdb-java:${Versions.tmdb}"
    const val trakt = "com.uwetrottmann.trakt5:trakt-java:${Versions.trakt}"

    const val jsoup = "org.jsoup:jsoup:${Versions.jsoup}"

    const val scalingImageView = "com.davemorrissey.labs:subsampling-scale-image-view-androidx:${Versions.scalingImageView}"

    const val prettyTime = "org.ocpsoft.prettytime:prettytime:${Versions.prettyTime}"

    const val retrofitSerializer = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.serializationConverter}"

    const val deeplink = "com.hellofresh.android:deeplink:${Versions.deeplink}"

    object Repositories {
        const val jitPack = "https://www.jitpack.io"
        const val sonatypeReleases = "https://oss.sonatype.org/content/repositories/releases"
    }

    object Android {

        object Tools {
            private const val version = "7.0.4"
            const val buildGradle = "com.android.tools.build:gradle:${version}"
        }
    }

    object AndroidX {

        object Activity {
            private const val version = "1.4.0"
            const val activity = "androidx.activity:activity:$version"
            const val activityKtx = "androidx.activity:activity-ktx:$version"

            object Compose {
                const val activityCompose = "androidx.activity:activity-compose:$version"
            }
        }

        object AppCompat {
            private const val version = "1.3.0-alpha01"
            const val appcompat = "androidx.appcompat:appcompat:$version"
            const val appcompatResources = "androidx.appcompat:appcompat-resources:$version"
        }

        object Browser {
            private const val version = "1.4.0"
            const val browser = "androidx.browser:browser:$version"
        }

        object Collection {
            private const val version = "1.2.0"
            const val collection = "androidx.collection:collection:$version"
            const val collectionKtx = "androidx.collection:collection-ktx:$version"
        }

        object Compose {
            internal const val version = "1.1.0-rc01"

            object Foundation {
                const val foundation = "androidx.compose.foundation:foundation:$version"
                const val layout = "androidx.compose.foundation:foundation-layout:$version"
            }

            object Material {
                const val material = "androidx.compose.material:material:$version"
                const val ripple = "androidx.compose.material:material-ripple:$version"

                object Icons {
                    const val core = "androidx.compose.material:material-icons-core:$version"
                    const val extended = "androidx.compose.material:material-icons-extended:$version"
                }
            }

            object Runtime {
                const val runtime = "androidx.compose.runtime:runtime:$version"
                const val liveData = "androidx.compose.runtime:runtime-livedata:$version"
            }

            object Ui {
                const val ui = "androidx.compose.ui:ui:$version"
                const val tooling = "androidx.compose.ui:ui-tooling:$version"
                const val test = "androidx.compose.ui:ui-test:$version"
                const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"
            }
        }

        object Core {
            private const val version = "1.7.0"
            const val core = "androidx.core:core:$version"
            const val coreKtx = "androidx.core:core-ktx:$version"

            object Animation {
                private const val version = "1.0.0-alpha01"
                const val animation = "androidx.core:core-animation:${version}"
                const val animationTest = "androidx.core:core-animation-testing:${version}"
            }
        }

        object ConstraintLayout {
            private const val version = "2.1.2"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:$version"
            const val constraintLayoutSolver = "androidx.constraintlayout:constraintlayout-solver:$version"

            object Compose {
                private const val version = "1.0.0-rc01"
                const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:$version"
            }
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
            private const val version = "1.4.0"
            const val fragment = "androidx.fragment:fragment:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
            const val test = "androidx.fragment:fragment-ktx:fragment-testing$version"
        }

        object Lifecycle {
            private const val version = "2.4.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
            const val runTimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveDataCoreKtx = "androidx.lifecycle:lifecycle-livedata-core-ktx:$version"

            object Compose {
                private const val version = "2.4.0"
                const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
            }
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

            object Compose {
                private const val version = "1.0.0-alpha08"
                const val pagingCompose = "androidx.paging:paging-compose:$version"
            }
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
            private const val version = "1.3.0-alpha01"
            const val recyclerView = "androidx.recyclerview:recyclerview:$version"

            object Selection {
                private const val version = "1.1.0-rc03"
                const val selection = "androidx.recyclerview:recyclerview-selection:$version"
            }
        }

        object Room {
            private const val version = "2.4.0"
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
            private const val version = "1.1.0"
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
            private const val version = "2.7.1"
            const val runtimeKtx = "androidx.work:work-runtime-ktx:$version"
            const val multiProcess = "androidx.work:work-multiprocess:$version"
            const val runtime = "androidx.work:work-runtime:$version"
            const val test = "androidx.work:work-test:$version"
        }
    }

    object AniTrend {

        object Arch {
            private const val version = "1.4.0-alpha02"
            const val ui = "com.github.anitrend.support-arch:ui:${version}"
            const val ext = "com.github.anitrend.support-arch:extension:${version}"
            const val core = "com.github.anitrend.support-arch:core:${version}"
            const val data = "com.github.anitrend.support-arch:data:${version}"
            const val theme = "com.github.anitrend.support-arch:theme:${version}"
            const val domain = "com.github.anitrend.support-arch:domain:${version}"
            const val recycler = "com.github.anitrend.support-arch:recycler:${version}"
        }

        object Emojify {
            private const val version = "1.6.0-beta01"
            const val emojify = "com.github.anitrend:android-emojify:$version"
        }

        object CommonUi {
            val character = Modules.Common.Character.path()
            val forum = Modules.Common.Forum.path()
            val media = Modules.Common.Media.path()
            val recommendation = Modules.Common.Recommendation.path()
            val review = Modules.Common.Review.path()
            val staff = Modules.Common.Staff.path()
            val user = Modules.Common.User.path()
            val episode = Modules.Common.Episode.path()
            val news = Modules.Common.News.path()
            val mediaList = Modules.Common.MediaList.path()
            val editor = Modules.Common.Editor.path()
            val feed = Modules.Common.Feed.path()
            val studio = Modules.Common.Studio.path()
            val genre = Modules.Common.Genre.path()
            val tag = Modules.Common.Tag.path()
			val shared = Modules.Common.Shared.path()
			val markdown = Modules.Common.Markdown.path()
        }

        object Data {
            val android = Modules.Data.Android.path()
            val core = Modules.Data.Core.path()
            val feed = Modules.Data.Feed.path()
            val imgur = Modules.Data.Imgur.path()
            val jikan = Modules.Data.Jikan.path()
            val relation = Modules.Data.Relation.path()
            val theme = Modules.Data.Theme.path()
            val thexem = Modules.Data.TheXem.path()
            val tmdb = Modules.Data.Tmdb.path()
            val trakt = Modules.Data.Trakt.path()
            val settings = Modules.Data.Settings.path()
        }

        object Markdown {
			private const val version = "0.12.1-alpha01"
            const val markdown = "com.github.AniTrend:support-markdown:${version}"
        }

        object Material {
            private const val version = "0.2.0"
            const val multiSearch = "com.github.anitrend:material-multi-search:${version}"
        }

        object Retrofit {
            private const val version = "0.11.0-beta03"
            const val graphQL = "com.github.AniTrend:retrofit-graphql:${version}"
        }

        object Sync {
            private const val version = "0.1.0-alpha01"
            const val plugin = "com.github.anitrend:support-sync-plugin:${version}"
        }

        object QueryBuilder {
            private const val version = "0.1.4-alpha01"
            const val core = "com.github.anitrend.support-query-builder:core:$version"
            const val annotation = "com.github.AniTrend.support-query-builder:annotations:$version"
            const val processor = "com.github.anitrend.support-query-builder:processor:$version"
        }
    }

    object AirBnB {

        object Lottie {
            private const val version = "4.2.2"
            const val lottie = "com.airbnb.android:lottie:$version"
        }

        object Epoxy {
            private const val version = "4.0.0"
            const val epoxy = "com.airbnb.android:epoxy:$version"
            const val paging = "com.airbnb.android:epoxy-paging:$version"
            const val dataBinding = "com.airbnb.android:epoxy-databinding:$version"
            const val processor = "com.airbnb.android:epoxy-processor:$version"
        }

        object Paris {
            private const val version = "2.0.1"
            const val paris = "com.airbnb.android:paris:$version"
            /** if using annotations */
            const val processor = "com.airbnb.android:paris-processor:$version"
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
            private const val version = "1.1.0"
            const val contour = "app.cash.contour:contour:$version"
        }

        object Turbine {
            private const val version = "0.7.0"
            const val turbine = "app.cash.turbine:turbine:$version"
        }
    }

    object Chuncker {
        private const val version = "3.5.2"

        const val debug = "com.github.ChuckerTeam.Chucker:library:$version"
        const val release = "com.github.ChuckerTeam.Chucker:library-no-op:$version"
    }

    object Coil {
        private const val version = "1.4.0"
        const val coil = "io.coil-kt:coil:$version"
        const val base = "io.coil-kt:coil-base:$version"
        const val gif = "io.coil-kt:coil-gif:$version"
        const val svg = "io.coil-kt:coil-svg:$version"
        const val video = "io.coil-kt:coil-video:$version"
        const val compose = "io.coil-kt:coil-compose:$version"
    }

    object Devrieze {
        object XmlUtil {
            private const val version = "0.84.0-RC1"

            object Android {
                const val core = "io.github.pdvrieze.xmlutil:core-android:$version"
                const val serialization = "io.github.pdvrieze.xmlutil:serialization-android:$version"
            }
        }
    }

    object Dropbox {
        private const val version = "4.0.4-KT15"
        const val store = "com.dropbox.mobile.store:store4:$version"
    }

    object Google {

        object Accompanist {
            private const val version = "0.22.0-rc"
            const val insets = "com.google.accompanist:accompanist-insets:$version"
            const val uiController = "com.google.accompanist:accompanist-systemuicontroller:${version}"
            const val appCompatTheme = "com.google.accompanist:accompanist-appcompat-theme:${version}"
            const val pager = "com.google.accompanist:accompanist-pager:${version}"
            const val pagerIndicators = "com.google.accompanist:accompanist-pager-indicators:${version}"
            const val flowLayout = "com.google.accompanist:accompanist-flowlayout:${version}"
        }

        object Gson {
            private const val version = "2.8.9"
            const val gson = "com.google.code.gson:gson:$version"
        }

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
                private const val version = "20.0.2"
                const val analytics = "com.google.firebase:firebase-analytics:$version"
                const val analyticsKtx = "com.google.firebase:firebase-analytics-ktx:$version"
            }

            object Crashlytics {
                private const val version = "18.2.6"
                const val crashlytics = "com.google.firebase:firebase-crashlytics:$version"

                object Gradle {
                    private const val version = "2.8.1"
                    const val plugin = "com.google.firebase:firebase-crashlytics-gradle:$version"
                }
            }
        }

        object FlexBox {
            private const val version = "2.0.1"
            const val flexBox = "com.google.android:flexbox:$version"
        }

        object Material {
            //private const val version = "cbf7bcad59"
            //const val material = "com.github.wax911:material-components-android:$version"
            private const val version = "1.5.0-rc01"
            const val material = "com.google.android.material:material:$version"

            object Compose {
                private const val version = "1.0.0-beta06"
                const val themeAdapter = "com.google.android.material:compose-theme-adapter:$version"
            }
        }

        object Services {
            private const val version = "4.3.10"
            const val googleServices = "com.google.gms:google-services:$version"
        }
    }

    object JetBrains {
        object Dokka {
            private const val version = "1.5.31"
            const val gradlePlugin = "org.jetbrains.dokka:dokka-gradle-plugin:$version"
        }

        object Kotlin {
            internal const val version = "1.5.31"
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
                private const val version = "1.5.2"
                const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
                const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
                const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
            }

            object DateTime {
                private const val version = "0.2.1"
                const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:$version"
            }

            object Serialization {
                private const val version = "1.1.0"
                const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
            }
        }
    }

    object Koin {
        private const val version = "2.2.3"
        const val core = "io.insert-koin:koin-core:$version"
        const val extension = "io.insert-koin:koin-core-ext:$version"
        const val test = "io.insert-koin:koin-test:$version"

        object AndroidX {
            const val scope = "io.insert-koin:koin-androidx-scope:$version"
            const val fragment = "io.insert-koin:koin-androidx-fragment:$version"
            const val viewModel = "io.insert-koin:koin-androidx-viewmodel:$version"
            const val workManager = "io.insert-koin:koin-androidx-workmanager:$version"
            // Compose is only available in koin 3.X
            const val compose = "io.insert-koin:koin-androidx-compose:$version"
        }

        object Gradle {
            const val plugin = "io.insert-koin:koin-gradle-plugin:$version"
        }
    }
    
    object Korlibs {
		
		object Klock {
			private const val version = "2.0.7"
			const val klock = "com.soywiz.korlibs.klock:klock-jvm:$version"
		}
	}
	
    object Markwon {
        private const val version = "4.6.2"
        const val core = "io.noties.markwon:core:$version"
        const val editor = "io.noties.markwon:editor:$version"
        const val html = "io.noties.markwon:html:$version"
        const val image = "io.noties.markwon:image:$version"
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

    object Mockk {
        const val version = "1.12.1"
        const val mockk = "io.mockk:mockk:$version"
        const val mockkAndroid = "io.mockk:mockk-android:$version"
    }

    object Saket {

        object BetterLinkMovement {
            private const val version = "2.2.0"
            const val betterLinkMovement = "me.saket:better-link-movement-method:$version"
        }

        object Cascade {
            private const val version = "1.3.0"
            const val cascade = "me.saket.cascade:cascade:$version"
        }
    }

    object Sheets {
        private const val version = "2.1.3"

        const val calendar = "com.maxkeppeler.sheets:calendar:$version"
        const val core = "com.maxkeppeler.sheets:core:$version"
        const val info = "com.maxkeppeler.sheets:info:$version"
        const val input = "com.maxkeppeler.sheets:input:$version"
        const val lottie = "com.maxkeppeler.sheets:lottie:$version"
        const val options = "com.maxkeppeler.sheets:options:$version"
        const val simple = "com.maxkeppeler.sheets:simple:$version"
        const val timeClock = "com.maxkeppeler.sheets:time-clock:$version"
        const val time = "com.maxkeppeler.sheets:time:$version"
    }

    object SmartTab {
        private const val version = "2.0.0@aar"
        const val layout = "com.ogaclejapan.smarttablayout:library:$version"
        const val utilities = "com.ogaclejapan.smarttablayout:utils-v4:$version"
    }

    object Square {

        object LeakCanary {
            private const val version = "2.7"
            const val leakCanary = "com.squareup.leakcanary:leakcanary-android:$version"
        }

        object Retrofit {
            private const val version = "2.9.0"
            const val retrofit = "com.squareup.retrofit2:retrofit:$version"
            const val gsonConverter =  "com.squareup.retrofit2:converter-gson:$version"
            @Deprecated("Use XmlUtil instead", ReplaceWith("Libraries.Devrieze.XmlUtil.Android.serialization"))
            const val xmlConverter =  "com.squareup.retrofit2:converter-simplexml:$version"
        }

        object OkHttp {
            private const val version = "4.9.2"
            const val okhttp = "com.squareup.okhttp3:okhttp:$version"
            const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
            const val mockServer = "com.squareup.okhttp3:mockwebserver:$version"
        }

        object KotlinPoet {
            private const val version = "1.10.2"
            const val kotlinPoet = "com.squareup:kotlinpoet:$version"
        }
    }
}
