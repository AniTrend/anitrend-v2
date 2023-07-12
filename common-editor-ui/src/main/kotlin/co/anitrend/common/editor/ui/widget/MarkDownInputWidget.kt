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

package co.anitrend.common.editor.ui.widget

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.core.content.ContextCompat
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.core.view.inputmethod.InputContentInfoCompat
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.extension.ext.dipToPx
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.editor.R
import co.anitrend.core.android.koinOf
import com.google.android.material.textfield.TextInputEditText
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import io.wax911.emojify.EmojiManager
import io.wax911.emojify.parser.parseToHtmlHexadecimal
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import com.google.android.material.R as MaterialR

class MarkDownInputWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = MaterialR.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr), CustomView, ActionMode.Callback,
    InputConnectionCompat.OnCommitContentListener {

    private val markwonEditorTextWatcher by lazy(UNSAFE) {
        MarkwonEditorTextWatcher.withProcess(
            koinOf()
        )
    }

    val editableFlow: Flow<String> = callbackFlow {
        val textWatcher = object : TextWatcher {

            /**
             * This method is called to notify you that, within `s`,
             * the `count` characters beginning at `start`
             * are about to be replaced by new text with length `after`.
             * It is an error to attempt to make changes to `s` from
             * this callback.
             */
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Nothing to do here
            }

            /**
             * This method is called to notify you that, within `s`,
             * the `count` characters beginning at `start`
             * have just replaced old text that had length `before`.
             * It is an error to attempt to make changes to `s` from
             * this callback.
             */
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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
                    trySendBlocking(text).onFailure {
                        Timber.w(it, "Failed to emmit text changes")
                    }
                }
            }
        }

        addTextChangedListener(textWatcher)

        awaitClose {
            removeTextChangedListener(textWatcher)
        }
    }

    /**
     * @return composed text as hex html entities
     */
    val formattedText: String?
        get() {
            val content = text.toString()
            val manager = koinOf<EmojiManager>()
            return manager.parseToHtmlHexadecimal(content)
        }

    val isEmpty: Boolean
        get() = text.isNullOrBlank()

    init { onInit(context, attrs, defStyleAttr) }

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        maxHeight = 200f.dipToPx
        setHintTextColor(
            context.getCompatColor(co.anitrend.arch.theme.R.color.primaryTextColor)
        )
        isVerticalScrollBarEnabled = true
        inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or
                InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE or
                InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
        setBackgroundColor(
            ContextCompat.getColor(
                context,
                android.R.color.transparent
            )
        )
    }

    /**
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider calling this in [android.view.View.onDetachedFromWindow]
     */
    override fun onViewRecycled() {
        super.onViewRecycled()
        if (!isInEditMode)
            removeTextChangedListener(
                markwonEditorTextWatcher
            )
        customSelectionActionModeCallback = null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!isInEditMode)
            addTextChangedListener(
                markwonEditorTextWatcher
            )
        customSelectionActionModeCallback = this
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        val inputConnection = super.onCreateInputConnection(outAttrs)
        EditorInfoCompat.setContentMimeTypes(outAttrs, supportedMimeTypes)
        return inputConnection?.let {
            InputConnectionCompat.createWrapper(it, outAttrs, this)
        }
    }

    /**
     * This is called when the view is detached from a window.  At this point it
     * no longer has a surface for drawing.
     *
     * @see .onAttachedToWindow
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onViewRecycled()
    }

    /**
     * Called when action mode is first created. The menu supplied will be used to
     * generate action buttons for the action mode.
     *
     * @param mode ActionMode being created
     * @param menu Menu used to populate action buttons
     * @return true if the action mode should be created, false if entering this
     * mode should be aborted.
     */
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    /**
     * Called to refresh an action mode's action menu whenever it is invalidated.
     *
     * @param mode ActionMode being prepared
     * @param menu Menu used to populate action buttons
     * @return true if the menu or action mode was updated, false otherwise.
     */
    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    /**
     * Called to report a user click on an action button.
     *
     * @param mode The current ActionMode
     * @param item The item that was clicked
     * @return true if this callback handled the event, false if the standard MenuItem
     * invocation should continue.
     */
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return false
    }

    /**
     * Called when an action mode is about to be exited and destroyed.
     *
     * @param mode The current ActionMode being destroyed
     */
    override fun onDestroyActionMode(mode: ActionMode?) {
        // Nothing to do here
    }

    /**
     * Intercepts InputConnection#commitContent API calls.
     *
     * @param inputContentInfo content to be committed
     * @param flags `0` or [.INPUT_CONTENT_GRANT_READ_URI_PERMISSION]
     * @param opts optional bundle data. This can be `null`
     * @return `true` if this request is accepted by the application, no matter if the
     * request is already handled or still being handled in background. `false` to use the
     * default implementation
     */
    override fun onCommitContent(
        inputContentInfo: InputContentInfoCompat,
        flags: Int,
        opts: Bundle?
    ): Boolean {
        runCatching {
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 &&
                flags and InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION != 0
            ) inputContentInfo.requestPermission()

            val linkUri = inputContentInfo.linkUri
            if (linkUri != null) {
                text?.insert(selectionStart, linkUri.toString())
                inputContentInfo.releasePermission()
                return true
            }
            inputContentInfo.releasePermission()
        }.onFailure {
            Timber.w(it)
        }

        return false
    }

    private companion object {
        val supportedMimeTypes = arrayOf("image/png", "image/gif")

        const val MARKDOWN_BOLD = "__"
        const val MARKDOWN_ITALIC = "_"
        const val MARKDOWN_STRIKE = "~~"
        const val MARKDOWN_NUMBER = "1. "
        const val MARKDOWN_BULLET = "- "
        const val MARKDOWN_HEADING = "# "
        const val MARKDOWN_CENTER_ALIGN = "~~~"
        const val MARKDOWN_QUOTE = "> "
        const val MARKDOWN_CODE = "`"
    }
}
