/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.about.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.liveData
import androidx.work.WorkManager
import co.anitrend.about.component.compose.AboutScreenContent
import co.anitrend.about.component.viewmodel.AboutViewModel
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.core.android.compose.design.ContentWrapper
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.navigation.model.common.IParam
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class AboutScreen : AniTrendScreen() {
    private val viewModel by viewModel<AboutViewModel>()
    private val workManager by lazy { WorkManager.getInstance(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrendTheme3 {
                ContentWrapper(
                    stateFlow = liveData { emit(LoadState.Idle()) },
                    param = IParam.None,
                ) {
                    AboutScreenContent(
                        workItemFlow = viewModel.invoke(workManager),
                        onBackPress = onBackPressedDispatcher::onBackPressed,
                        onCancelWork = { taskId ->
                            workManager.cancelWorkById(UUID.fromString(taskId))
                        },
                    )
                }
            }
        }
    }
}
