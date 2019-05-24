package co.anitrend.data.model.response.general.media

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * If AniList ever duplicates genres we're screwed! R.I.P
 */
@Entity
@Parcelize
data class MediaGenre(
    @PrimaryKey
    val genre: String
): Parcelable