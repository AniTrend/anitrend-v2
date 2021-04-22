/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.common.media.ui.widget.progress

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.extension.ext.*
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.widget.progress.controller.MediaProgressController
import co.anitrend.core.android.extensions.dp
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.media.entity.contract.IMedia
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
internal class MediaProgressWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), CustomView {

    private val progressText = MaterialTextView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).also { params ->
            params.gravity = Gravity.CENTER_VERTICAL
        }
        setTextColor(context.getCompatColor(R.color.colorBackground))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        setTypeface(typeface, Typeface.BOLD)
    }

    private val progressIncrement = AppCompatImageView(context).apply {
        layoutParams = LayoutParams(14.dp, 14.dp).also { params ->
            params.gravity = Gravity.CENTER_VERTICAL
        }
        setImageDrawable(
            this.context.getCompatDrawable(
                R.drawable.ic_add,
                R.color.colorBackground
            )
        )
    }

    private val progressSpinner = CircularProgressIndicator(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).also { params ->
            params.gravity = Gravity.CENTER_VERTICAL
        }
        indicatorSize = 10.dp
        trackCornerRadius = 8.dp
        trackThickness = 2.dp
        isIndeterminate = false
    }

    init {
        onInit(context, attrs, defStyleAttr)
    }

    private fun setUpClickListener(currentProgress: Int) {
        isFocusable = true
        setOnClickListener {
            //TODO: Call task-media-list to update media list progress
            val lifecycleOwner = it.context as LifecycleOwner
            lifecycleOwner.lifecycleScope.launch {
                runCatching {
                    if (!progressSpinner.isIndeterminate) {
                        progressIncrement.gone()
                        progressSpinner.hide()
                        progressSpinner.isIndeterminate = true
                        progressSpinner.show()
                        delay(500)
                        val progress = currentProgress.plus(1)
                        progressSpinner.setProgressCompat(progress, true)
                        progressIncrement.visible()
                    } else
                        Toast.makeText(it.context, "I'm still busy..", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun removeClickListener() {
        isFocusable = false
        setOnClickListener(null)
    }

    private fun initialise(controller: MediaProgressController) {
        val currentProgress = controller.getCurrentProgress()

        progressSpinner.max = controller.getMaximumProgress()
        progressSpinner.progress = currentProgress
        progressText.text = controller.getCurrentProgressText()

        if (currentProgress == 0)
            progressSpinner.gone()

        if (controller.isIncrementPossible()) {
            progressIncrement.visible()
            setUpClickListener(currentProgress)
        }
        else {
            progressIncrement.gone()
            removeClickListener()
        }

        if (controller.hasCaughtUp())
            progressSpinner.setIndicatorColor(
                context.getCompatColor(R.color.green_A700)
            )
        else
            progressSpinner.setIndicatorColor(
                context.getCompatColor(R.color.orange_A700)
            )
    }

    /**
     * @param media Media
     */
    fun setupUsingMedia(media: IMedia, settings: IAuthenticationSettings) {
        val controller = MediaProgressController(media, settings)
        if (controller.shouldHideWidget()) {
            gone()
            return
        }
        visible()
        initialise(controller)
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        addView(progressSpinner)
        addView(progressText)
        addView(progressIncrement)

        progressText.updateMargins(start = 8.dp, end = 8.dp)
        background = context.getCompatDrawable(R.drawable.widget_background)

        if (isInEditMode) {
            progressText.text = "5 / 25"
            progressSpinner.max = 25
            progressSpinner.progress = 5
        }
    }

    /**
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider calling this in [android.view.View.onDetachedFromWindow]
     */
    override fun onViewRecycled() {
        super.onViewRecycled()
        removeClickListener()
    }
}