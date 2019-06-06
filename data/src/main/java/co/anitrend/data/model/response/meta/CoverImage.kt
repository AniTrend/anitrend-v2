package co.anitrend.data.model.response.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** [CoverImage](https://anilist.github.io/ApiV2-GraphQL-Docs/mediacoverimage.doc.html)
 * Shared model between [co.anitrend.data.model.response.general.media.Media] &
 * [co.anitrend.data.model.response.general.user.User]
 *
 * @param large The cover image of media at its largest size
 * @param medium The cover image of media at medium size
 */
@Parcelize
data class CoverImage(
    val large: String?,
    val medium: String?
): Parcelable