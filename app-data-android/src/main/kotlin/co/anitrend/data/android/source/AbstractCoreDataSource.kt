package co.anitrend.data.android.source

import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Default

abstract class AbstractCoreDataSource : SupportCoreDataSource(), ISupportCoroutine by Default()
