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

package co.anitrend.core.android.shortcut.contract

import co.anitrend.core.android.shortcut.model.Shortcut
import kotlin.jvm.Throws

interface IShortcutFacade {

    /**
     * Publish the list of dynamic [shortcuts]. If there are already dynamic or
     * pinned shortcuts with the same IDs, each mutable shortcut is updated.
     *
     * @throws IllegalArgumentException if [android.content.pm.ShortcutManager.getMaxShortcutCountPerActivity]
     * is exceeded, or when trying to update immutable shortcuts.
     * @throws IllegalStateException when the user is locked.
     */
    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun createShortcuts(vararg shortcuts: Shortcut): Boolean

    /**
     * Re-enable pinned shortcuts that were previously disabled. If the target [shortcuts]
     * are already enabled, this method does nothing.
     *
     * @throws IllegalArgumentException If trying to enable immutable shortcuts.
     * @throws IllegalStateException when the user is locked.
     */
    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun enableShortcuts(vararg shortcuts: Shortcut)

    /**
     * Disable pinned shortcuts, showing the user a custom error message when they try to select
     * the disabled [shortcuts].
     *
     * @throws IllegalArgumentException If trying to enable immutable shortcuts.
     * @throws IllegalStateException when the user is locked.
     */
    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun disableShortcuts(vararg shortcuts: Shortcut)

    /**
     * Published shortcuts should call this method whenever the user
     * selects the shortcut containing the given ID or when the user completes
     * an action in the app that is equivalent to selecting the shortcut.
     *
     * @throws IllegalStateException when the user is locked.
     */
    @Throws(IllegalStateException::class)
    fun reportShortcutUsage(shortcut: Shortcut)

    /**
     * Delete all dynamic shortcuts from app.
     *
     * @throws IllegalStateException when the user is locked.
     */
    @Throws(IllegalStateException::class)
    fun removeAllDynamicShortcuts()
}