package co.anitrend.data.edge.navigation.datasource.local

interface IEdgeNavigationStore {
    fun edgeNavigationDao(): EdgeNavigationLocalSource
}
