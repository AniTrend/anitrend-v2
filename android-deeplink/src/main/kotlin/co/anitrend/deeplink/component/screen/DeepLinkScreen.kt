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

package co.anitrend.deeplink.component.screen

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.inject
import co.anitrend.deeplink.R
import co.anitrend.deeplink.databinding.DeepLinkScreenBinding
import co.anitrend.deeplink.exception.DeepLinkException
import com.kingsleyadio.deeplink.DeepLinkParser
import com.kingsleyadio.deeplink.DeepLinkUri
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class DeepLinkScreen : AniTrendScreen<DeepLinkScreenBinding>() {

    private val stateLayoutConfig by inject<StateLayoutConfig>()
    private val router by inject<DeepLinkParser<Intent?>>()

    private fun handleIntentData() {
        when (val uri = intent.data) {
            null -> {
                requireBinding().stateLayout.loadStateFlow.value =
                    LoadState.Error(DeepLinkException.MissingIntentData())
            }
            else -> {
                val deepLinkUri = DeepLinkUri.parse(uri.toString())
                val intent = router.parse(deepLinkUri)
                if (intent == null) {
                    requireBinding().stateLayout.loadStateFlow.value =
                        LoadState.Error(DeepLinkException.InvalidScreenIntent())
                }
                startActivity(intent)
                ActivityCompat.finishAfterTransition(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeepLinkScreenBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
        requireBinding().stateLayout.stateConfigFlow.value =
            stateLayoutConfig.copy(retryAction = R.string.label_text_action_ok)
        requireBinding().stateLayout.loadStateFlow.value =
            LoadState.Loading()
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenResumed {
            requireBinding().stateLayout.interactionFlow
                .debounce(resources.getInteger(R.integer.debounce_duration_short).toLong())
                .onEach { finishAfterTransition() }
                .catch { cause: Throwable ->
                    Timber.e(cause)
                }.collect()
        }
        lifecycleScope.launchWhenResumed {
            handleIntentData()
        }
    }
}