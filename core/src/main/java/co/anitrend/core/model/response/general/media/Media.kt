package co.anitrend.core.model.response.general.media

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/** [Media](https://anilist.github.io/ApiV2-GraphQL-Docs/media.doc.html)
 * Anime or Manga representation
 *
 * @param id The id of the media
 */
@Entity
@Parcelize
data class Media(
    // TODO("Pending implementation")
    @PrimaryKey
    val id: Int
): Parcelable