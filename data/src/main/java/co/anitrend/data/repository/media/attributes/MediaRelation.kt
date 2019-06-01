package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media Relation values
 */
@StringDef(
    MediaRelation.ADAPTATION,
    MediaRelation.PREQUEL,
    MediaRelation.SEQUEL,
    MediaRelation.PARENT,
    MediaRelation.SIDE_STORY,
    MediaRelation.CHARACTER,
    MediaRelation.SUMMARY,
    MediaRelation.ALTERNATIVE,
    MediaRelation.SPIN_OFF,
    MediaRelation.OTHER
)
annotation class MediaRelation {
    companion object {
        /** An adaption of the media into a different format */
        const val ADAPTATION = "ADAPTATION"
        /** Released before the relation */
        const val PREQUEL = "PREQUEL"
        /** Released after the relation */
        const val SEQUEL = "SEQUEL"
        /** The media a side story is from */
        const val PARENT = "PARENT"
        /** A side story of the parent media */
        const val SIDE_STORY = "SIDE_STORY"
        /** Shares at least 1 character */
        const val CHARACTER = "CHARACTER"
        /** A shortened and summarized version */
        const val SUMMARY = "SUMMARY"
        /** An alternative version of the same media */
        const val ALTERNATIVE = "ALTERNATIVE"
        /** An alternative version of the media with a different primary focus */
        const val SPIN_OFF = "SPIN_OFF"
        /** Other */
        const val OTHER = "OTHER"

        val ALL = listOf(
            ADAPTATION,
            PREQUEL,
            SEQUEL,
            PARENT,
            SIDE_STORY,
            CHARACTER,
            SUMMARY,
            ALTERNATIVE,
            SPIN_OFF,
            OTHER
        )
    }
}