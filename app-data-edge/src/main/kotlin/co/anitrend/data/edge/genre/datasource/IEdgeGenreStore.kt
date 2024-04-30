package co.anitrend.data.edge.genre.datasource

interface IEdgeGenreStore {
    fun edgeGenreDao(): EdgeGenreLocalSource
}
