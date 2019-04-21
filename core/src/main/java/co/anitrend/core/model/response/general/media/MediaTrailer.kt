package co.anitrend.core.model.response.general.media

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** [MediaTrailer](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatrailer.doc.html)
 * Media trailer or advertisement
 *
 * @param id The trailer video id
 * @param site The site the video is hosted by (Currently either youtube or dailymotion
 */
@Parcelize
data class MediaTrailer(
    val id: String?,
    val site: String?
): Parcelable