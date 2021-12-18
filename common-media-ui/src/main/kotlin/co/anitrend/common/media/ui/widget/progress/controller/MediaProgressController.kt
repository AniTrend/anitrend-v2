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

import co.anitrend.core.android.helpers.date.AniTrendDateHelper
import co.anitrend.core.android.settings.Settings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.base.IMediaList
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.navigation.MediaListTaskRouter

/**
 * Media progress widget controller
 */
internal class MediaProgressController(
    private val entity: IMedia,
    private val settings: Settings
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
     * Creates a save entry parameter object
     *
     * @param dateHelper Date helper/utility
     * @param increment Progress increment steps
     */
    fun createParams(
        dateHelper: AniTrendDateHelper,
        increment: Int = 1,
    ): MediaListTaskRouter.Param.SaveEntry {
        val progressIncremented = getCurrentProgress().plus(increment)

        var startFuzzyDate = requireMediaList().startedOn
        var finishFuzzyDate = requireMediaList().finishedOn

        val statusUpdated = when {
            progressIncremented == getMaximumProgress() -> {
                finishFuzzyDate = dateHelper.fuzzyDateNow()
                MediaListStatus.COMPLETED
            }
            requireMediaList().status == MediaListStatus.PLANNING -> {
                startFuzzyDate = dateHelper.fuzzyDateNow()
                MediaListStatus.CURRENT
            }
            else -> requireMediaList().status
        }

        val progressVolumes = when (val mediaProgress = requireMediaList().progress) {
            is MediaListProgress.Manga -> mediaProgress.volumeProgress
            else -> 0
        }

        return MediaListTaskRouter.Param.SaveEntry(
            id = requireMediaList().id,
            mediaId = requireMediaList().mediaId,
            status = statusUpdated,
            scoreFormat = settings.scoreFormat.value,
            score = requireMediaList().score,
            progress = progressIncremented,
            progressVolumes = progressVolumes,
            repeat = requireMediaList().progress.repeated,
            priority = requireMediaList().priority,
            private = requireMediaList().privacy.isPrivate,
            notes = requireMediaList().privacy.notes as? String,
            hiddenFromStatusLists = requireMediaList().privacy.isHidden,
            customLists = requireMediaList().customLists.map { it.name as String },
            advancedScores = requireMediaList().advancedScores.map { it.score },
            startedAt = startFuzzyDate,
            completedAt = finishFuzzyDate,
        )
    }
}