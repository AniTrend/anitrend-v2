/*
 * Copyright (C) 2025 AniTrend
 */
package co.anitrend.data.edge.news.datasource

import co.anitrend.data.edge.news.datasource.local.EdgeNewsLocalSource

interface IEdgeNewsStore {
    fun edgeNewsDao(): EdgeNewsLocalSource
}
