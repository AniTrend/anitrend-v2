/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.component.presenter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.transition.TransitionManager
import android.view.View
import co.anitrend.R
import co.anitrend.core.android.components.action.FloatingActionMenu
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.android.settings.Settings
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform

internal class MainPresenter(
    context: Context,
    settings: Settings
) : CorePresenter(context, settings) {

    private fun createContainerTransform(start: View, end: View): MaterialContainerTransform {
        val containerTransform = MaterialContainerTransform()
        with (containerTransform) {
            duration = 250
            pathMotion = MaterialArcMotion()
            startView = start
            endView = end
            scrimColor = Color.TRANSPARENT
        }
        return containerTransform
    }

    fun startActionMenuTransformation(
        floatingActionButton: FloatingActionButton,
        actionMenu: FloatingActionMenu,
        reveal: Boolean
    ) {
        val transform = when {
            reveal -> createContainerTransform(floatingActionButton, actionMenu)
            else -> createContainerTransform(actionMenu, floatingActionButton)
        }
        TransitionManager.beginDelayedTransition(actionMenu, transform)
        if (reveal) {
            floatingActionButton.hide()
            actionMenu.show()
        } else {
            floatingActionButton.show()
            actionMenu.dismiss()
        }
    }

    fun redirectToFAQ() {
        val faq = context.getString(R.string.app_faq_page_link)
        startViewIntent(Uri.parse(faq))
    }

    fun redirectToPatreon() {
        val patreon = context.getString(R.string.app_patreon_page_link)
        startViewIntent(Uri.parse(patreon))
    }

    fun redirectToDiscord() {
        val discord = context.getString(R.string.app_discord_page_link)
        startViewIntent(Uri.parse(discord))
    }
}