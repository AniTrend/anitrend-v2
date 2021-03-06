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

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.*
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.core.R
import co.anitrend.core.component.scope.KoinScope
import co.anitrend.core.ui.model.FragmentItem
import co.anitrend.data.arch.AniTrendExperimentalFeature
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.KoinScopeComponent

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
 * Provides scope for a given [source]
 */
@AniTrendExperimentalFeature
@Suppress("FunctionName")
fun ScopeComponent(source: Any?) = KoinScope(source)

/**
 * Checks for existing fragment in [FragmentManager], if one exists that is used otherwise
 * a new instance is created.
 *
 * @return tag of the fragment
 *
 * @see androidx.fragment.app.commit
 */
inline fun FragmentItem.commit(
    @IdRes contentFrame: Int,
    fragmentActivity: FragmentActivity,
    action: FragmentTransaction.() -> Unit = {
        setCustomAnimations(
            R.anim.enter_from_bottom,
            R.anim.exit_to_bottom,
            R.anim.popup_enter,
            R.anim.popup_exit
        )
    }
) : String? {
    if (fragment == null) return null
    val fragmentManager = fragmentActivity.supportFragmentManager

    val fragmentTag = tag()
    val backStack = fragmentManager.findFragmentByTag(fragmentTag)

    fragmentManager.commit {
        action()
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
inline fun FragmentItem.commit(
    contentFrame: View,
    fragmentActivity: FragmentActivity,
    action: FragmentTransaction.() -> Unit = {
        setCustomAnimations(
            R.anim.enter_from_bottom,
            R.anim.exit_to_bottom,
            R.anim.popup_enter,
            R.anim.popup_exit
        )
    }
) = commit(contentFrame.id, fragmentActivity, action)

inline fun <reified T : Fragment> FragmentActivity.fragment() = lazy {
    supportFragmentManager.fragmentFactory.instantiate(classLoader, T::class.java.name) as T
}

inline fun <reified T : Fragment> FragmentActivity.fragmentByTagOrNew(
    tag: String, noinline factory: () -> T
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        supportFragmentManager.findFragmentByTag(tag) as? T ?: factory()
    }
}