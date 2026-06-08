# Studio Detail Page Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use subagent-driven-development (recommended) or executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement full studio detail page with offline-first caching, GraphQL-backed studio + media data, and edge enrichment for studio logos.

**Architecture:** Standard Genre data-layer pattern: `DefaultMapper` → `AbstractCoreDataSource` → `Repository` → `UseCase` → `ViewModel`. Flat response container for GraphQL (avoids sealed class deserialization). Edge enrichment via `MediaStudioEntryEnricher`.

**Tech Stack:** Kotlin, Jetpack Compose, Room, Retrofit-GraphQL, Koin, Coil, Coroutines + Flow

---

### Task 1: Domain contracts — IStudioRepository + StudioUseCase + StudioDetailData

**Files:**
- Create: `domain/src/main/kotlin/co/anitrend/domain/studio/repository/IStudioRepository.kt`
- Create: `domain/src/main/kotlin/co/anitrend/domain/studio/interactor/StudioUseCase.kt`
- Create: `domain/src/main/kotlin/co/anitrend/domain/studio/entity/StudioDetailData.kt`

- [ ] **Create IStudioRepository**

```kotlin
package co.anitrend.domain.studio.repository

import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.studio.model.StudioParam

interface IStudioRepository<State : UiState<*>> {
    fun getStudio(param: StudioParam.Detail): State
}
```

- [ ] **Create StudioUseCase**

```kotlin
package co.anitrend.domain.studio.interactor

import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.studio.model.StudioParam
import co.anitrend.domain.studio.repository.IStudioRepository

abstract class StudioUseCase<State : UiState<*>>(
    protected val repository: IStudioRepository<State>,
) {
    fun getStudio(param: StudioParam.Detail) = repository.getStudio(param)
}
```

- [ ] **Create StudioDetailData**

```kotlin
package co.anitrend.domain.studio.entity

import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.studio.entity.Studio

data class StudioDetailData(
    val studio: Studio,
    val mediaEntries: List<MediaStudioEntry>,
    val networkLogo: CoverImage? = null,
    override val id: Long,
) : co.anitrend.domain.common.entity.IEntity
```

- [ ] **Commit**: `git add domain/src/main/kotlin/co/anitrend/domain/studio/ && git commit -m "feat(domain): add studio detail contracts and StudioDetailData model"`

---

### Task 2: Data Types.kt — typealiases

**Files:**
- Modify: `data/src/main/kotlin/co/anitrend/data/studio/Types.kt`

- [ ] **Replace Types.kt with typealiases**

```kotlin
package co.anitrend.data.studio

import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.entity.connection.MediaStudioConnectionEntity
import co.anitrend.data.studio.model.remote.StudioDetailContainer
import co.anitrend.data.studio.mapper.StudioDetailPersistenceData
import co.anitrend.domain.studio.entity.StudioDetailData
import co.anitrend.domain.studio.interactor.StudioUseCase
import co.anitrend.domain.studio.repository.IStudioRepository

internal typealias MediaStudioDetailController = GraphQLController<StudioDetailContainer, StudioDetailPersistenceData>
internal typealias StudioDetailRepository = IStudioRepository<DataState<StudioDetailData>>
typealias StudioDetailInteractor = StudioUseCase<DataState<StudioDetailData>>
```

- [ ] **Commit**: `git commit -m "feat(data-studio): add typealiases for studio detail controller, repository, interactor"`

---

### Task 3: StudioDetailContainer — flat response model

**Files:**
- Create: `data/src/main/kotlin/co/anitrend/data/studio/model/remote/StudioDetailContainer.kt`

- [ ] **Create StudioDetailContainer**

```kotlin
package co.anitrend.data.studio.model.remote

import co.anitrend.data.media.model.connection.MediaConnection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StudioDetailContainer(
    @SerialName("favourites") val favourites: Int? = null,
    @SerialName("isAnimationStudio") val isAnimationStudio: Boolean,
    @SerialName("isFavourite") val isFavourite: Boolean,
    @SerialName("isFavouriteBlocked") val isFavouriteBlocked: Boolean? = null,
    @SerialName("name") val name: String,
    @SerialName("siteUrl") val siteUrl: String,
    @SerialName("id") val id: Long,
    @SerialName("media") val media: MediaConnection? = null,
)
```

- [ ] **Commit**: `git commit -m "feat(data-studio): add StudioDetailContainer flat response model"`

---

### Task 4: StudioDetailPersistenceData + StudioDetailMapper

