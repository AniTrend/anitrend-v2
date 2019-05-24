package co.anitrend.data.model.response.general.media

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/** [MediaTag](https://anilist.github.io/ApiV2-GraphQL-Docs/mediatag.doc.html)
 * A tag that describes a theme or element of the media
 *
 * @param id The id of the tag
 * @param name The name of the tag
 * @param description A general description of the tag
 * @param category The categories of tags this tag belongs to
 * @param rank The relevance ranking of the tag out of the 100 for this media
 * @param isGeneralSpoiler If the tag could be a spoiler for any media
 * @param isMediaSpoiler If the tag is a spoiler for this media
 * @param isAdult If the tag is only for adult 18+ media
 */
@Entity
@Parcelize
data class MediaTag(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String?,
    val category: String?,
    val rank: Int?,
    val isGeneralSpoiler: Boolean?,
    val isMediaSpoiler: Boolean?,
    val isAdult: Boolean?
): Parcelable