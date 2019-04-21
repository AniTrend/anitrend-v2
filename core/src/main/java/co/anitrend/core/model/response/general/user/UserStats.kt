package co.anitrend.core.model.response.general.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** [UserStats](https://anilist.github.io/ApiV2-GraphQL-Docs/userstats.doc.html)
 * A user's statistics
 *
 * @param watchedTime The amount of anime the user has watched in minutes
 * @param chaptersRead The amount of manga chapters the user has read
 */
@Parcelize
data class UserStats(
    // TODO("Pending implementation")
    val watchedTime: Int,
    val chaptersRead: Int
): Parcelable