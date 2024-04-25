package co.anitrend.core.android.extensions

import android.content.Intent

inline fun intentOf(factory: Intent.() -> Unit) = Intent().apply { factory() }
