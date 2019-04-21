package co.anitrend.core.presenter

import android.content.Context
import co.anitrend.core.util.Settings
import co.anitrend.core.util.graphql.GraphUtil
import io.wax911.support.core.factory.InstanceCreator
import io.wax911.support.core.presenter.SupportPresenter

class SharedPresenter private constructor(context: Context?): SupportPresenter<Settings>(context) {

    /**
     * Provides the preference object to the lazy initializer
     *
     * @return instance of the preference class
     */
    override fun createPreference(): Settings? =
        context?.let { ctx ->
            Settings.newInstance(ctx)
        }

    /**
     * Provides pagination size for calculating offsets
     */
    override fun paginationSize(): Int = GraphUtil.PAGING_LIMIT

    companion object: InstanceCreator<SharedPresenter, Context?>({
        SharedPresenter(it)
    })
}