/*
 * Copyright (C) 2021  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.common.medialist.ui.widget.status

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.medialist.ui.R
import co.anitrend.common.medialist.ui.widget.status.adapter.StatusIconAdapter
import co.anitrend.domain.medialist.enums.MediaListStatus
import com.airbnb.paris.extensions.style
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class StatusSpinnerWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatSpinner(context, attrs, defStyleAttr), CustomView {

    private val statusIconAdapter = StatusIconAdapter(
        context,
        R.layout.adapter_status_spiner_item,
        R.id.statusSpinnerText,
        MediaListStatus.values().toList()
    )

    val selectionChangeFlow: Flow<MediaListStatus> = callbackFlow {
        val selectionChangeListener = object : OnItemSelectedListener {
            /**
             * Callback method to be invoked when an item in this view has been
             * selected. This callback is invoked only when the newly selected
             * position is different from the previously selected position or if
             * there was no selected item.
             *
             * Implementers can call getItemAtPosition(position) if they need to access the
             * data associated with the selected item.
             *
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                trySendBlocking(currentSelection()).onFailure {
                    Timber.w(it, "Failed to offer current selection to $view")
                }
            }

            /**
             * Callback method to be invoked when the selection disappears from this
             * view. The selection can disappear for instance when touch is activated
             * or when the adapter becomes empty.
             *
             * @param parent The AdapterView that now contains no selected item.
             */
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Not going to publish anything
            }
        }
        onItemSelectedListener = selectionChangeListener
        awaitClose {
            onItemSelectedListener = null
        }
    }

    init { onInit(context, attrs, defStyleAttr) }

    fun currentSelection(): MediaListStatus {
        return selectedItem as MediaListStatus
    }

    fun setCurrentSelection(mediaListStatus: MediaListStatus) {
        setSelection(mediaListStatus.ordinal)
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        adapter = statusIconAdapter
        if (isInEditMode)
            setCurrentSelection(MediaListStatus.PLANNING)
    }

    /**
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider calling this in [android.view.View.onDetachedFromWindow]
     */
    override fun onViewRecycled() {
        super.onViewRecycled()
    }

    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }
}