**Files:**
- Create: `data/src/main/kotlin/co/anitrend/data/studio/mapper/StudioDetailPersistenceData.kt`
- Create: `data/src/main/kotlin/co/anitrend/data/studio/mapper/StudioDetailMapper.kt`

- [ ] **Create StudioDetailPersistenceData**

```kotlin
package co.anitrend.data.studio.mapper

import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.entity.connection.MediaStudioConnectionEntity

internal data class StudioDetailPersistenceData(
    val studio: StudioEntity,
    val mediaConnections: List<MediaStudioConnectionEntity>,
)
```

- [ ] **Create StudioDetailMapper**

```kotlin
package co.anitrend.data.studio.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.studio.datasource.local.StudioLocalSource
import co.anitrend.data.studio.datasource.local.connection.MediaStudioConnectionLocalSource
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.entity.connection.MediaStudioConnectionEntity
import co.anitrend.data.studio.model.remote.StudioDetailContainer

internal class StudioDetailMapper(
    private val studioLocalSource: StudioLocalSource,
    private val connectionLocalSource: MediaStudioConnectionLocalSource,
) : DefaultMapper<StudioDetailContainer, StudioDetailPersistenceData>() {
    override suspend fun persist(data: StudioDetailPersistenceData) {
        studioLocalSource.upsert(data.studio)
        connectionLocalSource.upsert(data.mediaConnections)
    }

    override suspend fun onResponseMapFrom(source: StudioDetailContainer): StudioDetailPersistenceData {
        val studioEntity = StudioEntity(
            favourites = source.favourites ?: 0,
            isAnimationStudio = source.isAnimationStudio,
            isFavourite = source.isFavourite,
            isFavouriteBlocked = source.isFavouriteBlocked ?: false,
            name = source.name,
            siteUrl = source.siteUrl,
            id = source.id,
        )

        val mediaConnections = source.media?.edges.orEmpty().mapIndexedNotNull { index, edge ->
            val node = edge.node ?: return@mapIndexedNotNull null
            MediaStudioConnectionEntity(
                mediaId = node.id,
                entryId = edge.id ?: node.id,
                studioId = source.id,
                studioName = source.name,
                studioFavourites = source.favourites,
                studioIsAnimationStudio = source.isAnimationStudio,
                studioSiteUrl = source.siteUrl,
                isMain = edge.isMain,
                sortIndex = index,
            )
        }

        return StudioDetailPersistenceData(
            studio = studioEntity,
            mediaConnections = mediaConnections,
        )
    }
}
```

- [ ] **Commit**: `git commit -m "feat(data-studio): add StudioDetailMapper and persistence data"`

---

### Task 5: GraphQL operation — GetStudioDetail.graphql

**Files:**
- Create: `data/src/main/assets/graphql/queries/studio/GetStudioDetail.graphql`
- Modify: `data/src/main/assets/graphql/fragments/studio/StudioCore.graphql`

- [ ] **Update StudioCore fragment to include mediaConnection fields** (actually, we need a separate path — the detail query will inline the fields since mediaConnection is detail-only)

```graphql
# data/src/main/assets/graphql/queries/studio/GetStudioDetail.graphql
query GetStudioDetail($id: Int) {
    Studio(id: $id) {
        favourites
        isAnimationStudio
        isFavourite
        isFavouriteBlocked
        name
        siteUrl
        id
        media(isMain: true, sort: [POPULARITY_DESC], perPage: 30) {
            edges {
                node {
                    id
                    title {
                        userPreferred
                    }
                    coverImage {
                        large
                    }
                    format
                    startDate {
                        year
                    }
                    meanScore
                }
                isMain
                id
            }
        }
    }
}
```

- [ ] **Commit**: `git commit -m "feat(graphql): add GetStudioDetail query"`

---

### Task 6: StudioDetailRemoteSource

**Files:**
- Modify: `data/src/main/kotlin/co/anitrend/data/studio/datasource/remote/StudioRemoteSource.kt`

- [ ] **Replace StudioRemoteSource with GraphQL method**

```kotlin
package co.anitrend.data.studio.datasource.remote

import co.anitrend.data.studio.model.remote.StudioDetailContainer
import co.anitrend.support.query.builder.annotation.GRAPHQL
import co.anitrend.support.query.builder.annotation.GraphQuery
import co.anitrend.support.query.builder.model.QueryContainerBuilder
import co.anitrend.support.query.builder.model.response.GraphQLResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import co.anitrend.arch.data.request.IEndpointType

internal interface StudioRemoteSource {
    @GRAPHQL
    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("GetStudioDetail")
    suspend fun getStudioDetail(@Body queryContainer: QueryContainerBuilder): Response<GraphQLResponse<StudioDetailContainer>>
}
```

