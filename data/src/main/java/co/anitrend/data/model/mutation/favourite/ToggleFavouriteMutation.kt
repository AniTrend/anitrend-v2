package co.anitrend.data.model.mutation.favourite

import co.anitrend.data.model.contract.IGraphQuery

/** [ToggleFavourite mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Favourite or un/favourite an anime, manga, character, staff member, or studio
 *
 * @param animeId The id of the anime to un/favourite
 * @param mangaId The id of the manga to un/favourite
 * @param characterId The id of the character to un/favourite
 * @param staffId The id of the staff to un/favourite
 * @param studioId The id of the studio to un/favourite
 */
data class ToggleFavouriteMutation(
    val animeId: Int? = null,
    val mangaId: Int? = null,
    val characterId: Int? = null,
    val staffId: Int? = null,
    val studioId: Int? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "animeId" to animeId,
        "mangaId" to mangaId,
        "characterId" to characterId,
        "staffId" to staffId,
        "studioId" to studioId
    )
}