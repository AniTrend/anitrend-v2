package co.anitrend.data.util.graphql

import co.anitrend.data.model.query.PageQuery
import io.github.wax911.library.model.request.QueryContainerBuilder

/**
 * Graph request helper class
 */
object GraphUtil {

    /**
     * Default per page loading limit for this application
     */
    const val PAGING_LIMIT = 30

    /**
     * Builder provider helper method, that provides a default GraphQL Query and Variable Builder
     * @param pageQuery paging
     */
    fun getDefaultQuery(pageQuery: PageQuery? = null): QueryContainerBuilder {
        val queryContainer = QueryContainerBuilder()
        pageQuery?.apply {
            queryContainer.putVariables(toMap())
        }
        return queryContainer
    }
}