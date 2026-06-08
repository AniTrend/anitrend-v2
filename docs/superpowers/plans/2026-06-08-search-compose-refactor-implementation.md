# Search Compose Refactor Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Split `feature/search` Compose UI into focused ownership-based files inside the existing package without changing search behavior.

**Architecture:** Keep `co.anitrend.search.component.compose` as the package and decompose the current `SearchCompose.kt` monolith into a thin coordinator file, narrow shared UI files, and domain-owned section files. Preserve `SearchScreen`, `SearchViewModel`, routing, and tests; the refactor is successful only if behavior and compile/test outcomes remain unchanged.

**Tech Stack:** Kotlin, Jetpack Compose, Paging Compose, Koin, AniTrend shared UI components, Gradle

---

## File Structure

### Create

- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchScreenContent.kt` — top-level coordinator, state collection, scope routing, and callback threading.
- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchChrome.kt` — `SearchBarContent`, `SearchScopeChips`, and `SearchScope.label()`.
- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchState.kt` — reusable loading, idle, empty, and error state UI.
- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchMediaSections.kt` — `SearchSection` and `SearchDrillDown` for media scopes.
- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchUserSections.kt` — user preview/drill-down/list composables.
- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchStudioSections.kt` — studio preview/drill-down/list composables and local studio UI model.
- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchStaffSections.kt` — staff preview/drill-down/list composables.
- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCharacterSections.kt` — character preview/drill-down/list composables.
- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchPreview.kt` — preview-only entry point.

### Modify

- `feature/search/src/main/kotlin/co/anitrend/search/component/screen/SearchScreen.kt` — import remains `SearchScreenContent`; only touch if a temporary import breaks during extraction.
- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt` — temporary extraction source; delete when empty.

### Test / Verify

- `feature/search/src/test/kotlin/co/anitrend/search/component/viewmodel/SearchViewModelTest.kt` — unchanged regression coverage.

## Task 1: Establish the coordinator/shared-file skeleton

**Files:**
- Create: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchScreenContent.kt`
- Create: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchChrome.kt`
- Create: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchState.kt`
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt`

- [ ] **Step 1: Capture the current safety baseline**

Run:

```bash
./gradlew :feature:search:compileDebugKotlin :feature:search:testDebugUnitTest --tests co.anitrend.search.component.viewmodel.SearchViewModelTest --no-daemon
```

Expected: module compile succeeds and `SearchViewModelTest` passes before any refactor begins.

- [ ] **Step 2: Create `SearchState.kt` and move the generic state UI first**

Add:

```kotlin
package co.anitrend.search.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.anitrend.search.R

@Composable
internal fun SearchState(
    title: String,
    subtitle: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            onRetry?.also {
                OutlinedButton(onClick = it, modifier = Modifier.padding(top = 8.dp)) {
                    Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
                }
            }
        }
    }
}
```

Then delete the original `private fun SearchState(...)` from `SearchCompose.kt`.

- [ ] **Step 3: Create `SearchChrome.kt` and move the query/scope controls**

Add:

```kotlin
package co.anitrend.search.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import co.anitrend.search.R
import co.anitrend.search.component.viewmodel.SearchScope

@Composable
internal fun SearchBarContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) { /* move body unchanged from SearchCompose.kt */ }

@Composable
internal fun SearchScopeChips(
    scope: SearchScope,
    onScopeClick: (SearchScope) -> Unit,
) { /* move body unchanged from SearchCompose.kt */ }

@Composable
internal fun SearchScope.label(): String =
    when (this) {
        SearchScope.HOME -> stringResource(R.string.label_search_scope_home)
        SearchScope.ALL -> stringResource(R.string.label_search_scope_all)
        SearchScope.ANIME -> stringResource(R.string.label_search_scope_anime)
        SearchScope.MANGA -> stringResource(R.string.label_search_scope_manga)
        SearchScope.USERS -> stringResource(R.string.label_search_scope_users)
        SearchScope.STUDIOS -> stringResource(R.string.label_search_scope_studios)
        SearchScope.STAFF -> stringResource(R.string.label_search_scope_staff)
        SearchScope.CHARACTERS -> stringResource(R.string.label_search_scope_characters)
    }
```

Then delete the original `SearchBarContent`, `SearchScopeChips`, and `SearchScope.label()` declarations from `SearchCompose.kt`.

- [ ] **Step 4: Create `SearchScreenContent.kt` with only the top-level coordinator**

Start the file with the public entry point and move only orchestration code into it:

```kotlin
package co.anitrend.search.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R
import co.anitrend.search.component.viewmodel.SearchScope
import co.anitrend.search.component.viewmodel.SearchViewModel

@Composable
fun SearchScreenContent(
    settings: IUserSettings,
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onSeeAllClick: (SearchScope) -> Unit,
    onExitScope: () -> Unit,
    onMediaItemClick: (IParam) -> Unit,
    onUserClick: (IParam) -> Unit,
    onStudioClick: (IParam) -> Unit,
    onStaffClick: (IParam) -> Unit,
    onCharacterClick: (IParam) -> Unit,
) {
    // move the current SearchScreenContent body unchanged,
    // still calling SearchSection/SearchDrillDown/UserDrillDown/etc.
}
```

