/*
 * Copyright (C) 2019  AniTrend
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
