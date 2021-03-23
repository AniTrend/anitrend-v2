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

package co.anitrend.common.markdown.ui.widget

import android.content.Context
import android.text.TextUtils
import android.text.util.Linkify
import android.util.AttributeSet
import androidx.core.widget.TextViewCompat
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.markdown.R
import co.anitrend.core.android.koin.MarkdownFlavour
import co.anitrend.core.android.koinOf
import co.anitrend.core.android.startViewIntent
import co.anitrend.core.android.themeStyle
import co.anitrend.domain.common.entity.contract.IEntityName
import co.anitrend.domain.common.entity.contract.ISynopsis
import com.google.android.material.textview.MaterialTextView
import io.noties.markwon.Markwon
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import org.koin.core.qualifier.named

class MarkdownTextWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr), CustomView {

    init { onInit(context, attrs, defStyleAttr) }

    fun setText(synopsis: ISynopsis, entity: IEntityName) {
        val markwon = koinOf<Markwon>(named(MarkdownFlavour.ANILIST))
        if (entity.alternative.isEmpty())
            maxLines = 7
        val description = synopsis.description?.toString()
            ?: context.getString(R.string.label_place_holder_no_description)
        markwon.setMarkdown(this, description)
    }

    fun setText(synopsis: ISynopsis) {
        val markwon = koinOf<Markwon>(named(MarkdownFlavour.ANILIST))
        val description = synopsis.description?.toString()
            ?: context.getString(R.string.label_place_holder_no_description)
        markwon.setMarkdown(this, description)
    }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        maxLines = 4
        ellipsize = TextUtils.TruncateAt.END
        setTextIsSelectable(true)
        Linkify.addLinks(this, Linkify.ALL)
        val textAppearance = context.themeStyle(R.attr.textAppearanceCaption)
        TextViewCompat.setTextAppearance(this, textAppearance)
        movementMethod = BetterLinkMovementMethod.newInstance().apply {
            setOnLinkClickListener { view, url ->
                view.startViewIntent(url)
                true
            }
            setOnLinkLongClickListener { _, _ ->
                // Handle long-click or return false to let the framework handle this link.
                false
            }
        }
    }

    /**
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider calling this in [android.view.View.onDetachedFromWindow]
     */
    override fun onViewRecycled() {
        with (movementMethod) {
            if (this is BetterLinkMovementMethod) {
                setOnLinkClickListener(null)
                setOnLinkLongClickListener(null)
            }
        }
    }

    /**
     * This is called when the view is detached from a window.  At this point it
     * no longer has a surface for drawing.
     *
     * @see .onAttachedToWindow
     */
    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }
}