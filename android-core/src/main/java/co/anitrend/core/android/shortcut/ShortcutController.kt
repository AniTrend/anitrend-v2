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

package co.anitrend.core.android.shortcut

import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import co.anitrend.core.android.shortcut.contract.IShortcutController
import co.anitrend.core.android.shortcut.model.Shortcut
import co.anitrend.navigation.extensions.forActivity
import timber.log.Timber

internal class ShortcutController(
    private val context: Context,
    private val shortcutManager: ShortcutManager?
) : IShortcutController {

    /**
     * Publish the list of dynamic [shortcuts]. If there are already dynamic or
     * pinned shortcuts with the same IDs, each mutable shortcut is updated.
     *
     * @throws IllegalArgumentException if [ShortcutManager.getMaxShortcutCountPerActivity]
     * is exceeded, or when trying to update immutable shortcuts.
     * @throws IllegalStateException when the user is locked.
     */
    override fun createShortcuts(vararg shortcuts: Shortcut): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutInfo = shortcuts.mapNotNull { shortcut ->
                val intent = shortcut.router.forActivity(context)
                if (intent == null) {
                    Timber.w("Intent for shortcut: `${shortcut.id}` returned null")
                    null
                } else {
                    ShortcutInfo.Builder(context, shortcut.id)
                        .setShortLabel(context.getString(shortcut.label))
                        .setDisabledMessage(context.getString(shortcut.disabledMessage))
                        .setIcon(Icon.createWithResource(context, shortcut.icon))
                        .setIntent(intent)
                        .build()
                }
            }
            shortcutManager?.addDynamicShortcuts(shortcutInfo) ?: false
        }
        else
            false
    }

    /**
     * Re-enable pinned shortcuts that were previously disabled. If the target [shortcuts]
     * are already enabled, this method does nothing.
     *
     * @throws IllegalArgumentException If trying to enable immutable shortcuts.
     * @throws IllegalStateException when the user is locked.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1, lambda = 1)
    override fun enableShortcuts(vararg shortcuts: Shortcut) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            shortcutManager?.enableShortcuts(shortcuts.map(Shortcut::id))
    }

    /**
     * Disable pinned shortcuts, showing the user a custom error message when they try to select
     * the disabled [shortcuts].
     *
     * @throws IllegalArgumentException If trying to enable immutable shortcuts.
     * @throws IllegalStateException when the user is locked.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1, lambda = 1)
    override fun disableShortcuts(vararg shortcuts: Shortcut) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            shortcutManager?.disableShortcuts(shortcuts.map(Shortcut::id))
    }

    /**
     * Published shortcuts should call this method whenever the user
     * selects the shortcut containing the given ID or when the user completes
     * an action in the app that is equivalent to selecting the shortcut.
     *
     * @throws IllegalStateException when the user is locked.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1, lambda = 1)
    override fun reportShortcutUsage(shortcut: Shortcut) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            shortcutManager?.reportShortcutUsed(shortcut.id)
    }

    /**
     * Delete all dynamic shortcuts from app.
     *
     * @throws IllegalStateException when the user is locked.
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1, lambda = 1)
    override fun removeAllDynamicShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            shortcutManager?.removeAllDynamicShortcuts()
    }
}