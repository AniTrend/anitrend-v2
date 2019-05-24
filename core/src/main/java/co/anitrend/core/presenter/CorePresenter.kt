package co.anitrend.core.presenter

import android.content.Context
import co.anitrend.data.util.Settings
import co.anitrend.data.util.graphql.GraphUtil
import io.wax911.support.core.presenter.SupportPresenter


class CorePresenter(
    context: Context?,
    settings: Settings
): SupportPresenter<Settings>(context, settings) {

    /**
     * Provides pagination size for calculating offsets
     */
    override fun paginationSize(): Int = GraphUtil.PAGING_LIMIT
}