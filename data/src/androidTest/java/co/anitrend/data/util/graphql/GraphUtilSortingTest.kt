package co.anitrend.data.util.graphql

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.data.repository.media.attributes.MediaSortContract
import co.anitrend.data.repository.medialist.attributes.MediaListSortContract
import co.anitrend.data.util.Settings
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class GraphUtilSortingTest {

    private val appContext by lazy { InstrumentationRegistry.getInstrumentation().context }

    @Test
    fun sorting_helper_does_not_append_postfix_to_ignored_values() {
        val given = Settings(
            context = appContext
        ).apply {
            isSortOrderDescending = true
        }

        val expected = "SEARCH_MATCH"

        val actual = GraphUtil.applySortOrderOn(
            sortType = MediaSortContract.SEARCH_MATCH,
            settings = given
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun sorting_helper_appends_postfix_to_non_ignored_values() {
        val given = Settings(
            context = appContext
        ).apply {
            isSortOrderDescending = true
        }

        val expected = "PROGRESS_VOLUMES_DESC"

        val actual = GraphUtil.applySortOrderOn(
            sortType = MediaListSortContract.PROGRESS_VOLUMES,
            settings = given
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun sorting_helper_does_not_append_postfix_when_preference_order_is_not_set_to_descending_order() {
        val given = Settings(
            context = appContext
        ).apply {
            isSortOrderDescending = false
        }

        val expected = "PROGRESS_VOLUMES"

        val actual = GraphUtil.applySortOrderOn(
            sortType = MediaListSortContract.PROGRESS_VOLUMES,
            settings = given
        )

        Assert.assertEquals(expected, actual)
    }
}