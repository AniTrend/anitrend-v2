package co.anitrend.navigation.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import co.anitrend.navigation.model.NavPayload
import co.anitrend.navigation.router.NavigationRouter
import timber.log.Timber


/**
 * Builds an activity intent from the navigation component
 */
fun NavigationRouter.forActivity(
    context: Context,
    navPayload: NavPayload? = null,
    flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK,
    action: String = Intent.ACTION_VIEW,
): Intent? {
    val intent = provider.activity(context)
    intent?.flags = flags
    intent?.action = action
    navPayload?.also {
        intent?.putExtra(it.key, it.param)
    }
    return intent
}

/**
 * Builds an activity intent and starts it
 */
fun NavigationRouter.startActivity(
    context: Context?,
    navPayload: NavPayload? = null,
    flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK,
    action: String = Intent.ACTION_VIEW,
    options: Bundle? = null
) {
    runCatching {
        val intent = forActivity(
            requireNotNull(context),
            navPayload,
            flags,
            action
        )
        context.startActivity(intent, options)
    }.onFailure {
        Timber.tag(moduleTag).e(it)
    }
}

/**
 * Builds an activity intent and starts it
 */
fun NavigationRouter.startActivity(
    view: View?,
    navPayload: NavPayload? = null,
    flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK,
    action: String = Intent.ACTION_VIEW,
    options: Bundle? = null
) = startActivity(view?.context,navPayload, flags, action, options)
