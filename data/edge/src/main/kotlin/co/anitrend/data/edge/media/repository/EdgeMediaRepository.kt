package co.anitrend.data.edge.media.repository

import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.data.edge.media.source.contract.EdgeMediaSource
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.media.repository.IMediaRepository

internal class EdgeMediaRepository(
    private val source: EdgeMediaSource,
) : IMediaRepository.Detail<DataState<Media>> {
    override fun getMedia(param: MediaParam.Detail): DataState<Media> = source create source(param.id.toInt())
}
