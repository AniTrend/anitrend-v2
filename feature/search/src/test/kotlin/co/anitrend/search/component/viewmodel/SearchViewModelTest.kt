package co.anitrend.search.component.viewmodel

import androidx.paging.PagingData
import co.anitrend.data.character.GetSearchCharacterInteractor
import co.anitrend.data.media.GetPagingMediaInteractor
import co.anitrend.data.staff.GetPagingStaffInteractor
import co.anitrend.data.studio.GetSearchStudioInteractor
import co.anitrend.data.user.GetSearchUserInteractor
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.SearchRouter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun `submitSearch trims whitespace and updates submitted search`() = runTest {
        val testVm = createViewModel()
        testVm.onQueryChange("  Bleach  ")
        testVm.submitSearch()
        assertEquals("Bleach", testVm.query.value)
        assertEquals("Bleach", testVm.submittedSearch.value.query)
    }

    @Test
    fun `submitSearch sets both query and submitted search query`() {
        viewModel.submitSearch("One Piece")
        assertEquals("One Piece", viewModel.query.value)
        assertEquals("One Piece", viewModel.submittedSearch.value.query)
    }

    @Test
    fun `submitSearch defaults to current query value`() {
        viewModel.onQueryChange("Dragon Ball")
        viewModel.submitSearch()
        assertEquals("Dragon Ball", viewModel.query.value)
        assertEquals("Dragon Ball", viewModel.submittedSearch.value.query)
    }

    @Test
    fun `onQueryChange does not auto submit search`() = runTest {
        val testVm = createViewModel()
        testVm.onQueryChange("  Attack on Titan  ")
        testScheduler.advanceUntilIdle()

        assertEquals("", testVm.submittedSearch.value.query)
    }

    @Test
    fun `initialize applies deeplink filters and destination`() {
        val testVm = createViewModel()
        testVm.initialize(
            SearchRouter.SearchParam(
                query = "Cowboy Bebop",
                genres = listOf("Action", "Sci-Fi"),
                year = 1998,
                season = MediaSeason.SPRING,
                format = MediaFormat.TV,
                status = MediaStatus.FINISHED,
                destination = SearchRouter.Destination.ANIME,
            ),
        )

        assertEquals("Cowboy Bebop", testVm.query.value)
        assertEquals("Cowboy Bebop", testVm.submittedSearch.value.query)
        assertEquals(listOf("Action", "Sci-Fi"), testVm.submittedSearch.value.genres)
        assertEquals(1998, testVm.submittedSearch.value.year)
        assertEquals(MediaSeason.SPRING, testVm.submittedSearch.value.season)
        assertEquals(MediaFormat.TV, testVm.submittedSearch.value.format)
        assertEquals(MediaStatus.FINISHED, testVm.submittedSearch.value.status)
        assertEquals(SearchScope.ANIME, testVm.scope.value)
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

    @Test
    fun `blank query does not trigger media interactor`() = runTest {
        val media = mockk<GetPagingMediaInteractor>(relaxed = true)
        val user = mockk<GetSearchUserInteractor>(relaxed = true)
        val studio = mockk<GetSearchStudioInteractor>(relaxed = true)
        val staff = mockk<GetPagingStaffInteractor>(relaxed = true)
        val character = mockk<GetSearchCharacterInteractor>(relaxed = true)

        val testVm = SearchViewModel(
            mediaInteractor = media,
            userSearchInteractor = user,
            studioInteractor = studio,
            staffInteractor = staff,
            characterInteractor = character,
        )

        // submittedQuery starts blank — no interactor should be invoked
        advanceUntilIdle()

        verify(exactly = 0) { media.invoke(any()) }
        verify(exactly = 0) { user.getPaged(any()) }
        verify(exactly = 0) { studio.getStudioPaged(any()) }
        verify(exactly = 0) { staff.invoke(any()) }
        verify(exactly = 0) { character.invoke(any()) }
    }

    @Test
    fun `submitting search preserves deeplink media filters in submitted state`() = runTest {
        val testVm =
            SearchViewModel(
                mediaInteractor = mediaInteractor,
                userSearchInteractor = userSearchInteractor,
                studioInteractor = studioInteractor,
                staffInteractor = staffInteractor,
                characterInteractor = characterInteractor,
            )

        testVm.initialize(
            SearchRouter.SearchParam(
                genres = listOf("Action"),
                year = 2020,
                season = MediaSeason.FALL,
                format = MediaFormat.TV,
                status = MediaStatus.RELEASING,
                destination = SearchRouter.Destination.ANIME,
            ),
        )
        advanceUntilIdle()

        val mediaParam = testVm.submittedSearch.value.toMediaParam(type = MediaType.ANIME)

        assertEquals(listOf("Action"), mediaParam.genre_in)
        assertEquals(2020, mediaParam.seasonYear)
        assertEquals(MediaSeason.FALL, mediaParam.season)
        assertEquals(MediaFormat.TV, mediaParam.format)
        assertEquals(MediaStatus.RELEASING, mediaParam.status)
        assertEquals(MediaType.ANIME, mediaParam.type)
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
