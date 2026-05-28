package co.anitrend.studio.component.viewmodel

import co.anitrend.arch.data.state.DataState
import co.anitrend.data.studio.StudioDetailInteractor
import co.anitrend.domain.studio.entity.StudioDetailData
import co.anitrend.domain.studio.model.StudioParam
import co.anitrend.navigation.StudioRouter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class StudioViewModelTest {
    private val interactor = mockk<StudioDetailInteractor>()
    private val viewModel = StudioViewModel(interactor = interactor)

    @Test
    fun `invoke ignores router params without id`() {
        viewModel(StudioRouter.StudioParam(id = null, name = "Bones"))

        verify(exactly = 0) { interactor.getStudio(any()) }
    }

    @Test
    fun `retryCurrent replays last invoke id`() {
        val dataState = mockk<DataState<StudioDetailData>>(relaxed = true)
        every { interactor.getStudio(any()) } returns dataState

        viewModel(StudioRouter.StudioParam(id = 184L, name = "Kyoto Animation"))
        viewModel.retryCurrent()

        verify(exactly = 2) { interactor.getStudio(StudioParam.Detail(id = 184L)) }
    }

    @Test
    fun `retryCurrent before invoke does nothing`() {
        viewModel.retryCurrent()

        verify(exactly = 0) { interactor.getStudio(any()) }
    }
}