- [ ] **Commit**: `git commit -m "feat(data-studio): add getStudioDetail remote source"`

---

### Task 7: entriesByStudioIdFlow on MediaStudioConnectionLocalSource

**Files:**
- Modify: `data/src/main/kotlin/co/anitrend/data/studio/datasource/local/connection/MediaStudioConnectionLocalSource.kt`

- [ ] **Add entriesByStudioIdFlow query**

Insert before the closing brace of the class:

```kotlin
@Query(
    """
    select * from media_studio_connection
    where studio_id = :studioId
    order by sort_index asc
    """,
)
abstract fun entriesByStudioIdFlow(studioId: Long): Flow<List<MediaStudioConnectionEntity>>
```

- [ ] **Commit**: `git commit -m "feat(data-studio): add entriesByStudioIdFlow to MediaStudioConnectionLocalSource"`

---

### Task 8: StudioCache

**Files:**
- Create: `data/src/main/kotlin/co/anitrend/data/studio/cache/StudioCache.kt`

- [ ] **Create StudioCache**

```kotlin
package co.anitrend.data.studio.cache

import co.anitrend.data.android.cache.datasource.CacheLocalSource
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.cache.repository.CacheStorePolicy
import org.threeten.bp.Instant

internal class StudioCache(
    override val localSource: CacheLocalSource,
    override val request: CacheRequest = CacheRequest.STUDIO,
) : CacheStorePolicy() {
    override suspend fun shouldRefresh(
        identity: CacheIdentity,
        expiresAfter: Instant,
    ): Boolean = isRequestBefore(identity, expiresAfter)

    enum class Identity(
        override val id: Long,
        override val key: String,
    ) : CacheIdentity {
        DETAIL(10L, "studio_detail"),
    }
}
```

Note: check if `CacheRequest.STUDIO` exists; if not, use `CacheRequest.GENRE` or define a new enum constant.

- [ ] **Commit**: `git commit -m "feat(data-studio): add StudioCache"`

---

### Task 9: StudioDetailSource (abstract contract)

**Files:**
- Create: `data/src/main/kotlin/co/anitrend/data/studio/source/contract/StudioDetailSource.kt`

- [ ] **Create StudioDetailSource**

```kotlin
package co.anitrend.data.studio.source.contract

import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.data.studio.cache.StudioCache
import co.anitrend.domain.studio.entity.StudioDetailData
import co.anitrend.domain.studio.model.StudioParam
import kotlinx.coroutines.flow.Flow

internal abstract class StudioDetailSource : AbstractCoreDataSource() {
    protected lateinit var param: StudioParam.Detail
    protected lateinit var cacheIdentity: CacheIdentity

    protected abstract val cachePolicy: ICacheStorePolicy
    protected abstract fun observable(): Flow<StudioDetailData>
    protected abstract suspend fun getStudio(callback: RequestCallback): Boolean

    internal operator fun invoke(param: StudioParam.Detail): Flow<StudioDetailData> {
        this.param = param
        cacheIdentity = StudioCache.Identity.DETAIL
        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = cacheIdentity,
            block = ::getStudio,
        )
        return observable()
    }
}
```

- [ ] **Commit**: `git commit -m "feat(data-studio): add StudioDetailSource abstract contract"`

---

### Task 10: StudioDetailSourceImpl

**Files:**
- Create: `data/src/main/kotlin/co/anitrend/data/studio/source/StudioDetailSourceImpl.kt`

- [ ] **Create StudioDetailSourceImpl**

