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
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import co.anitrend.arch.extension.ext.*
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.presenter.MediaPresenter
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.extensions.stackTrace
import co.anitrend.domain.media.entity.contract.IMedia
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView
import timber.log.Timber

@SuppressLint("SetTextI18n")
internal class MediaProgressWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), CustomView {

    private var presenter: MediaPresenter? = null

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

    private fun isLoading(loading: Boolean) {
        progressIncrement.gone()
        progressSpinner.hide()
        progressSpinner.isIndeterminate = loading
        progressSpinner.show()
        progressIncrement.visible()
    }

    private fun setUpClickListener(presenter: MediaPresenter) {
        isFocusable = true
        setOnClickListener {
            runCatching {
                if (!progressSpinner.isIndeterminate) {
                    isLoading(true)
                    presenter()
                }
            }.onFailure {
                isLoading(false)
                Timber.e(it)
            }
        }
    }

    private fun removeClickListener() {
        isFocusable = false
        setOnClickListener(null)
    }

    private fun initialise() = presenter?.run {
        val currentProgress = controller.getCurrentProgress()

        progressSpinner.max = controller.getMaximumProgress()
        progressSpinner.progress = currentProgress
        progressText.text = getCurrentProgressText()

        if (currentProgress == 0)
            progressSpinner.gone()

        if (controller.isIncrementPossible()) {
            progressIncrement.visible()
            setUpClickListener(this)
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
    fun setupUsingMedia(media: IMedia, settings: Settings) {
        presenter = MediaPresenter(context, settings, media)

        if (presenter == null || presenter?.controller?.shouldHideWidget() == true) {
            gone()
            return
        } else
            visible()

        isLoading(false)
        initialise()
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
        presenter = null
    }
}