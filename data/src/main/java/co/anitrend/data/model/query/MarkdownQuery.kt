package co.anitrend.data.model.query

import co.anitrend.data.model.contract.IGraphQuery

/** [Markdown query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * Provide AniList markdown to be converted to html (Requires auth)
 *
 * @param markdown: The markdown to be parsed to html
 */
data class MarkdownQuery(
    val markdown: String
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "markdown" to markdown
    )
}