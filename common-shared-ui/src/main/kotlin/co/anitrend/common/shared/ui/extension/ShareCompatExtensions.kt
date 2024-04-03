package co.anitrend.common.shared.ui.extension

import androidx.core.app.ShareCompat.IntentBuilder
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.extensions.stackTrace

/**
 * Triggers system intent chooser for easier content sharing
 *
 * @param action Delegates the complexities of creating an intent chooser
 */
inline fun FragmentActivity.shareContent(
    action: IntentBuilder.() -> IntentBuilder,
) = runCatching {
    val intentBuilder = IntentBuilder(this)
    action(intentBuilder)
        .createChooserIntent()
}.onSuccess {
    it.run(::startActivity)
}.stackTrace()
