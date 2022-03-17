/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.medialist.component.container.mediator

import co.anitrend.domain.user.entity.attribute.MediaListInfo
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

internal class MediaListTabConfiguration(
    private val mediaListInfo: List<MediaListInfo>
) : TabLayoutMediator.TabConfigurationStrategy {

    /**
     * Called to configure the tab for the page at the specified position.
     * Typically calls [TabLayout.Tab.setText], but any form of styling can be applied.
     *
     * @param tab The Tab which should be configured to represent the title of the item at the given
     * position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        val mediaInfo = mediaListInfo[position]
        tab.orCreateBadge.number = mediaInfo.count
        tab.text = mediaInfo.name
    }
}