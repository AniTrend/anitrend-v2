package co.anitrend.data.model.response.meta

import android.os.Parcelable
import co.anitrend.data.repository.media.attributes.ScoreFormat
import kotlinx.android.parcel.Parcelize

/** [MediaListOptions](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistoptions.doc.html)
 * A user's list options
 *
 * @param scoreFormat The score format the user is using for media lists
 * @param rowOrder The default order list rows should be displayed in
 * @param animeList The user's anime list options
 * @param mangaList The user's manga list options
 */
@Parcelize
data class MediaListOptions(
    @ScoreFormat
    val scoreFormat: String,
    val rowOrder: String,
    val animeList: MediaListTypeOptions?,
    val mangaList: MediaListTypeOptions?
): Parcelable