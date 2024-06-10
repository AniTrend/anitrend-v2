package co.anitrend.data.android.paging

import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Default
import co.anitrend.arch.paging.legacy.source.SupportPagingDataSource

abstract class AbstractPagingSource<T : Any> : SupportPagingDataSource<T>(), ISupportCoroutine by Default()