```kotlin
package co.anitrend.data.studio.source

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.edge.network.datasource.local.EdgeNetworkLocalSource
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.studio.MediaStudioDetailController
import co.anitrend.data.studio.converter.MediaStudioEntryEnricher
import co.anitrend.data.studio.converter.StudioEntityConverter
import co.anitrend.data.studio.datasource.local.StudioLocalSource
import co.anitrend.data.studio.datasource.local.connection.MediaStudioConnectionLocalSource
import co.anitrend.data.studio.datasource.remote.StudioRemoteSource
import co.anitrend.data.studio.source.contract.StudioDetailSource
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.domain.studio.entity.StudioDetailData
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class StudioDetailSourceImpl(
    private val remoteSource: StudioRemoteSource,
    private val localSource: StudioLocalSource,
    private val connectionLocalSource: MediaStudioConnectionLocalSource,
    private val mediaLocalSource: MediaLocalSource,
    private val edgeNetworkLocalSource: EdgeNetworkLocalSource,
    private val controller: MediaStudioDetailController,
    private val clearDataHelper: IClearDataHelper,
    private val entityConverter: StudioEntityConverter,
    private val enricher: MediaStudioEntryEnricher,
    override val cachePolicy: ICacheStorePolicy,
    override val dispatcher: ISupportDispatcher,
) : StudioDetailSource() {
    override fun observable(): Flow<StudioDetailData> =
        combine(
            localSource.studioByIdFlow(param.id).filterNotNull(),
            connectionLocalSource.entriesByStudioIdFlow(param.id),
        ) { studioEntity, connections ->
            val studio = entityConverter.convertFrom(studioEntity)
            val networks = edgeNetworkLocalSource.allFlow().firstOrNull().orEmpty()

            val mediaEntries = connections.mapNotNull { connection ->
                val mediaEntity = mediaLocalSource.mediaByIdFlow(connection.mediaId).firstOrNull() ?: return@mapNotNull null
                MediaStudioEntry(
                    studio = studio,
                    isMain = connection.isMain,
                    id = connection.id ?: connection.entryId,
                )
            }

            val enriched = enricher.enrich(mediaEntries, networks)
            val networkLogo = enriched.firstNotNullOfOrNull { it.networkMatch?.logoPath }
                ?.let { CoverImage(large = it, medium = it) }

            StudioDetailData(
                studio = studio,
                mediaEntries = enriched,
                networkLogo = networkLogo,
                id = studio.id,
            )
        }.flowOn(dispatcher.io)

    override suspend fun getStudio(callback: RequestCallback): Boolean {
        val deferred = deferred { remoteSource.getStudioDetail(QueryContainerBuilder(param.id)) }
        val result = controller(deferred, callback)
        return result != null
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            cachePolicy.invalidateLastRequest(cacheIdentity)
            localSource.clearById(param.id)
        }
    }
}
```

Note: `QueryContainerBuilder(param.id)` — verify constructor signature. May need `QueryContainerBuilder().apply { add("id", param.id) }`.

- [ ] **Commit**: `git commit -m "feat(data-studio): add StudioDetailSourceImpl with edge enrichment"`

---

### Task 11: StudioDetailRepository

**Files:**
- Create: `data/src/main/kotlin/co/anitrend/data/studio/repository/StudioDetailRepository.kt`

- [ ] **Create StudioDetailRepository**

```kotlin
package co.anitrend.data.studio.repository

import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.data.studio.StudioDetailRepository
import co.anitrend.data.studio.source.contract.StudioDetailSource
import co.anitrend.domain.studio.model.StudioParam

internal class StudioDetailRepository(
    private val source: StudioDetailSource,
) : StudioDetailRepository {
    override fun getStudio(param: StudioParam.Detail) = source create source(param)
}
```

- [ ] **Commit**: `git commit -m "feat(data-studio): add StudioDetailRepository"`

---

### Task 12: StudioDetailUseCaseImpl

**Files:**
- Create: `data/src/main/kotlin/co/anitrend/data/studio/usecase/StudioDetailUseCaseImpl.kt`

- [ ] **Create StudioDetailUseCaseImpl**

```kotlin
package co.anitrend.data.studio.usecase

import co.anitrend.data.studio.StudioDetailInteractor
import co.anitrend.data.studio.StudioDetailRepository

internal class StudioDetailUseCaseImpl(
    repository: StudioDetailRepository,
) : StudioDetailInteractor(repository)
```

- [ ] **Commit**: `git commit -m "feat(data-studio): add StudioDetailUseCaseImpl"`

---

### Task 13: Data layer Koin wiring

**Files:**
- Modify: `data/src/main/kotlin/co/anitrend/data/studio/koin/Modules.kt`

- [ ] **Populate sourceModule, useCaseModule, repositoryModule, add cacheModule, mapperModule**

