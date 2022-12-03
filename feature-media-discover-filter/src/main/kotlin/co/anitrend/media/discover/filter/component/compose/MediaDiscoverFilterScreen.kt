package co.anitrend.media.discover.filter.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.anitrend.arch.extension.util.attribute.SeasonType
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.media.discover.filter.R
import co.anitrend.navigation.MediaDiscoverRouter

@Composable
fun MediaDiscoverFilterScreen(
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.Param,
) {
    val isAdultChecked by remember { mutableStateOf(param.isAdult ?: false) }
    val yearRangeValue by remember { mutableIntStateOf(param.seasonYear ?: 0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Switch(
            checked = isAdultChecked,
            onCheckedChange = { /*isAdultChecked = it*/ },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Discover Filter On List Section
        Column {
            Text(text = stringResource(id = R.string.label_discover_filter_on_list_title), style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.label_discover_filter_on_list_sub_title))
            Spacer(modifier = Modifier.height(24.dp))
            // Add ChipGroup for on-list options here
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Discover Filter Media Type Section
        Column {
            Text(text = stringResource(id = R.string.label_discover_filter_media_type_title), style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.label_discover_filter_media_type_title))
            Spacer(modifier = Modifier.height(24.dp))
            // Add ChipGroup for media types here
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Discover Filter Season Section
        Column {
            Text(text = stringResource(id = R.string.label_discover_filter_season_title), style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.label_discover_filter_season_sub_title))
            Spacer(modifier = Modifier.height(24.dp))
            // Add ChipGroup for seasons here
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Discover Filter Year Section
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.label_discover_filter_year_title), style = MaterialTheme.typography.headlineSmall)
                Slider(
                    value = yearRangeValue.toFloat(),
                    onValueChange = { /*yearRangeValue = it*/ },
                    valueRange = 1970f..(dateHelper.getCurrentYear() + 2).toFloat(),
                    steps = (dateHelper.getCurrentYear() + 2 - 1970),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Discover Filter Status Section
    Column {
        Text(text = stringResource(id = R.string.label_discover_filter_status_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.label_discover_filter_status_sub_title))
        Spacer(modifier = Modifier.height(24.dp))
        // Add ChipGroup for status options here
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Discover Filter Media Type Section
    Column {
        Text(text = stringResource(id = R.string.label_discover_filter_media_type_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.label_discover_filter_media_type_title))
        Spacer(modifier = Modifier.height(24.dp))
        // Add ChipGroup for media types here
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Discover Filter Season Section
    Column {
        Text(text = stringResource(id = R.string.label_discover_filter_season_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.label_discover_filter_season_sub_title))
        Spacer(modifier = Modifier.height(24.dp))
        // Add ChipGroup for seasons here
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Discover Filter Format Section
    Column {
        Text(text = stringResource(id = R.string.label_discover_filter_format_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.label_discover_filter_format_sub_title))
        Spacer(modifier = Modifier.height(24.dp))
        // Add ChipGroup for format options here
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Discover Filter Source Section
    Column {
        Text(text = stringResource(id = R.string.label_discover_filter_source_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.label_discover_filter_source_sub_title))
        Spacer(modifier = Modifier.height(24.dp))
        // Add ChipGroup for source options here
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Discover Filter Country Section
    Column {
        Text(text = stringResource(id = R.string.label_discover_filter_country_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.label_discover_filter_country_sub_title))
        Spacer(modifier = Modifier.height(24.dp))
        // Add ChipGroup for country options here
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Discover Filter Streaming Section
    Column {
        Text(text = stringResource(id = R.string.label_discover_filter_streaming_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.label_discover_filter_streaming_sub_title))
        Spacer(modifier = Modifier.height(24.dp))
        // Add ChipGroup for streaming options here
    }
}

@AniTrendPreview.Mobile
@Composable
fun MediaDiscoverFilterScreenPreview() {
    AniTrendTheme3 {
        MediaDiscoverFilterScreen(
            dateHelper = object : AbstractSupportDateHelper() {
                override val currentSeason: SeasonType = SeasonType.FALL
                override fun getCurrentYear(delta: Int) = 2023 + delta
            },
            param = MediaDiscoverRouter.Param()
        )
    }
}
