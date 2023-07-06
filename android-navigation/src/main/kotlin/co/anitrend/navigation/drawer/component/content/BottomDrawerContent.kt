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

package co.anitrend.navigation.drawer.component.content

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.core.android.animations.lerp
import co.anitrend.core.android.components.sheet.SheetBehaviourCallback
import co.anitrend.core.android.components.sheet.action.contract.OnSlideAction
import co.anitrend.core.android.components.sheet.action.contract.OnStateChangedAction
import co.anitrend.core.android.extensions.applySystemBarsWindowInsetsListener
import co.anitrend.core.component.content.AniTrendContent
import co.anitrend.core.ui.inject
import co.anitrend.navigation.AboutRouter
import co.anitrend.navigation.UpdaterRouter
import co.anitrend.navigation.drawer.R
import co.anitrend.navigation.drawer.action.OnSandwichSlideAction
import co.anitrend.navigation.drawer.adapter.AccountAdapter
import co.anitrend.navigation.drawer.adapter.NavigationAdapter
import co.anitrend.navigation.drawer.component.content.contract.INavigationDrawer
import co.anitrend.navigation.drawer.component.presenter.DrawerPresenter
import co.anitrend.navigation.drawer.component.viewmodel.AccountViewModel
import co.anitrend.navigation.drawer.component.viewmodel.NavigationViewModel
import co.anitrend.navigation.drawer.databinding.BottomNavigationDrawerBinding
import co.anitrend.navigation.drawer.model.navigation.Navigation
import co.anitrend.navigation.drawer.model.state.SandwichState
import co.anitrend.navigation.extensions.startActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.toExtras
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.math.abs

