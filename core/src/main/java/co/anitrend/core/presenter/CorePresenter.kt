package co.anitrend.core.presenter

import android.content.Context
import co.anitrend.core.util.Settings
import co.anitrend.core.util.graphql.GraphUtil
import io.wax911.support.core.presenter.SupportPresenter
import org.koin.core.KoinComponent
import org.koin.core.inject


class CorePresenter(context: Context?): SupportPresenter<Settings>(context), KoinComponent {

    /**
     * Provides the preference object to the lazy initializer
     *
     * @return instance of the preference class
     */
    override fun createPreference() = inject<Settings>().value

    /**
     * Provides pagination size for calculating offsets
     */
    override fun paginationSize(): Int = GraphUtil.PAGING_LIMIT
}