```kotlin
package co.anitrend.data.studio.koin

import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.studio.StudioDetailInteractor
import co.anitrend.data.studio.StudioDetailRepository
import co.anitrend.data.studio.cache.StudioCache
import co.anitrend.data.studio.converter.MediaStudioConnectionEntityConverter
import co.anitrend.data.studio.converter.MediaStudioEntryEnricher
import co.anitrend.data.studio.converter.StudioConverter
import co.anitrend.data.studio.converter.StudioEntityConverter
import co.anitrend.data.studio.converter.StudioModelConverter
import co.anitrend.data.studio.mapper.MediaStudioMapper
import co.anitrend.data.studio.mapper.StudioDetailMapper
import co.anitrend.data.studio.repository.StudioDetailRepository
import co.anitrend.data.studio.source.StudioDetailSourceImpl
import co.anitrend.data.studio.source.contract.StudioDetailSource
import co.anitrend.data.studio.usecase.StudioDetailUseCaseImpl
import org.koin.dsl.module

private val cacheModule = module {
    factory {
        StudioCache(
            localSource = cacheLocalSource(),
        )
    }
}

private val sourceModule = module {
    factory<StudioDetailSource> {
        StudioDetailSourceImpl(
            remoteSource = aniListApi(),
            localSource = store().studioDao(),
            connectionLocalSource = store().mediaStudioConnectionDao(),
            mediaLocalSource = store().mediaDao(),
            edgeNetworkLocalSource = store().edgeNetworkDao(),
            controller = graphQLController(mapper = get<StudioDetailMapper>()),
            clearDataHelper = get(),
            entityConverter = get(),
            enricher = get(),
            cachePolicy = get<StudioCache>(),
            dispatcher = get(),
        )
    }
}

private val converterModule = module {
    factory { MediaStudioConnectionEntityConverter() }
    factory { MediaStudioEntryEnricher() }
    factory { StudioConverter() }
    factory { StudioModelConverter() }
    factory { StudioEntityConverter() }
}

private val mapperModule = module {
    factory { MediaStudioMapper(localSource = store().mediaStudioConnectionDao()) }
    factory {
        StudioDetailMapper(
            studioLocalSource = store().studioDao(),
            connectionLocalSource = store().mediaStudioConnectionDao(),
        )
    }
}

private val useCaseModule = module {
    factory<StudioDetailInteractor> {
        StudioDetailUseCaseImpl(repository = get())
    }
}

private val repositoryModule = module {
    factory<StudioDetailRepository> {
        StudioDetailRepository(source = get())
    }
}

internal val studioModules = module {
    includes(
        cacheModule,
        converterModule,
        mapperModule,
        sourceModule,
        useCaseModule,
        repositoryModule,
    )
}
```

- [ ] **Commit**: `git commit -m "feat(data-studio): wire studio detail modules in Koin"`

---

### Task 14: StudioViewModel

**Files:**
- Create: `feature/studio/src/main/kotlin/co/anitrend/studio/component/viewmodel/StudioViewModel.kt`

- [ ] **Create StudioViewModel**

```kotlin
package co.anitrend.studio.component.viewmodel

import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.domain.observer.StateObserver
import co.anitrend.core.component.viewmodel.AniTrendViewModelState
import co.anitrend.data.studio.StudioDetailInteractor
import co.anitrend.domain.studio.entity.StudioDetailData
import co.anitrend.domain.studio.model.StudioParam
import co.anitrend.navigation.StudioRouter

class StudioViewModel(
    private val interactor: StudioDetailInteractor,
) : AniTrendViewModelState<StudioDetailData>() {
    override val model: StateObserver<StudioDetailData> = StateObserver()

    operator fun invoke(param: StudioRouter.StudioParam) {
        val studioParam = StudioParam.Detail(id = param.id)
        launchState { interactor.getStudio(studioParam) }
    }
}
```

- [ ] **Commit**: `git commit -m "feat(studio): add StudioViewModel"`

---

### Task 15: Compose UI — StudioContent + StudioScreen updates

**Files:**
- Create: `feature/studio/src/main/kotlin/co/anitrend/studio/component/compose/StudioContent.kt`
- Modify: `feature/studio/src/main/kotlin/co/anitrend/studio/component/compose/StudioCompose.kt`
- Modify: `feature/studio/src/main/kotlin/co/anitrend/studio/component/screen/StudioScreen.kt`

- [ ] **Create StudioContent.kt** with header + media section

