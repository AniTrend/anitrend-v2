package co.anitrend.search.component.viewmodel

import androidx.paging.PagingData
import co.anitrend.data.character.GetSearchCharacterInteractor
import co.anitrend.data.media.GetPagingMediaInteractor
import co.anitrend.data.staff.GetPagingStaffInteractor
import co.anitrend.data.studio.GetSearchStudioInteractor
import co.anitrend.data.user.GetSearchUserInteractor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mediaInteractor = mockk<GetPagingMediaInteractor>()
    private val userSearchInteractor = mockk<GetSearchUserInteractor>()
    private val studioInteractor = mockk<GetSearchStudioInteractor>()
    private val staffInteractor = mockk<GetPagingStaffInteractor>()
    private val characterInteractor = mockk<GetSearchCharacterInteractor>()

    private lateinit var viewModel: SearchViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { mediaInteractor.invoke(any()) } returns flowOf(PagingData.empty())
        every { userSearchInteractor.getPaged(any()) } returns flowOf(PagingData.empty())
        every { studioInteractor.getStudioPaged(any()) } returns flowOf(PagingData.empty())
        every { staffInteractor.invoke(any()) } returns flowOf(PagingData.empty())
        every { characterInteractor.invoke(any()) } returns flowOf(PagingData.empty())

        viewModel = SearchViewModel(
            mediaInteractor = mediaInteractor,
            userSearchInteractor = userSearchInteractor,
            studioInteractor = studioInteractor,
            staffInteractor = staffInteractor,
            characterInteractor = characterInteractor,
        )
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty query`() {
        assertEquals("", viewModel.query.value)
    }

    @Test
    fun `initial state has HOME scope`() {
        assertEquals(SearchScope.HOME, viewModel.scope.value)
    }

    @Test
    fun `onQueryChange updates query state`() {
        viewModel.onQueryChange("Naruto")
        assertEquals("Naruto", viewModel.query.value)
    }

    @Test
    fun `onQueryChange trims whitespace via submitSearch call in init`() = runTest {
        val testVm = createViewModel()
        testVm.onQueryChange("  Bleach  ")
        testVm.submitSearch()
        assertEquals("Bleach", testVm.query.value)
        assertEquals("Bleach", testVm.submittedQuery.value)
    }

    @Test
    fun `submitSearch sets both query and submittedQuery`() {
        viewModel.submitSearch("One Piece")
        assertEquals("One Piece", viewModel.query.value)
        assertEquals("One Piece", viewModel.submittedQuery.value)
    }

    @Test
    fun `submitSearch defaults to current query value`() {
        viewModel.onQueryChange("Dragon Ball")
        viewModel.submitSearch()
        assertEquals("Dragon Ball", viewModel.query.value)
        assertEquals("Dragon Ball", viewModel.submittedQuery.value)
    }

    @Test
    fun `debounced submit receives trimmed query after delay`() = runTest {
        val testVm = createViewModel()
        testVm.onQueryChange("  Attack on Titan  ")
        assertEquals("", testVm.submittedQuery.value)

        testScheduler.advanceUntilIdle()
        assertEquals("Attack on Titan", testVm.submittedQuery.value)
    }

    @Test
    fun `debounced submit ignores rapid intermediate changes`() = runTest {
        val testVm = createViewModel()
        testVm.onQueryChange("N")
        testVm.onQueryChange("Na")
        testVm.onQueryChange("Nar")
        testVm.onQueryChange("Naruto")

        assertEquals("", testVm.submittedQuery.value)

        testScheduler.advanceUntilIdle()
        assertEquals("Naruto", testVm.submittedQuery.value)
    }

    @Test
    fun `showScope changes current scope`() {
        viewModel.showScope(SearchScope.ALL)
        assertEquals(SearchScope.ALL, viewModel.scope.value)

        viewModel.showScope(SearchScope.ANIME)
        assertEquals(SearchScope.ANIME, viewModel.scope.value)

        viewModel.showScope(SearchScope.MANGA)
        assertEquals(SearchScope.MANGA, viewModel.scope.value)

        viewModel.showScope(SearchScope.USERS)
        assertEquals(SearchScope.USERS, viewModel.scope.value)
    }

    @Test
    fun `showHome resets scope to HOME`() {
        viewModel.showScope(SearchScope.ALL)
        assertEquals(SearchScope.ALL, viewModel.scope.value)

        viewModel.showHome()
        assertEquals(SearchScope.HOME, viewModel.scope.value)
    }

    @Test
    fun `each SearchScope entry is defined`() {
        val entries = SearchScope.entries
        assertEquals(8, entries.size)

        val expected = setOf(
            SearchScope.HOME,
            SearchScope.ALL,
            SearchScope.ANIME,
            SearchScope.MANGA,
            SearchScope.USERS,
            SearchScope.STUDIOS,
            SearchScope.STAFF,
            SearchScope.CHARACTERS,
        )
        assertEquals(expected, entries.toSet())
    }

    @Test
    fun `all entity flows are not null`() {
        assertNotNull(viewModel.mediaAll)
        assertNotNull(viewModel.mediaAnime)
        assertNotNull(viewModel.mediaManga)
        assertNotNull(viewModel.studios)
        assertNotNull(viewModel.staff)
        assertNotNull(viewModel.characters)
        assertNotNull(viewModel.users)
    }

    private fun createViewModel(): SearchViewModel {
        val media = mockk<GetPagingMediaInteractor>()
        val user = mockk<GetSearchUserInteractor>()
        val studio = mockk<GetSearchStudioInteractor>()
        val staff = mockk<GetPagingStaffInteractor>()
        val character = mockk<GetSearchCharacterInteractor>()

        every { media.invoke(any()) } returns flowOf(PagingData.empty())
        every { user.getPaged(any()) } returns flowOf(PagingData.empty())
        every { studio.getStudioPaged(any()) } returns flowOf(PagingData.empty())
        every { staff.invoke(any()) } returns flowOf(PagingData.empty())
        every { character.invoke(any()) } returns flowOf(PagingData.empty())

        return SearchViewModel(
            mediaInteractor = media,
            userSearchInteractor = user,
            studioInteractor = studio,
            staffInteractor = staff,
            characterInteractor = character,
        )
    }
}