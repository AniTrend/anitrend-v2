package co.anitrend.core.extensions

import co.anitrend.arch.extension.ext.UNSAFE
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Helper to create a [Lazy] [MutableStateFlow] for a given generator function [factory]
 *
 * @param factory Produces a result of [T] that should be used as an initial value
 */
fun <T> mutableStateFlow(factory: () -> T): Lazy<MutableStateFlow<T>> = lazy(UNSAFE) {
    MutableStateFlow(factory())
}
