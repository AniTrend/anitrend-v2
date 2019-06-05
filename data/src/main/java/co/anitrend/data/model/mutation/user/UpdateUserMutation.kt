package co.anitrend.data.model.mutation.user

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.model.input.MediaListOptionsInput
import co.anitrend.data.model.input.NotificationOption
import co.anitrend.data.repository.media.attributes.ScoreFormat
import co.anitrend.data.repository.user.attributes.UserTitleLanguage

/** [UpdateUser mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * @param about User's about/bio text
 * @param titleLanguage User's title language
 * @param displayAdultContent If the user should see media marked as adult-only
 * @param airingNotifications If the user should get notifications when a show they are watching aires
 * @param scoreFormat The user's list scoring system
 * @param rowOrder The user's default list order
 * @param profileColor Profile highlight color
 * @param notificationOptions Notification Options
 * @param animeListOptions The user's anime list options
 * @param mangaListOptions The user's anime list options
 */
data class UpdateUserMutation(
    val about: String?,
    val titleLanguage: UserTitleLanguage,
    val displayAdultContent: Boolean = false,
    val airingNotifications: Boolean = true,
    val scoreFormat: ScoreFormat,
    val rowOrder: String,
    val profileColor: String,
    val notificationOptions: List<NotificationOption>,
    val animeListOptions: MediaListOptionsInput,
    val mangaListOptions: MediaListOptionsInput
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "about" to about,
        "titleLanguage" to titleLanguage,
        "displayAdultContent" to displayAdultContent,
        "airingNotifications" to airingNotifications,
        "scoreFormat" to scoreFormat,
        "rowOrder" to rowOrder,
        "profileColor" to profileColor,
        "notificationOptions" to notificationOptions,
        "animeListOptions" to animeListOptions.toMap(),
        "mangaListOptions" to mangaListOptions.toMap()
    )
}