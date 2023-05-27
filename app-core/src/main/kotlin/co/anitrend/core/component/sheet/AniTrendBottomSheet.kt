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

package co.anitrend.core.component.sheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import co.anitrend.arch.extension.network.contract.ISupportConnectivity
import co.anitrend.arch.extension.network.model.ConnectivityState
import co.anitrend.arch.ui.common.ILifecycleController
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.core.R
import co.anitrend.core.android.binding.IBindingView
import co.anitrend.core.android.components.sheet.SheetBehaviourCallback
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.koinOf
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.core.extensions.stackTrace
import co.anitrend.core.ui.inject
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.core.component.KoinScopeComponent
import timber.log.Timber


abstract class AniTrendBottomSheet<B : ViewBinding>(
    @MenuRes protected open val inflateMenu: Int = SupportFragment.NO_MENU_ITEM,
    @LayoutRes protected open val inflateLayout: Int = SupportFragment.NO_LAYOUT_ITEM
) : BottomSheetDialogFragment(), AndroidScopeComponent, KoinScopeComponent, IBindingView<B>,
    CoroutineScope by MainScope(), ILifecycleController {

    override var binding: B? = null

    override val scope by fragmentScope()

    protected lateinit var behavior: BottomSheetBehavior<*>

    protected open val bottomSheetCallback = SheetBehaviourCallback()

    protected open val defaultPeekHeight: Int = 275.dp
    protected open val defaultState: Int = BottomSheetBehavior.STATE_HALF_EXPANDED

    protected val closeSheetOnBackPressed =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                dismiss()
            }
        }

    private val connectivity by inject<ISupportConnectivity>()

    private fun applyMargins(viewParent: ViewParent) {
        runCatching {
            val parent = viewParent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val width = resources.getDimensionPixelSize(R.dimen.bottom_sheet_margin)
            params.setMargins(width, 0, width, 0)
            parent.layoutParams = params
        }.stackTrace()
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    protected abstract fun setUpViewModelObserver()

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * **N.B.** Calling super of this will register a connectivity change listener, so only
     * call `super.initializeComponents` if you require this behavior
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                connectivity.connectivityStateFlow
                    .onEach { state ->
                        Timber.v("Connectivity state changed: $state")
                        if (state == ConnectivityState.Connected) viewModelState()?.retry()
                    }
                    .catch { cause ->
                        Timber.w(cause, "While collecting connectivity state")
                    }
                    .collect()
            }
        }
    }

    // If I set a theme then the dialog will create a window background
    //override fun getTheme() = R.style.BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        initializeComponents(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            behavior = BottomSheetBehavior.from(bottomSheet).apply {
                halfExpandedRatio = 0.45f
                addBottomSheetCallback(bottomSheetCallback)
                peekHeight = defaultPeekHeight
                skipCollapsed = false
                setState(defaultState)
            }
        }
        return dialog
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null. This will be called between
     * [onCreate] and [onViewCreated].
     *
     * A default View can be returned by calling [androidx.fragment.app.Fragment] in your
     * constructor. Otherwise, this method returns null.
     *
     * It is recommended to **only** inflate the layout in this method and move
     * logic that operates on the returned View to [onViewCreated].
     *
     * If you return a View from here, you will later be called in
     * [onDestroyView] when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (inflateLayout != SupportFragment.NO_LAYOUT_ITEM) {
            inflater.inflate(inflateLayout, container, false)
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.
     *
     * The fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyMargins(view.parent)
        setUpViewModelObserver()
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState(): AniTrendViewModelState<*>? = null

    /**
     * Called when the fragment is no longer in use. This is called
     * after [onStop] and before [onDetach].
     */
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
