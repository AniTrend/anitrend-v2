package co.anitrend.core.auth.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.core.auth.AuthenticationHelper
import kotlinx.android.parcel.Parcelize
import okhttp3.internal.http2.Header

/**
 * Json Web Token from Implicit Grant see implicit grant manual docs at:
 * https://anilist.gitbook.io/anilist-apiv2-docs/overview/oauth/implicit-grant
 */
@Entity
@Parcelize
data class JsonWebToken(
    @PrimaryKey
    val id: Long,
    val accessToken: String
): Parcelable {
    fun getTokenKey() = "Bearer $accessToken"
}