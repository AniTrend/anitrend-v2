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

package co.anitrend.viewer.component.screen

import android.Manifest
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.extension.ext.extra
import co.anitrend.arch.extension.ext.hideStatusBarAndNavigationBar
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.android.helpers.image.model.toCoverImage
import co.anitrend.core.android.helpers.image.using
import co.anitrend.core.component.screen.AniTrendScreen
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.viewer.R
import co.anitrend.viewer.component.viewmodel.ImageViewerViewModel
import co.anitrend.viewer.databinding.ImageViewerScreenBinding
import coil.request.Disposable
import coil.target.Target
import com.davemorrissey.labs.subscaleview.ImageSource
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImageViewerScreen : AniTrendScreen<ImageViewerScreenBinding>() {

    private val param: ImageViewerRouter.Param? by extra(ImageViewerRouter.Param.KEY)

    private val viewModel by viewModel<ImageViewerViewModel>()

    private val stateLayoutConfig = StateLayoutConfig(
        loadingMessage = R.string.label_text_loading
    )

    private val permissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isAllowed: Boolean ->
        if (isAllowed)
            viewModel.downloadImage(param?.imageSrc)
        else
            Toast.makeText(this, R.string.warning_permission_for_storage_not_granted, Toast.LENGTH_LONG).show()
    }

    private var disposable: Disposable? = null

    /**
     * Can be used to configure custom theme styling as desired
     */
    override fun configureActivity() {
        super.configureActivity()
        hideStatusBarAndNavigationBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ImageViewerScreenBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)
    }

    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        requireBinding().stateLayout.stateConfigFlow.value = stateLayoutConfig
        val duration = resources.getInteger(R.integer.motion_duration_large)
        requireBinding().subSamplingImageView.setOnClickListener {
            val transparency = requireBinding().downloadAction.alpha
            requireBinding().downloadAction.animate()
                .alpha(if (transparency == VISIBLE) HIDDEN else VISIBLE)
                .setDuration(duration.toLong())
                .apply { interpolator = DecelerateInterpolator() }
        }
        requireBinding().downloadAction.setOnClickListener {
            val transparency = requireBinding().downloadAction.alpha
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (transparency == VISIBLE)
                permissionResult.launch(permission)
        }
        setUpImagePreview()
    }

    private fun setUpImagePreview() {
        disposable = object : Target {
            /**
             * Called when the request starts.
             */
            override fun onStart(placeholder: Drawable?) {
                binding?.stateLayout?.loadStateFlow?.value = LoadState.Loading()
            }

            /**
             * Called if an error occurs while executing the request.
             */
            override fun onError(error: Drawable?) {
                binding?.stateLayout?.loadStateFlow?.value = LoadState.Error(
                    RequestError("Unable to load image")
                )
            }

            /**
             * Called if the request completes successfully.
             */
            override fun onSuccess(result: Drawable) {
                val source = ImageSource.bitmap(result.toBitmap())
                binding?.subSamplingImageView?.setImage(source)
                binding?.stateLayout?.loadStateFlow?.value = LoadState.Success()
            }
        }.using(param?.imageSrc.toCoverImage(), this)
    }

    override fun onDestroy() {
        binding?.subSamplingImageView?.setOnClickListener(null)
        disposable?.dispose()
        disposable = null
        super.onDestroy()
    }

    companion object {
        const val VISIBLE = 1f
        const val HIDDEN = 0f
    }
}
