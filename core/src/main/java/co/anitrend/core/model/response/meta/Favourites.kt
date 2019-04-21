package co.anitrend.core.model.response.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** [Favourites](https://anilist.github.io/ApiV2-GraphQL-Docs/favourites.doc.html)
 * User's favourite anime, manga, characters, staff & studios
 *
 */
@Parcelize
data class Favourites(
    // TODO("Pending implementation")
    val anime: String
): Parcelable