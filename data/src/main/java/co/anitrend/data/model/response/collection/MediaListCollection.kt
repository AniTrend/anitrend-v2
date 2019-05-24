package co.anitrend.data.model.response.collection

import android.os.Parcelable
import co.anitrend.data.model.response.group.MediaListGroup
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/** [MediaListCollection](https://anilist.github.io/ApiV2-GraphQL-Docs/medialistcollection.doc.html)
 * List of anime or manga
 *
 * @param lists Grouped media list entries
 */
@Parcelize
data class MediaListCollection(
    val lists: @RawValue List<MediaListGroup>
): Parcelable