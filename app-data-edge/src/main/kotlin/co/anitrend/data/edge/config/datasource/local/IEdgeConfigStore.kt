package co.anitrend.data.edge.config.datasource.local

interface IEdgeConfigStore {
    fun edgeConfigDao(): EdgeConfigLocalSource
}