```kotlin
package co.anitrend.studio.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import co.anitrend.arch.data.state.DataState
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.domain.studio.entity.StudioDetailData

@Composable
internal fun StudioDetailContent(
    state: DataState<StudioDetailData>?,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is DataState.Loading -> LoadingContent(modifier = modifier)
        is DataState.Success -> {
            val data = state.data
            StudioDetailPopulated(
                data = data,
                modifier = modifier,
            )
        }
        is DataState.Empty -> EmptyContent(modifier = modifier)
        is DataState.Error -> ErrorContent(
            message = state.message ?: "Couldn't load studio",
            modifier = modifier,
        )
        null -> {}
    }
}

@Composable
private fun StudioDetailPopulated(
    data: StudioDetailData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        StudioImageSection(image = data.networkLogo)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = data.studio.name,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "\u2605 ${data.studio.favourites} Favourites",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (data.studio.isAnimationStudio) {
            SuggestionChip(
                onClick = {},
                label = { Text("Animation Studio") },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        data.studio.siteUrl?.let { url ->
            Text(
                text = url,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 1.dp,
        ) {
            Column {
                Text(
                    text = "Media",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 8.dp),
                )

                if (data.mediaEntries.isEmpty()) {
                    Text(
                        text = "No media found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(data.mediaEntries, key = { it.id }) { entry ->
                            MediaCardSmall(entry = entry)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StudioImageSection(image: CoverImage?) {
    if (image != null) {
        AsyncImage(
            model = image.large,
            contentDescription = "Studio logo",
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Fit,
        )
    } else {
        Surface(
            modifier = Modifier.size(96.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            // Placeholder surface for missing logo
        }
    }
}

@Composable
private fun MediaCardSmall(entry: MediaStudioEntry) {
    Column(
        modifier = Modifier.width(120.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            entry.studio.image?.large?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = entry.studio.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        Text(
            text = entry.studio.name,
            style = MaterialTheme.typography.caption,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    // Shimmer placeholder
    Text("Loading...", modifier = modifier.padding(16.dp))
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Text(
        "No studio data available",
        modifier = modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        message,
        modifier = modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
    )
}

@AniTrendPreview.Default
@Composable
private fun StudioContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme {
        StudioDetailContent(state = DataState.Loading)
    }
}
```

- [ ] **Update StudioCompose.kt to use StudioDetailContent**

```kotlin
@Composable
fun StudioScreenContent(
    state: DataState<StudioDetailData>?,
    onBackPress: () -> Unit,
) {
    DefaultScaffold(onBackPress = onBackPress) {
        StudioDetailContent(
            state = state,
            modifier = Modifier.padding(it),
        )
    }
}
```

- [ ] **Update StudioScreen.kt to wire ViewModel**

```kotlin
class StudioScreen : AniTrendScreen() {
    private val viewModel by viewModel<StudioViewModel>()

    override fun initializeComponents(savedInstanceState: Bundle?) {
        val param = intent?.extras?.let { StudioRouter.StudioParam(it) }
        param?.let { viewModel(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                val state by viewModel.model.observeAsState()
                StudioScreenContent(
                    state = state,
                    onBackPress = onBackPressedDispatcher::onBackPressed,
                )
            }
        }
    }
}
```

Note: `viewModel.model` is `LiveData<DataState<StudioDetailData>>`, observed via `observeAsState()`.

- [ ] **Commit**: `git commit -m "feat(studio): add StudioContent UI and wire ViewModel"`

---

### Task 16: Feature layer Koin wiring

**Files:**
- Modify: `feature/studio/src/main/kotlin/co/anitrend/studio/koin/Modules.kt`

- [ ] **Add viewModelModule**

```kotlin
package co.anitrend.studio.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.data.studio.StudioDetailInteractor
import co.anitrend.navigation.StudioRouter
import co.anitrend.studio.component.viewmodel.StudioViewModel
import co.anitrend.studio.provider.FeatureProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val featureModule = module {
    factory<StudioRouter.Provider> { FeatureProvider() }
    viewModel { StudioViewModel(interactor = get<StudioDetailInteractor>()) }
}

internal val moduleHelper = DynamicFeatureModuleHelper(listOf(featureModule))
```

- [ ] **Commit**: `git commit -m "feat(studio): wire StudioViewModel in feature Koin module"`

---

### Task 17: Format + test

**Files:**
- Run: `./gradlew spotlessApply`
- Run: `./gradlew lint spotlessCheck`
- Run: `./gradlew :data:testDebugUnitTest --no-daemon` (data layer)
- Run: `./gradlew :feature:studio:testDebugUnitTest --no-daemon` (feature layer)
- Run targeted `rg` or file-existence checks for any edited doc references.

- [ ] **Run formatting and checks**: `git add -A && ./gradlew spotlessApply && git commit -m "chore: spotlessApply formatting"`
- [ ] **Run tests**: `./gradlew testDebugUnitTest --no-daemon`
- [ ] **Verify no regressions**