Keep the body behavior-identical. Do not rename callbacks or alter the scope-switching `when` logic during this step.

- [ ] **Step 5: Run the module compile and regression test after the shared-file split**

Run:

```bash
./gradlew :feature:search:compileDebugKotlin :feature:search:testDebugUnitTest --tests co.anitrend.search.component.viewmodel.SearchViewModelTest --no-daemon
```

Expected: PASS. `SearchCompose.kt` can still contain domain sections at this stage, but the public entry point should already live in `SearchScreenContent.kt`.

## Task 2: Extract media sections into the media-owned file

**Files:**
- Create: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchMediaSections.kt`
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchScreenContent.kt`
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt`

- [ ] **Step 1: Create `SearchMediaSections.kt` and move media preview/drill-down declarations**

Add:

```kotlin
package co.anitrend.search.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCompactItem
import co.anitrend.common.media.ui.compose.item.MediaPosterListItem
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R

@Composable
internal fun SearchSection(
    title: String,
    items: LazyPagingItems<Media>,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    onSeeAllClick: () -> Unit,
) { /* move body unchanged */ }

@Composable
internal fun SearchDrillDown(
    items: LazyPagingItems<Media>,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) { /* move body unchanged */ }

@Composable
private fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit,
) { /* move body unchanged */ }
```

Keep `SectionHeader` here for now as the shared section header used by the non-media section files in later tasks; widen visibility to `internal` if needed.

- [ ] **Step 2: Make the shared section header visible to later section files**

Adjust the helper in `SearchMediaSections.kt` to:

```kotlin
@Composable
internal fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        TextButton(onClick = onSeeAllClick) {
            Text(text = stringResource(R.string.action_search_see_all))
        }
    }
}
```

This keeps the helper shared without introducing an extra file that the approved design did not ask for.

- [ ] **Step 3: Update `SearchScreenContent.kt` to import the moved media composables implicitly via same-package access**

The coordinator body should still call:

```kotlin
SearchSection(
    title = stringResource(R.string.label_search_media_all),
    items = allMedia,
    mediaPreferenceData = mediaPreferenceData,
    onMediaItemClick = onMediaItemClick,
    onSeeAllClick = { onSeeAllClick(SearchScope.ALL) },
)
```

Do not rename `SearchSection` or `SearchDrillDown` in this refactor; minimizing API churn keeps the move safer.

- [ ] **Step 4: Remove the original media declarations from `SearchCompose.kt`**

Delete these original declarations after the new file compiles:

```kotlin
private fun SearchSection(...)
private fun SearchDrillDown(...)
private fun SectionHeader(...)
```

- [ ] **Step 5: Verify after the media extraction**

Run:

```bash
./gradlew :feature:search:compileDebugKotlin :feature:search:testDebugUnitTest --tests co.anitrend.search.component.viewmodel.SearchViewModelTest --no-daemon
```

Expected: PASS.

## Task 3: Extract user and studio ownership files

**Files:**
- Create: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchUserSections.kt`
- Create: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchStudioSections.kt`
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt`

- [ ] **Step 1: Create `SearchUserSections.kt` and move all user-specific composables**

Add:

```kotlin
package co.anitrend.search.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R

@Composable internal fun UserSearchSection(...) { /* move body unchanged */ }
@Composable internal fun UserDrillDown(...) { /* move body unchanged */ }
@Composable private fun UserCompactRow(...) { /* move body unchanged */ }
@Composable private fun UserListItem(...) { /* move body unchanged */ }
```

- [ ] **Step 2: Create `SearchStudioSections.kt` and move the studio model + studio composables**

Add:

```kotlin
package co.anitrend.search.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.navigation.StudioRouter
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R

private data class StudioSearchItemUiModel(...)

private fun Studio.toSearchUiModel() = when (this) {
    is Studio.Core -> StudioSearchItemUiModel(id, name, isAnimationStudio, favourites, siteUrl, image)
    is Studio.Extended -> StudioSearchItemUiModel(id, name, isAnimationStudio, favourites, siteUrl, image)
}

@Composable internal fun StudioSearchSection(...) { /* move body unchanged */ }
@Composable internal fun StudioDrillDown(...) { /* move body unchanged */ }
@Composable private fun StudioSearchCompactRow(...) { /* move body unchanged */ }
@Composable private fun StudioSearchListRow(...) { /* move body unchanged */ }
@Composable private fun AvatarImageBadge(...) { /* move body unchanged */ }
```

Keep `AvatarImageBadge` local to the studio file because it is only used by studio rows today.

- [ ] **Step 3: Remove the original user/studio declarations from `SearchCompose.kt`**

Delete the original copies of:

```kotlin
private fun UserSearchSection(...)
private fun UserDrillDown(...)
private fun UserCompactRow(...)
private fun UserListItem(...)
private data class StudioSearchItemUiModel(...)
private fun Studio.toSearchUiModel()
private fun StudioSearchSection(...)
private fun StudioDrillDown(...)
private fun StudioSearchCompactRow(...)
private fun StudioSearchListRow(...)
private fun AvatarImageBadge(...)
```

