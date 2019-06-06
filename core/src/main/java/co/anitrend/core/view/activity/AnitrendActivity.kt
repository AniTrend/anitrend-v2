package co.anitrend.core.view.activity

import io.wax911.support.core.presenter.SupportPresenter
import io.wax911.support.ui.activity.SupportActivity

/**
 * Abstract application based activity for anitrend, avoids further modification of the
 * support library, any feature additions should be added through extensions
 */
abstract class AnitrendActivity<M, P: SupportPresenter<*>>: SupportActivity<M, P>() {

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val systemUiOptions = window.decorView.systemUiVisibility
            when (@AppCompatDelegate.NightMode val nightMode = AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_NO -> {
                    window.navigationBarColor = getCompatColor(R.color.colorPrimary)
                    window.decorView.systemUiVisibility = systemUiOptions or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }
                AppCompatDelegate.MODE_NIGHT_YES -> {
                    window.navigationBarColor = getCompatColor(R.color.colorPrimary)
                    window.decorView.systemUiVisibility = systemUiOptions and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    window.decorView.systemUiVisibility = systemUiOptions and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                else -> {
                    // According to Google/IO other ui options like auto and follow system might be deprecated
                }
            }
        }*/
    }
}