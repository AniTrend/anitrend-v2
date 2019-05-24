package co.anitrend.data.model.response.general.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.data.model.response.meta.CoverImage
import co.anitrend.data.model.response.meta.Favourites
import co.anitrend.data.model.response.meta.MediaListOptions
import kotlinx.android.parcel.Parcelize

/** [User](https://anilist.github.io/ApiV2-GraphQL-Docs/user.doc.html)
 * A user from the anilist platform
 *
 * @param id The id of the user
 * @param name The name of the user
 * @param about The bio written by user
 * @param avatar The user's avatar images
 * @param bannerImage The user's banner images
 * @param isFollowing If the authenticated user if following this user
 * @param options The user's general options
 * @param mediaListOptions The user's media list options
 * @param favourites The users favourites
 * @param stats The user's statistics
 * @param unreadNotificationCount The number of unread notifications the user has
 * @param siteUrl The url for the user page on the AniList website
 * @param donatorTier The donation tier of the user
 * @param moderatorStatus If the user is a moderator or data moderator
 * @param updatedAt When the user's data was last updated
 */
@Entity
@Parcelize
data class User(
    @PrimaryKey
    val id: Long,
    val name: String,
    val about: String?,
    val avatar: CoverImage?,
    val bannerImage: String?,
    val isFollowing: Boolean?,
    val options: UserOptions?,
    val mediaListOptions: MediaListOptions?,
    val favourites: Favourites?,
    val stats: UserStats?,
    val unreadNotificationCount: Int?,
    val siteUrl: String?,
    val donatorTier: Int?,
    val moderatorStatus: String?,
    val updatedAt: Int?
): Parcelable