- [ ] **Step 4: Verify after the user/studio extraction**

Run:

```bash
./gradlew :feature:search:compileDebugKotlin :feature:search:testDebugUnitTest --tests co.anitrend.search.component.viewmodel.SearchViewModelTest --no-daemon
```

Expected: PASS.

## Task 4: Extract character and staff ownership files

**Files:**
- Create: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCharacterSections.kt`
- Create: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchStaffSections.kt`
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt`

- [ ] **Step 1: Create `SearchCharacterSections.kt` and move all character-specific code**

Add:

```kotlin
package co.anitrend.search.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.domain.character.entity.Character as DomainCharacter
import co.anitrend.navigation.CharacterRouter
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R

private fun characterDisplayName(item: DomainCharacter): String =
    item.name?.userPreferred ?: item.name?.full ?: ""

@Composable internal fun CharacterSearchSection(...) { /* move body unchanged */ }
@Composable internal fun CharacterDrillDown(...) { /* move body unchanged */ }
@Composable private fun CharacterSearchCard(...) { /* move body unchanged */ }
@Composable private fun CharacterSearchListItem(...) { /* move body unchanged */ }
```

- [ ] **Step 2: Create `SearchStaffSections.kt` and move all staff-specific code**

Add:

```kotlin
package co.anitrend.search.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.domain.staff.entity.Staff
import co.anitrend.navigation.StaffRouter
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R

private fun staffDisplayName(item: Staff): String = item.name?.userPreferred ?: item.name?.full ?: ""

@Composable
private fun staffRoleLabel(item: Staff): String =
    item.primaryOccupations.firstOrNull() ?: stringResource(R.string.label_search_staff_fallback_role)

@Composable internal fun StaffSearchSection(...) { /* move body unchanged */ }
@Composable internal fun StaffDrillDown(...) { /* move body unchanged */ }
@Composable private fun StaffSearchCard(...) { /* move body unchanged */ }
@Composable private fun StaffSearchListItem(...) { /* move body unchanged */ }
```

- [ ] **Step 3: Remove the original character/staff declarations from `SearchCompose.kt`**

Delete the original copies of:

```kotlin
private fun characterDisplayName(...)
private fun CharacterSearchSection(...)
private fun CharacterDrillDown(...)
private fun CharacterSearchCard(...)
private fun CharacterSearchListItem(...)
private fun staffDisplayName(...)
private fun staffRoleLabel(...)
private fun StaffSearchSection(...)
private fun StaffDrillDown(...)
private fun StaffSearchCard(...)
private fun StaffSearchListItem(...)
```

- [ ] **Step 4: Verify after the character/staff extraction**

Run:

```bash
./gradlew :feature:search:compileDebugKotlin :feature:search:testDebugUnitTest --tests co.anitrend.search.component.viewmodel.SearchViewModelTest --no-daemon
```

Expected: PASS.

## Task 5: Final cleanup, preview isolation, and monolith removal

**Files:**
- Create: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchPreview.kt`
- Delete: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt`
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchScreenContent.kt`

- [ ] **Step 1: Create `SearchPreview.kt` and move the preview entry point out of production files**

Add:

```kotlin
package co.anitrend.search.component.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme

@AniTrendPreview.Default
@Composable
private fun SearchScreenPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        SearchState(
            title = "Search preview",
            subtitle = "Media sections will render from paging data.",
            modifier = Modifier.padding(16.dp),
        )
    }
}
```

- [ ] **Step 2: Delete the now-empty `SearchCompose.kt` source file**

After confirming all declarations were moved, remove the file entirely:

```bash
rm feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt
```

Expected result: all search Compose code now lives in ownership-based files, with `SearchScreenContent.kt` as the public entry point.

- [ ] **Step 3: Run final verification for the feature module**

Run:

```bash
./gradlew :feature:search:compileDebugKotlin :feature:search:testDebugUnitTest --tests co.anitrend.search.component.viewmodel.SearchViewModelTest --no-daemon
```

Expected: PASS.

Then run a broader compile check if the branch has room for it:

```bash
./gradlew :feature:search:assembleDebug --no-daemon
```

Expected: PASS, confirming the refactor did not break downstream feature packaging.

- [ ] **Step 4: Review the resulting file boundaries before considering the work done**

Check these questions manually:

```text
- Does SearchScreenContent.kt only orchestrate state, scope routing, and callbacks?
- Do media/user/studio/staff/character files each own both preview and drill-down rendering?
- Are SearchChrome.kt and SearchState.kt still narrow shared files?
- Is there any reusable helper sitting in the coordinator file that should live nearer the domain it serves?
```

Do not broaden scope if the answers are “good enough”; this refactor is complete once ownership is clear and behavior is preserved.

## Self-Review

- Spec coverage check: covered package preservation, ownership-based file split, thin coordinator, preview isolation, behavior preservation, and unchanged `SearchViewModelTest` verification.
- Placeholder scan: no `TODO`, `TBD`, or “write tests later” placeholders were left in the task steps.
- Type consistency: file names, composable names, and verification commands match the current feature module naming.
