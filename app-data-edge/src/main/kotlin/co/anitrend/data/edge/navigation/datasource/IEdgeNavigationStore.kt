package co.anitrend.data.edge.navigation.datasource

interface IEdgeNavigationStore {
    fun edgeNavigationDao(): EdgeNavigationLocalSource
}
