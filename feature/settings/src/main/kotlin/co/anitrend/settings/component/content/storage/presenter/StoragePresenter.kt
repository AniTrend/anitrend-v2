package co.anitrend.settings.component.content.storage.presenter

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Storage
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.storage.contract.IStorageController
import co.anitrend.android.core.storage.enums.StorageType
import co.anitrend.android.core.storage.extensions.toHumanReadableByteValue
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.settings.cache.ICacheSettings
import co.anitrend.settings.R
import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem
import kotlin.math.abs

class StoragePresenter(
    context: Context,
    private val preferenceBuilder: IPreferenceBuilder,
    settings: Settings,
    private val storageController: IStorageController? = null,
): CorePresenter(context, settings) {

    private fun percentLabel(value: Float): String {
        val percent = (value * 100).toInt()
        return context.getString(R.string.label_settings_storage_percent, percent)
    }

    fun getItems(): List<SettingItem> {
        preferenceBuilder.clear()

        val cache = settings as ICacheSettings

        // Intro hint
        preferenceBuilder.add(
            entries = listOf(
                SettingItem.HintCard(
                    id = "storage_hint",
                    title = context.getString(R.string.preference_title_storage),
                    description = context.getString(R.string.preference_summary_storage),
                    icon = Icons.Outlined.Storage,
                    onClick = {},
                )
            )
        )

        // Options for ratio (discrete steps)
        val options = listOf(0.10f, 0.15f, 0.20f, 0.25f, 0.30f, 0.35f, 0.40f, 0.50f)
        val selected = options.minByOrNull { abs(it - cache.cacheUsageRatio.value) } ?: options.first()

        // Storage info (optional if controller provided)
        val extraInfoProvider: (() -> String)? = storageController?.let { controller ->
            {
                val free = controller.getFreeSpace(context, StorageType.CACHE)
                val limit = controller.getStorageUsageLimit(context, StorageType.CACHE, cache)
                val usedPercent = (selected * 100).toInt()
                context.getString(
                    R.string.label_settings_storage_info,
                    percentLabel(selected),
                    limit.toHumanReadableByteValue(),
                    free.toHumanReadableByteValue(),
                ) + " • " + context.getString(R.string.label_settings_storage_used_percent, usedPercent)
            }
        }
        val progressProvider: (() -> Float)? = storageController?.let { _ ->
            { selected }
        }

        preferenceBuilder.add(
            entries = listOf(
                SettingItem.SliderSetting(
                    id = "storage_cache_ratio",
                    value = { cache.cacheUsageRatio.value },
                    onValueChange = { value ->
                        // Snap to nearest step in options
                        val snapped = options.minByOrNull { abs(it - value) } ?: value
                        cache.cacheUsageRatio.value = snapped
                    },
                    valueRange = options.first()..options.last(),
                    steps = options.size - 2,
                    valueLabel = { v -> percentLabel(options.minByOrNull { abs(it - v) } ?: v) },
                    extraInfo = extraInfoProvider,
                    progress = progressProvider,
                ),
            ),
        )

        return preferenceBuilder.build()
    }
}
