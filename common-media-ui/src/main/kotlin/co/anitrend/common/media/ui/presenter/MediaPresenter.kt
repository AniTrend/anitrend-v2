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

package co.anitrend.common.media.ui.presenter

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.work.Operation
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.common.media.ui.widget.progress.controller.MediaProgressController
import co.anitrend.core.android.koinOf
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.navigation.MediaListTaskRouter
import co.anitrend.navigation.extensions.createOneTimeUniqueWorker

internal class MediaPresenter(
    context: Context,
    settings: Settings,
    entity: IMedia
) : CorePresenter(context, settings) {

    val controller by lazy(UNSAFE) {
        MediaProgressController(entity, settings)
    }

    /**
     * @return Styled text representing progress and remaining
     */
    fun getCurrentProgressText(): Spannable {
        val maximum = controller.getMaximumProgress()
        val builder = SpannableStringBuilder()
        builder.append("${controller.getCurrentProgress()}")
        builder.append(" / ")
        if (maximum == 0)
            builder.append("?")
        else
            builder.append("${controller.getMaximumProgress()}")
        return builder
    }

    /**
     * Enqueues work to be performed
     */
    operator fun invoke(): Operation {
        val params = controller.createParams(
            dateHelper = koinOf()
        )

        return MediaListTaskRouter.forMediaListSaveEntryWorker()
            .createOneTimeUniqueWorker(context, params)
            .enqueue()
    }
}