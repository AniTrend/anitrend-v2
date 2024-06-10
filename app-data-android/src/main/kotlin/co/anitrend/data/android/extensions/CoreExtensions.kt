package co.anitrend.data.android.extensions

typealias Async <T> = (suspend () -> T)

fun <T> deferred(block: Async<T>): Async<T> = block
