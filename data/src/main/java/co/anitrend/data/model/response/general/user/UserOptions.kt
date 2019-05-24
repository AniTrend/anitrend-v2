package co.anitrend.data.model.response.general.user

import android.os.Parcelable
import co.anitrend.data.repository.user.UserTitleLanguage
import kotlinx.android.parcel.Parcelize

/** [UserOptions](https://anilist.github.io/ApiV2-GraphQL-Docs/useroptions.doc.html)
 * A user's general options
 *
 * @param titleLanguage The language the user wants to see media titles in
 * @param displayAdultContent Whether the user has enabled viewing of 18+ content
 * @param airingNotifications Whether the user receives notifications when a show they are watching airs
 * @param profileColor Profile highlight color (blue, purple, pink, orange, red, green, gray)
 */
@Parcelize
data class UserOptions(
    @UserTitleLanguage
    val titleLanguage: String,
    val displayAdultContent: Boolean,
    val airingNotifications: Boolean,
    val profileColor: String
): Parcelable