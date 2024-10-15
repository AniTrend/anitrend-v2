/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.common.medialist.ui.widget.contract

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.extension.ext.getCompatDrawableAttr
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.medialist.ui.R
import co.anitrend.core.android.extensions.dp
import com.airbnb.paris.extensions.style
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

abstract class AbstractEditWidget
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : LinearLayoutCompat(context, attrs, defStyleAttr), CustomView {
        open val supportsMaximumWidget: Boolean = true

        protected val incrementWidget =
            AppCompatImageView(context).apply {
                layoutParams =
                    LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    ).also {
                        it.gravity = Gravity.CENTER_VERTICAL
                    }
                isClickable = true
                isFocusable = true
                setPadding(8.dp)
                background =
                    context.getCompatDrawableAttr(
                        android.R.attr.selectableItemBackground,
                    )
                setImageDrawable(
                    context.getCompatDrawable(
                        R.drawable.ic_arrow_up,
                        co.anitrend.core.android.R.color.colorAccent,
                    ),
                )
            }

        protected val decrementWidget =
            AppCompatImageView(context).apply {
                layoutParams =
                    LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    ).also {
                        it.gravity = Gravity.CENTER_VERTICAL
                    }
                isClickable = true
                isFocusable = true
                setPadding(8.dp)
                background =
                    context.getCompatDrawableAttr(
                        android.R.attr.selectableItemBackground,
                    )
                setImageDrawable(
                    context.getCompatDrawable(
                        R.drawable.ic_arrow_down,
                        co.anitrend.core.android.R.color.colorAccent,
                    ),
                )
            }

        protected val currentWidget =
            EditText(context).apply {
                style(com.google.android.material.R.style.Widget_MaterialComponents_TextView)
                TextViewCompat.setTextAppearance(
                    this,
                    com.google.android.material.R.style.TextAppearance_MaterialComponents_Subtitle1,
                )
                layoutParams =
                    LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                    )
                inputType = EditorInfo.TYPE_CLASS_NUMBER
            }

        protected val maximumWidget =
            MaterialTextView(context).apply {
                style(com.google.android.material.R.style.Widget_MaterialComponents_TextView)
                TextViewCompat.setTextAppearance(
                    this,
                    com.google.android.material.R.style.TextAppearance_MaterialComponents_Subtitle1,
                )
                layoutParams =
                    LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
            }

        val currentEditableFlow: Flow<String> =
            callbackFlow {
                val textWatcher =
                    object : TextWatcher {
                        /**
                         * This method is called to notify you that, within `s`,
                         * the `count` characters beginning at `start`
                         * are about to be replaced by new text with length `after`.
                         * It is an error to attempt to make changes to `s` from
                         * this callback.
                         */
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int,
                        ) {
                            // Nothing to do here
                        }

                        /**
                         * This method is called to notify you that, within `s`,
                         * the `count` characters beginning at `start`
                         * have just replaced old text that had length `before`.
                         * It is an error to attempt to make changes to `s` from
                         * this callback.
                         */
                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int,
                        ) {
                            // Nothing to do here
                        }

                        /**
                         * This method is called to notify you that, somewhere within
                         * `s`, the text has been changed.
                         * It is legitimate to make further changes to `s` from
                         * this callback, but be careful not to get yourself into an infinite
                         * loop, because any changes you make will cause this method to be
                         * called again recursively.
                         * (You are not told where the change took place because other
                         * afterTextChanged() methods may already have made other changes
                         * and invalidated the offsets.  But if you need to know here,
                         * you can use [Spannable.setSpan] in [.onTextChanged]
                         * to mark your place and then look up from here where the span
                         * ended up.
                         */
                        override fun afterTextChanged(s: Editable?) {
                            if (s != null) {
                                val text = s.toString()
                                onCurrentTextChanged(text)
                                trySendBlocking(text).onFailure {
                                    Timber.w(it, "Failed to emmit text changes")
                                }
                            }
                        }
                    }

                currentWidget.addTextChangedListener(textWatcher)

                awaitClose {
                    currentWidget.removeTextChangedListener(textWatcher)
                }
            }

        /**
         * Called after [currentWidget] text has been changed
         */
        protected abstract fun onCurrentTextChanged(number: String)

        /**
         * Callable in view constructors to perform view inflation and attribute initialization
         *
         * @param context view context
         * @param attrs view attributes if applicable
         * @param styleAttr style attribute if applicable
         */
        override fun onInit(
            context: Context,
            attrs: AttributeSet?,
            styleAttr: Int?,
        ) {
            addView(decrementWidget)
            addView(currentWidget)
            if (supportsMaximumWidget) {
                addView(maximumWidget)
            }
            addView(incrementWidget)
        }

        /**
         * Should be called on a view's detach from window to unbind or release object references
         * and cancel all running coroutine jobs if the current view
         *
         * Consider calling this in [android.view.View.onDetachedFromWindow]
         */
        override fun onViewRecycled() {
            super.onViewRecycled()
            currentWidget.keyListener = null
            incrementWidget.setOnClickListener(null)
            decrementWidget.setOnClickListener(null)
        }

        override fun onDetachedFromWindow() {
            onViewRecycled()
            super.onDetachedFromWindow()
        }
    }
