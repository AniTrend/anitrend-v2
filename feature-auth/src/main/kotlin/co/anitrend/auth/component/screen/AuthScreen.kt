/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.auth.component.screen

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.anitrend.arch.extension.ext.extra
import co.anitrend.auth.component.viewmodel.AuthViewModel
import co.anitrend.auth.databinding.AuthScreenBinding
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.commit
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.navigation.AuthRouter
import co.anitrend.navigation.MainRouter
import co.anitrend.navigation.extensions.forActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthScreen : AniTrendScreen<AuthScreenBinding>() {

    private val viewModel by viewModel<AuthViewModel>()
    private val authRouterParam by extra<AuthRouter.Param>(AuthRouter.Param.KEY)

    private fun checkIntentData() {
        viewModel.onIntentData(
            applicationContext,
            authRouterParam
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthScreenBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
    }

    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            onUpdateUserInterface()
        }
    }

    /**
     * {@inheritDoc}
     *
     * Handle onNewIntent() to inform the fragment manager that the
     * state is not saved.  If you are handling new intents and may be
     * making changes to the fragment state, you want to be sure to call
     * through to the super-class here first.  Otherwise, if your state
     * is saved but the activity is not stopped, you could get an
     * onNewIntent() call which happens before onResume() and trying to
     * perform fragment operations at that point will throw IllegalStateException
     * because the fragment manager thinks the state is still saved.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        checkIntentData()
    }

    /**
     * Obtain an [Intent] that will launch an explicit target activity specified by
     * this activity's logical parent. The logical parent is named in the application's manifest
     * by the [parentActivityName][android.R.attr.parentActivityName] attribute.
     * Activity subclasses may override this method to modify the Intent returned by
     * super.getParentActivityIntent() or to implement a different mechanism of retrieving
     * the parent intent entirely.
     *
     * @return a new Intent targeting the defined parent of this activity or null if
     * there is no valid parent.
     */
    override fun getParentActivityIntent(): Intent? {
        return MainRouter.forActivity(
            context = applicationContext
        )
    }

    private fun onUpdateUserInterface() {
        currentFragmentTag = FragmentItem(AuthRouter.forFragment())
            .commit(requireBinding().splashFrame, this)
    }
}
