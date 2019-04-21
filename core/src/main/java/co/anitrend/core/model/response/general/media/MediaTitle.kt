package co.anitrend.core.model.response.general.media

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** [MediaTitle](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatitle.doc.html)
 * The official titles of the media in various languages
 *
 * @param romaji The romanization of the native language title
 * @param english The official english title
 * @param native Official title in it's native language
 * @param userPreferred The currently authenticated users preferred title language.
 * Default romaji for non-authenticated requests
 */
@Parcelize
data class MediaTitle(
    val romaji: String?,
    val english: String?,
    val native: String?,
    val userPreferred: String?
): Parcelable