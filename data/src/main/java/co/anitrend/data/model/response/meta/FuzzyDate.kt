package co.anitrend.data.model.response.meta

import android.os.Parcelable
import androidx.annotation.IntRange
import co.anitrend.data.model.contract.IGraphQuery
import kotlinx.android.parcel.Parcelize

/** [FuzzyDate](https://anilist.github.io/ApiV2-GraphQL-Docs/fuzzydate.doc.html)
 * Date object that allows for incomplete date values (fuzzy) Unknown dates represented by 0.
 *
 * @param year Year annotated to have a minimum of 0
 * @param month Month annotated to have a minimum of 0 and maximum of 12
 * @param day Day annotated to have a minimum of 0 and maximum of 31
 */
@Parcelize
data class FuzzyDate(
    @IntRange(from = 0)
    val year: Int = UNKNOWN,
    @IntRange(from = 0, to = 12)
    val month: Int = UNKNOWN,
    @IntRange(from = 0, to = 31)
    val day: Int = UNKNOWN
) : IGraphQuery, Parcelable {

    /**
     * Checks if all date fields are not set
     *
     * @return [Boolean] true if the date fields are set to [UNKNOWN] otherwise false
     */
    fun isDateNotSet() = day == UNKNOWN && month == UNKNOWN && year == UNKNOWN

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "year" to year,
        "month" to month,
        "day" to day
    )

    companion object {
        /**
         * Default representation of null or not set
         */
        const val UNKNOWN = 0
    }
}