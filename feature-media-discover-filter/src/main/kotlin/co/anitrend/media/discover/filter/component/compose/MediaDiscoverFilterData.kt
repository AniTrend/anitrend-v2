package co.anitrend.media.discover.filter.component.compose

sealed class MediaDiscoverFilterData {
    abstract val title: Int

    data class SelectSection(
        val isMultiSelect: Boolean,
        override val title: Int,
    ) : MediaDiscoverFilterData()

    data class ToggleSection(
        val isActive: Boolean,
        override val title: Int,
    ) : MediaDiscoverFilterData()

    data class SliderSection(
        val isRangeSlider: Boolean,
        override val title: Int,
    ) : MediaDiscoverFilterData()
}
