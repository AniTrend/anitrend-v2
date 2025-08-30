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
package co.anitrend.medialist.editor.provider

import androidx.fragment.app.DialogFragment
import co.anitrend.data.settings.developer.IDeveloperSettings
import co.anitrend.medialist.editor.component.content.MediaListEditorContent
import co.anitrend.medialist.editor.component.content.MediaListEditorSheet
import co.anitrend.navigation.MediaListEditorRouter

class FeatureProvider(
    private val settings: IDeveloperSettings
) : MediaListEditorRouter.Provider {
    override fun sheet(): Class<out DialogFragment> = when (settings.experimentalComposeUi.value) {
        true -> MediaListEditorSheet::class.java
        else -> MediaListEditorContent::class.java
    }
}
