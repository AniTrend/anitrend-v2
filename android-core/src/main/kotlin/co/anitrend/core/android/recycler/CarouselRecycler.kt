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
package co.anitrend.core.android.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.core.android.recycler.decorator.DefaultSpacingDecorator

class CarouselRecycler
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
    ) : SupportRecyclerView(context, attrs, defStyle), CustomView {
        private val defaultItemAnimator by lazy(UNSAFE) {
            val animator =
                object : DefaultItemAnimator() {
                    override fun getSupportsChangeAnimations() = false
                }
            animator.supportsChangeAnimations = false
            animator
        }

        private val defaultDecorator by lazy(UNSAFE) {
            DefaultSpacingDecorator()
        }

        init {
            onInit(context, attrs, defStyle)
        }

        /**
         * Applies start and end padding to items
         */
        private fun useCarouselDecorator() {
            if (itemDecorationCount == 0) {
                addItemDecoration(defaultDecorator)
            }
        }

        private fun disableItemAnimator() {
            itemAnimator = defaultItemAnimator
        }

        override fun onInit(
            context: Context,
            attrs: AttributeSet?,
            styleAttr: Int?,
        ) {
            layoutManager =
                LinearLayoutManager(
                    context,
                    HORIZONTAL,
                    false,
                ).apply {
                    // allow prefetching to speed up recycler performance
                    isItemPrefetchEnabled = true
                    initialPrefetchItemCount = 5
                    // If the view types are not the same across RecyclerView then it may lead to performance degradation.
                    recycleChildrenOnDetach = true
                }
            useCarouselDecorator()
            disableItemAnimator()
        }
    }
