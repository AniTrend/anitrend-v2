package co.anitrend.core.view.activity

import io.wax911.support.custom.activity.SupportActivity
import io.wax911.support.custom.presenter.SupportPresenter

/**
 * Abstract application based activity for anitrend, avoids further modification of the
 * support library, any feature additions should be added through extensions
 */
abstract class AnitrendActivity<M, P: SupportPresenter<*>>: SupportActivity<M, P>()