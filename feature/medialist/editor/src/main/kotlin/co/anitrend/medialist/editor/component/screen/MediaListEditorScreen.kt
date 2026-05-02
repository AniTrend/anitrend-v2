/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.medialist.editor.component.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.core.ui.inject
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.medialist.editor.component.compose.MediaListEditorSheetScreen
import co.anitrend.medialist.editor.component.viewmodel.MediaListEditorViewModel
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaListTaskRouter
import co.anitrend.navigation.extensions.createOneTimeUniqueWorker
import co.anitrend.navigation.extensions.nameOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MediaListEditorScreen : AniTrendScreen() {
    private val viewModel by viewModel<MediaListEditorViewModel>()
    private val dateHelper by inject<AniTrendDateHelper>()
    private val settings by inject<IUserSettings>()

    private val param by extra<MediaListEditorRouter.MediaListEditorParam>(
        key = nameOf<MediaListEditorRouter.MediaListEditorParam>(),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scoreFormat by settings.scoreFormat.flow.collectAsStateWithLifecycle(ScoreFormat.POINT_10_DECIMAL)
            AniTrendTheme3 {
                LaunchedEffect(param) {
                    param?.also { viewModel(param = it) }
                        ?: Timber.e("MediaListEditor param is null when it should not be")
                }
                Surface {
                    MediaListEditorSheetScreen(
                        viewModel = viewModel,
                        scoreFormat = scoreFormat,
                        dateHelper = dateHelper,
                        onDismiss = onBackPressedDispatcher::onBackPressed,
                        onSave = {
                            MediaListTaskRouter
                                .forMediaListSaveEntryWorker()
                                .createOneTimeUniqueWorker(this@MediaListEditorScreen, it)
                                .enqueue()
                        },
                        onDelete = {
                            MediaListTaskRouter
                                .forMediaListDeleteEntryWorker()
                                .createOneTimeUniqueWorker(this@MediaListEditorScreen, it)
                                .enqueue()
                        },
                    )
                }
            }
        }
    }
}
