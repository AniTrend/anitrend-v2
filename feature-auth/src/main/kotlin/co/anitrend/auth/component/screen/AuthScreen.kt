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

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import co.anitrend.auth.component.viewmodel.AuthViewModel
import co.anitrend.auth.databinding.AuthScreenBinding
import co.anitrend.core.component.screen.AnitrendScreen
import co.anitrend.core.ui.commit
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.navigation.AuthRouter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AuthScreen : AnitrendScreen<AuthScreenBinding>() {

    private val viewModel by viewModel<AuthViewModel>()

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
        lifecycleScope.launchWhenResumed {
            viewModel.onIntentData(applicationContext, intent.data)
        }
    }

    private fun onUpdateUserInterface() {
        currentFragmentTag = FragmentItem(fragment = AuthRouter.forFragment())
            .commit(requireBinding().splashFrame, this)
    }
}