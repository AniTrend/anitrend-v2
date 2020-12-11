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

package co.anitrend.navigation.drawer.component.presenter

import android.content.Context
import android.content.res.ColorStateList
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import co.anitrend.arch.extension.ext.getColorFromAttr
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.recycler.common.DefaultClickableItem
import co.anitrend.arch.theme.extensions.isEnvironmentNightMode
import co.anitrend.core.android.helpers.image.model.CoverRequestImage
import co.anitrend.core.android.helpers.image.using
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.core.settings.Settings
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.drawer.R
import co.anitrend.navigation.drawer.action.*
import co.anitrend.navigation.drawer.callback.BottomNavigationDrawerCallback
import co.anitrend.navigation.drawer.component.viewmodel.BottomDrawerViewModel
import co.anitrend.navigation.drawer.databinding.BottomNavigationDrawerBinding
import co.anitrend.navigation.drawer.model.account.Account
import co.anitrend.navigation.drawer.shape.SemiCircleEdgeCutoutTreatment
import co.anitrend.navigation.extensions.start
import coil.transform.CircleCropTransformation
import com.google.android.material.shape.MaterialShapeDrawable

internal class DrawerPresenter(
    context: Context,
    settings: Settings
) : CorePresenter(context, settings) {

    fun createForegroundShape(
        container: LinearLayoutCompat
    ): MaterialShapeDrawable {
        val shapeDrawable = MaterialShapeDrawable(
            container.context, null, R.attr.bottomSheetStyle,0
        )

        with (shapeDrawable) {
            fillColor = ColorStateList.valueOf(
                container.context.getColorFromAttr(R.attr.colorPrimary)
            )
            elevation = context.resources.getDimension(R.dimen.xl_margin)
            shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_NEVER
            initializeElevationOverlay(context)
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setTopEdge(
                    SemiCircleEdgeCutoutTreatment(
                        context.resources.getDimension(R.dimen.lg_margin),
                        context.resources.getDimension(R.dimen.spacing_sm),
                        0F,
                        context.resources.getDimension(
                            R.dimen.navigation_drawer_profile_image_size_padded
                        )
                    )
                ).build()
        }

        return shapeDrawable
    }

    fun createBackgroundShape(container: FrameLayout): MaterialShapeDrawable {
        val shapeDrawable = MaterialShapeDrawable(
            container.context, null, R.attr.bottomSheetStyle,0
        )

        with (shapeDrawable) {
            fillColor = ColorStateList.valueOf(
                if (container.context.isEnvironmentNightMode())
                    container.context.getCompatColor(R.color.colorSurface)
                else
                    container.context.getCompatColor(R.color.anitrendDrawerSelector)
            )
            elevation = context.resources.getDimension(R.dimen.lg_margin)

            initializeElevationOverlay(context)
        }

        return shapeDrawable
    }

    fun applyWindowInsetsListener(container: LinearLayoutCompat) {
        container.setOnApplyWindowInsetsListener { view, windowInsets ->
            // Record the window's top inset so it can be applied when the bottom sheet is slide up
            // to meet the top edge of the screen.
            view.setTag(
                R.id.tag_system_window_inset_top,
                windowInsets.getInsets(WindowInsets.Type.systemBars())
            )
            windowInsets
        }
    }

    fun applyStateAndSlideActions(
        binding: BottomNavigationDrawerBinding,
        callback: BottomNavigationDrawerCallback,
        foregroundShapeDrawable: MaterialShapeDrawable
    ) {
        // Scrim view transforms
        callback.addOnSlideAction(AlphaSlideAction(binding.scrimView))
        callback.addOnStateChangedAction(VisibilityStateAction(binding.scrimView))
        // Foreground transforms
        callback.addOnSlideAction(
            ForegroundSheetTransformSlideAction(
                binding.sheetForegroundContainer,
                foregroundShapeDrawable,
                binding.profileImageView
            )
        )
        // Recycler transforms
        callback.addOnStateChangedAction(
            ScrollToTopStateAction(binding.navigationRecyclerView)
        )
    }

    fun applyProfilePicture(imageView: AppCompatImageView, model: Account?) {
        val parentContext = imageView.context
        when (model) {
            is Account.Authenticated -> imageView.using(
                CoverRequestImage(model.coverImage),
                listOf(CircleCropTransformation())
            )
            is Account.Anonymous -> imageView.using(
                parentContext.getCompatDrawable(model.imageRes)
            )
            else -> {}
        }
    }

    fun onAuthenticatedItemClicked(
        clickable: DefaultClickableItem<Account>,
        viewModel: BottomDrawerViewModel
    ) {
        when (clickable.view.id) {
            R.id.accountContainer -> {
                if (clickable.data!!.isActiveUser)
                    ProfileRouter.start(clickable.view.context)
                else { /* TODO: Switch account to the selected one */ }
            }
            R.id.accountSignOut -> {
                viewModel.accountState.signOut(
                    clickable.data!!
                )
            }
        }
    }
}