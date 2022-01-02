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

package co.anitrend.core.ui

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.core.R
import co.anitrend.core.android.extensions.fragmentManager
import co.anitrend.core.extensions.runIfActivityContext
import co.anitrend.core.ui.model.FragmentItem
import org.koin.androidx.fragment.android.KoinFragmentFactory
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ViewModelOwnerDefinition
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.androidx.viewmodel.scope.BundleDefinition
import org.koin.androidx.viewmodel.scope.emptyState
import org.koin.androidx.viewmodel.scope.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.component.KoinScopeComponent
import org.koin.java.KoinJavaComponent.getKoin

/**
 * Get given dependency
 *
 * @param qualifier - bean qualifier / optional
 * @param parameters - injection parameters
 */
inline fun <reified T : Any> KoinScopeComponent.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = runCatching {
    scope.get<T>(qualifier, parameters)
}.getOrElse {
    getKoin().get(qualifier, parameters)
}

/**
 * Inject lazily
 *
 * @param qualifier - bean qualifier / optional
 * @param parameters - injection parameters
 */
inline fun <reified T : Any> KoinScopeComponent.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = lazy(UNSAFE) { get<T>(qualifier, parameters) }

/**
 * Checks for existing fragment in [FragmentManager], if one exists that is used otherwise
 * a new instance is created.
 *
 * @param contentFrame Resource Id of the view to inflate the fragment into
 * @param context Any lifecycle driver context
 * @param fragmentTransaction Optional transaction to run while committing fragment
 *
 * @return [String] tag of the fragment
 *
 * @see androidx.fragment.app.commit
 *
 * @throws NotImplementedError if [context] cannot resolve [FragmentManager]
 */
inline fun FragmentItem<*>.commit(
    @IdRes contentFrame: Int,
    context: Context,
    fragmentTransaction: FragmentTransaction.() -> Unit = {
        setCustomAnimations(
            R.anim.enter_from_bottom,
            R.anim.exit_to_bottom,
            R.anim.popup_enter,
            R.anim.popup_exit
        )
    }
) : String {
    val fragmentTag = tag()
    val fragmentManager = context.fragmentManager()
    val backStack = fragmentManager.findFragmentByTag(fragmentTag)

    fragmentManager.commit {
        fragmentTransaction()
        backStack?.let {
            replace(contentFrame, it, fragmentTag)
        } ?: replace(contentFrame, fragment, parameter, fragmentTag)
    }
    return fragmentTag
}

/**
 * Checks for existing fragment in [FragmentManager], if one exists that is used otherwise
 * a new instance is created.
 *
 * @return tag of the fragment
 *
 * @see androidx.fragment.app.commit
 */
inline fun <T: Fragment> FragmentItem<T>.commit(
    contentFrame: View,
    context: Context,
    action: FragmentTransaction.() -> Unit = {
        setCustomAnimations(
            R.anim.enter_from_bottom,
            R.anim.exit_to_bottom,
            R.anim.popup_enter,
            R.anim.popup_exit
        )
    }
) = commit(contentFrame.id, context, action)

/**
 * Uses fragment factory to instantiate a fragment class definition
 *
 * @param classDefinition [Class] with an out variance of type [Fragment]
 */
inline fun <reified T : Fragment> FragmentActivity.createFragment(
    classDefinition: Class<out T>
): T {
    val qualifier = classDefinition.name
    val factory = supportFragmentManager.fragmentFactory
    require(factory is KoinFragmentFactory) {
        "Fragment factory for $this is $factory instead of KoinFragmentFactory"
    }
    return factory.instantiate(classLoader, qualifier) as T
}

/**
 * Retrieves or creates a new fragment using the given [FragmentItem]
 *
 * @param activity Calling activity
 *
 * @see runIfActivityContext
 */
inline fun <reified T : Fragment> FragmentItem<T>.fragmentByTagOrNew(
    activity: FragmentActivity
): T {
    val fragmentManager = activity.supportFragmentManager
    val fragment = fragmentManager.findFragmentByTag(tag()) as? T
        ?: activity.createFragment(fragment)
    fragment.arguments = parameter
    return fragment
}

/**
 * Retrieves or creates a new fragment using the given [tag]
 *
 * @param classDefinition A [Class] with an out variance of type [Fragment]
 * @param tag Tag to identity the fragment
 * @param lazyMode [LazyThreadSafetyMode] to use
 */
inline fun <reified T : Fragment> FragmentActivity.fragmentByTagOrNew(
    fragmentItem: FragmentItem<T>,
    lazyMode: LazyThreadSafetyMode = UNSAFE
) = lazy(lazyMode) {
    fragmentItem.fragmentByTagOrNew(this)
}

/**
 * Resolve view models from within views
 */
inline fun <reified T : ViewModel> View.viewModel(
    qualifier: Qualifier? = null,
    noinline owner: ViewModelOwnerDefinition = {
        ViewModelOwner.from(
            findViewTreeViewModelStoreOwner()!!,
            findViewTreeSavedStateRegistryOwner()
        )
    },
    noinline state: BundleDefinition = emptyState(),
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = lazy(UNSAFE) {
    if (context is KoinScopeComponent) {
        val scopeComponent = context as KoinScopeComponent
        scopeComponent.scope.getViewModel(
            qualifier,
            owner,
            T::class,
            state,
            parameters
        )
    } else {
        getKoin().getViewModel(
            qualifier,
            owner,
            parameters
        )
    }
}