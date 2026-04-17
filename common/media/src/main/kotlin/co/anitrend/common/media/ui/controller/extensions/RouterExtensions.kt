/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.common.media.ui.controller.extensions

import android.view.View
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.extensions.runIfActivityContext
import co.anitrend.core.extensions.runIfAuthenticated
import co.anitrend.core.ui.fragmentByTagOrNew
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asBundle
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.navigation.model.common.IParam

internal fun View.startMediaScreenFor(entity: Media) {
    MediaRouter.startActivity(
        context = context,
        navPayload =
            MediaRouter
                .MediaParam(
                    id = entity.id,
                    type = entity.category.type,
                ).asNavPayload(),
    )
}

fun FragmentActivity.handleMediaItemNavigation(
    param: IParam,
    settings: IUserSettings,
) {
    when (param) {
        is MediaRouter.MediaParam ->
            MediaRouter.startActivity(
                context = this,
                navPayload = param.asNavPayload(),
            )

        is MediaListEditorRouter.MediaListEditorParam ->
            openMediaListSheetFor(
                mediaListParam = param,
                settings = settings,
            )

        else -> Unit
    }
}

fun FragmentActivity.openMediaListSheetFor(
    mediaListParam: MediaListEditorRouter.MediaListEditorParam,
    settings: IUserSettings,
): Boolean {
    window.decorView.runIfAuthenticated(settings) {
        val fragmentItem =
            FragmentItem(
                fragment = MediaListEditorRouter.forSheet(),
                parameter = mediaListParam.asBundle(),
            )
        val dialog = fragmentItem.fragmentByTagOrNew(this)
        dialog.show(supportFragmentManager, fragmentItem.tag())
    }
    return true
}

fun View.openMediaListSheetFor(
    mediaListParam: MediaListEditorRouter.MediaListEditorParam,
    settings: IUserSettings,
): Boolean {
    runIfActivityContext {
        openMediaListSheetFor(
            mediaListParam = mediaListParam,
            settings = settings,
        )
    }
    return true
}

fun View.openMediaListSheetFor(
    entity: Media,
    settings: IUserSettings,
): Boolean =
    openMediaListSheetFor(
        mediaListParam =
            MediaListEditorRouter.MediaListEditorParam(
                mediaId = entity.id,
                mediaType = entity.category.type,
                scoreFormat = settings.scoreFormat.value,
            ),
        settings = settings,
    )