class BottomDrawerContent(
    private val navigationAdapter: NavigationAdapter,
    private val accountAdapter: AccountAdapter,
    override val inflateLayout: Int = R.layout.bottom_navigation_drawer,
    override val inflateMenu: Int = R.menu.drawer_menu
) : AniTrendContent<BottomNavigationDrawerBinding>(), INavigationDrawer {

    private val presenter by inject<DrawerPresenter>()

    private val accountViewModel by viewModel<AccountViewModel>(
        ownerProducer = { requireActivity() }
    )

    private val navigationViewModel by viewModel<NavigationViewModel>(
        ownerProducer = { requireActivity() },
    )

    private val behavior: BottomSheetBehavior<FrameLayout> by lazy(UNSAFE) {
        BottomSheetBehavior.from(requireBinding().sheetBackgroundContainer)
    }

    private val bottomSheetCallback = SheetBehaviourCallback()

    private val sandwichSlideActions = mutableListOf<OnSandwichSlideAction>()

    private val backgroundShapeDrawable: MaterialShapeDrawable by lazy(UNSAFE) {
        presenter.createBackgroundShape(requireBinding().sheetBackgroundContainer)
    }

    private val foregroundShapeDrawable: MaterialShapeDrawable by lazy(UNSAFE)  {
        presenter.createForegroundShape(requireBinding().sheetForegroundContainer)
    }

    private val mutableNavigationStateFlow =
        MutableStateFlow<Navigation.Menu?>(null)

    override val navigationFlow: Flow<Navigation.Menu> =
        mutableNavigationStateFlow.filterNotNull()

    private var sandwichState: SandwichState = SandwichState.CLOSED
    private var sandwichAnimator: ValueAnimator? = null

    private val sandwichInterpolator by lazy(UNSAFE) {
        AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in)
    }

    private val closeDrawerOnBackPressed =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                dismiss()
            }
        }

    // Progress value which drives the animation of the sandwiching account picker. Responsible
    // for both calling progress updates and state updates.
    private var sandwichProgress: Float = 0F
        set(value) {
            if (field != value) {
                onSandwichProgressChanged(value)
                val newState = when(value) {
                    0F -> SandwichState.CLOSED
                    1F -> SandwichState.OPEN
                    else -> SandwichState.SETTLING
                }
                if (sandwichState != newState) onSandwichStateChanged(newState)
                sandwichState = newState
                field = value
            }
        }

    /**
     * Open or close the account picker "sandwich".
     */
    private fun toggleSandwich() {
        val initialProgress = sandwichProgress
        val newProgress = when (sandwichState) {
            SandwichState.CLOSED -> {
                // Store the original top location of the background container so we can animate
                // the delta between its original top position and the top position needed to just
                // show the account picker RecyclerView, and back again.
                requireBinding().sheetBackgroundContainer.setTag(
                    R.id.tag_view_top_snapshot,
                    requireBinding().sheetBackgroundContainer.top
                )
                1F
            }
            SandwichState.OPEN -> 0F
            SandwichState.SETTLING -> return
        }
        sandwichAnimator?.cancel()
        sandwichAnimator = ValueAnimator.ofFloat(initialProgress, newProgress).apply {
            addUpdateListener { sandwichProgress = animatedValue as Float }
            interpolator = sandwichInterpolator
            duration = (abs(newProgress - initialProgress) *
                    resources.getInteger(R.integer.motion_duration_medium)).toLong()
        }
        sandwichAnimator?.start()
    }

    /**
     * Called each time the value of [sandwichProgress] changes. [progress] is the state of the
     * sandwiching, with 0F being the default [SandwichState.CLOSED] state and 1F being the
     * [SandwichState.OPEN] state.
     */
    private fun onSandwichProgressChanged(progress: Float) {
        requireBinding().run {
            val navProgress = lerp(0F, 1F, 0F, 0.5F, progress)
            val accProgress = lerp(0F, 1F, 0.5F, 1F, progress)

            sheetForegroundContainer.translationY =
                (sheetForegroundContainer.height * 0.15F) * navProgress
            profileImageView.scaleX = 1F - navProgress
            profileImageView.scaleY = 1F - navProgress
            profileImageView.alpha = 1F - navProgress
            sheetForegroundContainer.alpha = 1F - navProgress
            accountRecycler.alpha = accProgress

            foregroundShapeDrawable.interpolation = 1F - navProgress

            // Animate the translationY of the backgroundContainer so just the account picker is
            // peeked above the BottomAppBar.
            sheetBackgroundContainer.translationY = progress *
                    ((scrimView.bottom - accountRecycler.height
                            - resources.getDimension(R.dimen.design_bottom_app_bar_height)) -
                            (sheetBackgroundContainer.getTag(R.id.tag_view_top_snapshot) as Int))
        }

        // Call any actions which have been registered to run on progress changes.
        sandwichSlideActions.forEach { it.onSlide(progress) }
    }

    /**
     * Called when the [SandwichState] of the sandwiching account picker has changed.
     */
    private fun onSandwichStateChanged(state: SandwichState) {
        // Change visibility/clickability of views which obstruct user interaction with
        // the account list.
        when (state) {
            SandwichState.OPEN -> {
                with (requireBinding()) {
                    sheetForegroundContainer.gone()
                    profileImageView.isClickable = false
                    profileImageView.isFocusable = false
                }
            }
            else -> {
                with (requireBinding()) {
                    sheetForegroundContainer.visible()
                    profileImageView.isClickable = true
                    profileImageView.isFocusable = true
                }
            }
        }
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        super.initializeComponents(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this, closeDrawerOnBackPressed
        )
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                accountViewModel.accountState()
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                navigationAdapter.clickableFlow
                    .filterIsInstance<ClickableItem.Data<Navigation.Menu>>()
                    .onEach { clickable ->
                        val model = clickable.data
                        setCheckedItem(model.id)
                        mutableNavigationStateFlow.value = model
                    }.catch { cause: Throwable ->
                        Timber.e(
                            cause,
                            "navigationAdapter.clickableStateFlow threw an uncaught exception",
                        )
                    }.collect()
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                presenter.settings.isAuthenticated.flow
                    .onEach {
                        accountViewModel.accountState()
                        navigationViewModel(it)
                    }.catch { cause: Throwable ->
                        Timber.e(cause)
                    }.collect()
            }
        }
    }

    override fun setUpViewModelObserver() {
        accountViewModel.userAccounts.observe(viewLifecycleOwner) {
            accountAdapter.submitList(it)
        }
        accountViewModel.activeAccount.observe(viewLifecycleOwner) { account ->
            presenter.applyProfilePicture(requireBinding().profileImageView, account)
        }
        navigationViewModel.navigationItems.onEach {
            navigationAdapter.submitList(it)
        }.catch {
            Timber.e(it)
        }.launchIn(lifecycleScope)
        setCheckedItem(R.id.navigation_home)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomNavigationDrawerBinding.bind(view)
        requireBinding().run {
            sheetForegroundContainer.applySystemBarsWindowInsetsListener()

            sheetBackgroundContainer.background = backgroundShapeDrawable
            sheetForegroundContainer.background = foregroundShapeDrawable

            scrimView.setOnClickListener { dismiss() }

            profileImageView.setOnClickListener { toggleSandwich() }

            presenter.applyStateAndSlideActions(
                this, bottomSheetCallback, foregroundShapeDrawable
            )
            // Close the sandwiching account picker if open
            bottomSheetCallback.addOnStateChangedAction(object : OnStateChangedAction {
                override fun onStateChanged(sheet: View, newState: Int) {
                    sandwichAnimator?.cancel()
                    sandwichProgress = 0F
                }
            })

            // If the drawer is open, pressing the system back button should close the drawer.
            bottomSheetCallback.addOnStateChangedAction(object : OnStateChangedAction {
                override fun onStateChanged(sheet: View, newState: Int) {
                    closeDrawerOnBackPressed.isEnabled = newState != BottomSheetBehavior.STATE_HIDDEN
                }
            })

            toggleMenuVisibility(false)
            behavior.addBottomSheetCallback(bottomSheetCallback)
            behavior.state = BottomSheetBehavior.STATE_HIDDEN

            accountRecycler.adapter = accountAdapter
            navigationRecyclerView.adapter = navigationAdapter
        }
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     *
     * @see .onCreateOptionsMenu
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_about -> {
            AboutRouter.startActivity(context)
            true
        }
        R.id.action_updates -> {
            UpdaterRouter.startActivity(context)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun isShowing(): Boolean {
        return behavior.state == BottomSheetBehavior.STATE_HALF_EXPANDED ||
                behavior.state == BottomSheetBehavior.STATE_EXPANDED
    }

    override fun toggleDrawer() {
        if (sandwichState == SandwichState.OPEN)
            toggleSandwich()
        else when (behavior.state) {
            BottomSheetBehavior.STATE_EXPANDED -> show()
            BottomSheetBehavior.STATE_HIDDEN -> show()
            BottomSheetBehavior.STATE_COLLAPSED,
            BottomSheetBehavior.STATE_HALF_EXPANDED -> dismiss()
            BottomSheetBehavior.STATE_DRAGGING -> { }
            BottomSheetBehavior.STATE_SETTLING -> { }
        }
    }

    override fun show() {
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    override fun dismiss() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun addOnSlideAction(action: OnSlideAction) {
        bottomSheetCallback.addOnSlideAction(action)
    }

    override fun removeOnSlideAction(action: OnSlideAction) {
        bottomSheetCallback.removeOnSlideAction(action)
    }

    override fun addOnStateChangedAction(action: OnStateChangedAction) {
        bottomSheetCallback.addOnStateChangedAction(action)
    }

    override fun removeOnStateChangedAction(action: OnStateChangedAction) {
        bottomSheetCallback.removeOnStateChangedAction(action)
    }

    override fun setCheckedItem(@IdRes selectedItem: Int) {
        navigationViewModel.setNavigationMenuItemChecked(selectedItem)
    }

    override fun toggleMenuVisibility(showDrawerMenu: Boolean) {
        setMenuVisibility(showDrawerMenu)
    }
}
