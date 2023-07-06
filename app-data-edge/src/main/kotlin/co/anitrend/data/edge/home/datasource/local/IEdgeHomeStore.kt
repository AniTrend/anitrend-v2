package co.anitrend.data.edge.home.datasource.local

interface IEdgeHomeStore {
    fun edgeHomeDao(): EdgeHomeLocalSource
}
