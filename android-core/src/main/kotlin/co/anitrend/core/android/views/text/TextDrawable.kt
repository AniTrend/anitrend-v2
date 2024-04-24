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

package co.anitrend.core.android.views.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.core.android.R
import com.google.android.material.textview.MaterialTextView

/**
 * A drawable that uses a text view as it's canvas
 */
class TextDrawable(
    context: Context,
    private val text: String?
) : Drawable() {

    private val materialTextView = MaterialTextView(context).also { textView ->
        textView.text = text
        textView.background = textView.context.getCompatDrawable(
            R.drawable.indicator_background, co.anitrend.arch.theme.R.color.colorBackground
        )
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.setTextColor(
            textView.context.getCompatColor(
                co.anitrend.arch.theme.R.color.colorOnBackground
            )
        )
        val padding = textView.resources
            .getDimensionPixelSize(R.dimen.text_drawable_margin)
        textView.setPadding(padding, padding, padding, padding)
    }

    /**
     * Specify a bounding rectangle for the Drawable. This is where the drawable
     * will draw when its draw() method is called.
     */
    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            right - left,
            View.MeasureSpec.EXACTLY
        )
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            bottom - top,
            View.MeasureSpec.EXACTLY
        )
        materialTextView.measure(widthMeasureSpec, heightMeasureSpec)
        materialTextView.layout(left, top, right, bottom)
    }

    /**
     * Draw in its bounds (set via setBounds) respecting optional effects such
     * as alpha (set via setAlpha) and color filter (set via setColorFilter).
     *
     * @param canvas The canvas to draw into
     */
    override fun draw(canvas: Canvas) {
        materialTextView.draw(canvas)
    }

    /**
     * Specify an alpha value for the drawable. 0 means fully transparent, and
     * 255 means fully opaque.
     */
    override fun setAlpha(alpha: Int) {
        materialTextView.alpha = alpha/255f
    }

    /**
     * Specify an optional color filter for the drawable.
     *
     * If a Drawable has a ColorFilter, each output pixel of the Drawable's
     * drawing contents will be modified by the color filter before it is
     * blended onto the render target of a Canvas.
     *
     * Pass `null` to remove any existing color filter.
     *
     * **Note:** Setting a non-`null` color
     * filter disables [tint][.setTintList].
     *
     * @param colorFilter The color filter to apply, or `null` to remove the
     * existing color filter
     */
    override fun setColorFilter(colorFilter: ColorFilter?) {
        // Not supported
    }

    /**
     * Return the opacity/transparency of this Drawable.  The returned value is
     * one of the abstract format constants in
     * [android.graphics.PixelFormat]:
     * [android.graphics.PixelFormat.UNKNOWN],
     * [android.graphics.PixelFormat.TRANSLUCENT],
     * [android.graphics.PixelFormat.TRANSPARENT], or
     * [android.graphics.PixelFormat.OPAQUE].
     *
     * An OPAQUE drawable is one that draws all all content within its bounds, completely
     * covering anything behind the drawable. A TRANSPARENT drawable is one that draws nothing
     * within its bounds, allowing everything behind it to show through. A TRANSLUCENT drawable
     * is a drawable in any other state, where the drawable will draw some, but not all,
     * of the content within its bounds and at least some content behind the drawable will
     * be visible. If the visibility of the drawable's contents cannot be determined, the
     * safest/best return value is TRANSLUCENT.
     *
     * Generally a Drawable should be as conservative as possible with the
     * value it returns.  For example, if it contains multiple child drawables
     * and only shows one of them at a time, if only one of the children is
     * TRANSLUCENT and the others are OPAQUE then TRANSLUCENT should be
     * returned.  You can use the method [.resolveOpacity] to perform a
     * standard reduction of two opacities to the appropriate single output.
     *
     * Note that the returned value does not necessarily take into account a
     * custom alpha or color filter that has been applied by the client through
     * the [.setAlpha] or [.setColorFilter] methods. Some subclasses,
     * such as [android.graphics.drawable.BitmapDrawable], [android.graphics.drawable.ColorDrawable], and [android.graphics.drawable.GradientDrawable],
     * do account for the value of [.setAlpha], but the general behavior is dependent
     * upon the implementation of the subclass.
     *
     * @return int The opacity class of the Drawable.
     *
     * @see android.graphics.PixelFormat
     */
    @Deprecated("Deprecated in Java", ReplaceWith("PixelFormat.UNKNOWN", "android.graphics.PixelFormat"))
    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}
