package co.anitrend.data.edge.media.datasource.local

interface IEdgeMediaStore {
    fun edgeMediaDao(): EdgeMediaLocalSource
}
