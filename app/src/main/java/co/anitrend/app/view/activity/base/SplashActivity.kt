package co.anitrend.app.view.activity.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import co.anitrend.app.R
import co.anitrend.app.databinding.ActivitySplashBinding
import co.anitrend.app.view.activity.index.MainActivity
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.view.activity.AnitrendActivity
import io.wax911.support.extension.startNewActivity
import io.wax911.support.ui.activity.SupportActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class SplashActivity : AnitrendActivity<Nothing, CorePresenter>() {

    private lateinit var binding: ActivitySplashBinding

    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return supportPresenter of the generic type specified
     */
    override val supportPresenter: CorePresenter by inject()

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragment]
     * then this method will be called in [SupportFragment.onCreate]. Otherwise [SupportActivity.onPostCreate]
     * invokes this function
     *
     * @see [SupportActivity.onPostCreate] and [SupportFragment.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        val description = getString(
            R.string.app_splash_description
        )
        binding.splashDescription.text = description
        makeRequest()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(
            this,
            R.layout.activity_splash
        )
    }

    override fun updateUI() {
        startNewActivity<MainActivity>(intent.extras)
        finish()
    }

    override fun makeRequest() {
        supportPresenter.syncMediaGenresAndTags()
        updateUI()
    }
}
