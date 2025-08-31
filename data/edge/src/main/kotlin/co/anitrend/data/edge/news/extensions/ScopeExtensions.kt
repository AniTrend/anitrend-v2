/*
 * Copyright (C) 2025 AniTrend
 */
package co.anitrend.data.edge.news.extensions

import co.anitrend.data.edge.news.datasource.IEdgeNewsStore
import org.koin.core.scope.Scope

internal fun Scope.newsStore() = get<IEdgeNewsStore>()
