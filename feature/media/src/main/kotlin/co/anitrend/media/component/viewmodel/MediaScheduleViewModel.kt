package co.anitrend.media.component.viewmodel

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.domain.airing.enums.AiringSort
import co.anitrend.domain.airing.interactor.AiringScheduleUseCase
import co.anitrend.domain.airing.model.AiringParam
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.model.sorting.Sorting
import org.threeten.bp.Instant

/**
 * ViewModel that exposes upcoming airing schedule for a specific media id.
 */
class MediaScheduleViewModel(
    private val interactor: AiringScheduleUseCase.GetPaged<DataState<PagedList<Media>>>,
) : AniTrendViewModelState<PagedList<Media>>() {
    /**
     * Load upcoming schedule for provided [mediaId]
     */
    operator fun invoke(mediaId: Long) {
        val nowEpochSec = Instant.now().epochSecond.toInt()
        val param =
            AiringParam.Find(
                mediaId = mediaId,
                airingAt_greater = nowEpochSec,
                notYetAired = true,
                sort = listOf(Sorting(AiringSort.TIME, SortOrder.ASC)),
            )
        val result = interactor(param)
        state.postValue(result)
    }
}
