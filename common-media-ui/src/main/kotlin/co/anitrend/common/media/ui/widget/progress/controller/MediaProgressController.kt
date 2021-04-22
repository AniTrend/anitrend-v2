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

package co.anitrend.common.media.ui.widget.progress.controller

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.annotation.ColorInt
import androidx.core.text.color
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.common.media.ui.R
import co.anitrend.core.android.helpers.color.asColorInt
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.common.HexColor
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.base.IMediaList
import co.anitrend.domain.medialist.entity.contract.MediaListProgress

/**
 * Media progress widget controller
 */
internal class MediaProgressController(
    private val entity: IMedia,
    private val settings: IAuthenticationSettings
) {

    private fun requireMediaList(): IMediaList {
        return requireNotNull(entity.mediaList)
    }

    /**
     * Indicates whether or not the widget should be displayed based
     * on the availability of data [IMediaList] in this [IMedia]
     */
    fun shouldHideWidget(): Boolean {
        return entity.mediaList == null
    }

    /**
     * Check if we've caught up, assuming that the max progress is not 0
     */
    fun hasCaughtUp(): Boolean {
        val current = getCurrentProgress()
        val maximum = getMaximumProgress()
        if (maximum == 0)
            return false
        return current == maximum
    }

    /**
     * Checks if the current user is the owner of this media list and if the releasing status
     * is not [MediaStatus.NOT_YET_RELEASED]
     */
    fun isIncrementPossible(): Boolean {
        val isCurrentUser = requireMediaList().userId == settings.authenticatedUserId.value
        val isValidStatus = entity.status != MediaStatus.NOT_YET_RELEASED
        return isCurrentUser && isValidStatus && !hasCaughtUp()
    }

    /**
     * @return The maximum possible progress
     */
    fun getMaximumProgress(): Int {
        val media = entity as Media
        return when (val category = media.category) {
            is Media.Category.Anime -> {
                if (category.schedule != null && category.episodes == 0)
                    category.schedule!!.episode
                else category.episodes

            }
            is Media.Category.Manga -> category.chapters
        }
    }

    /**
     * @return Current progress for this user
     */
    fun getCurrentProgress(): Int {
        return when (val progress = requireMediaList().progress) {
            is MediaListProgress.Anime -> progress.episodeProgress
            is MediaListProgress.Manga -> progress.chapterProgress
        }
    }

    /**
     * @return Styled text representing progress and remaining
     */
    fun getCurrentProgressText(): Spannable {
        val maximum = getMaximumProgress()
        val builder = SpannableStringBuilder()
        builder.append("${getCurrentProgress()}")
        builder.append(" / ")
        if (maximum == 0)
            builder.append("?")
        else
            builder.append("${getMaximumProgress()}")
        return builder
    }
}