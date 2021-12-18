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

package co.anitrend.common.medialist.ui.widget.status.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import co.anitrend.arch.extension.ext.getCompatDrawableTintAttr
import co.anitrend.arch.extension.ext.getStringList
import co.anitrend.common.medialist.ui.R
import co.anitrend.common.medialist.ui.databinding.AdapterStatusSpinerItemBinding
import co.anitrend.domain.medialist.enums.MediaListStatus

internal class StatusIconAdapter(
    context: Context,
    @LayoutRes layoutResource: Int,
    @IdRes textViewResourceId: Int,
    objects: List<MediaListStatus>
) : ArrayAdapter<MediaListStatus>(context, layoutResource, textViewResourceId, objects) {

    private var binding: AdapterStatusSpinerItemBinding? = null
    private val mediaListStatusLocalized = context.getStringList(R.array.media_list_status)

    private fun onViewCreated(view: View, position: Int) {
        binding = AdapterStatusSpinerItemBinding.bind(view)

        binding?.statusSpinnerText?.text = mediaListStatusLocalized[position]

        val statusIconDrawable = when(getItem(position)) {
            MediaListStatus.CURRENT -> R.drawable.ic_current
            MediaListStatus.COMPLETED -> R.drawable.ic_completed
            MediaListStatus.DROPPED -> R.drawable.ic_dropped
            MediaListStatus.PAUSED -> R.drawable.ic_paused
            MediaListStatus.PLANNING -> R.drawable.ic_planning
            else -> R.drawable.ic_repeat
        }

        binding?.statusSpinnerIcon?.setImageDrawable(
            context.getCompatDrawableTintAttr(
                statusIconDrawable,
                R.attr.colorControlNormal
            )
        )
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        onViewCreated(view, position)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        onViewCreated(view, position)
        return view
    }
}