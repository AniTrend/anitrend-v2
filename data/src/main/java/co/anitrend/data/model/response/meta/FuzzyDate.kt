package co.anitrend.data.model.response.meta

import android.os.Parcelable
import androidx.annotation.IntRange
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
): Parcelable {

    /** [FuzzyDateInt](https://anilist.github.io/ApiV2-GraphQL-Docs/fuzzydateint.doc.html)
     *
     * 8 digit long date integer (YYYYMMDD).
     * Unknown dates represented by 0.
     *
     * E.g. 2016: 20160000, May 1976: 19760500
     */
    fun toFuzzyDateInt(): String {
        if (year == 0 && month == 0 && day == 0)
            return "0"
        val fuzzyDateYear = when {
            year < 10 -> "000$year"
            year < 100 -> "00$year"
            year < 1000 -> "0$year"
            else -> "$year"
        }
        val fuzzyDateMonth = when {
            month <= 9 -> "0$month"
            else -> "$month"
        }
        val fuzzyDateDay = when {
            day <= 9 -> "0$day"
            else -> "$day"
        }
        return "$fuzzyDateYear$fuzzyDateMonth$fuzzyDateDay"
    }

    companion object {
        /**
         * Default representation of null or not set
         */
        const val UNKNOWN = 0
    }
}