package co.anitrend.android.navigation.drawer.model.internal

internal object DrawerSelectionResolver {
    fun resolve(
        currentSelection: DrawerDestination,
        entries: List<DrawerEntry>,
    ): DrawerDestination {
        val availableSelections =
            entries
                .filterIsInstance<DrawerEntry.Item>()
                .filter(DrawerEntry.Item::isCheckable)
                .map(DrawerEntry.Item::destination)

        return currentSelection.takeIf(availableSelections::contains)
            ?: availableSelections.firstOrNull()
            ?: DrawerDestination.Home
    }
}
