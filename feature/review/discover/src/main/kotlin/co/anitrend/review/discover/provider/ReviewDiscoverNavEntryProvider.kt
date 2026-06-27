/*
 * Copyright (C) 2025 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package co.anitrend.review.discover.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import co.anitrend.android.core.ui.theme.AniTrendTheme3
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.common.navigation.FeatureNavRegistry
import co.anitrend.navigation.ReviewRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.navigation.nav3.ReviewsNavKey
import co.anitrend.review.discover.component.compose.ReviewDiscoverRoute
import co.anitrend.review.discover.component.content.viewmodel.ReviewDiscoverViewModel
import org.koin.compose.viewmodel.koinViewModel

internal class ReviewDiscoverNavEntryProvider : FeatureNavEntryProvider {
    override fun register(registry: FeatureNavRegistry) {
        registry.register(ReviewsNavKey::class) { _ ->
            ReviewDiscoverNavContent(onBackPress = ::pop)
        }
    }
}

@Composable
private fun ReviewDiscoverNavContent(onBackPress: () -> Unit) {
    val viewModel = koinViewModel<ReviewDiscoverViewModel>()
    val context = LocalContext.current

    AniTrendTheme3 {
        ReviewDiscoverRoute(
            onBackPress = onBackPress,
            onReviewClick = { reviewId, scoreFormat ->
                ReviewRouter.startActivity(
                    context = context,
                    navPayload =
                        ReviewRouter
                            .ReviewParam(
                                id = reviewId,
                                scoreFormat = scoreFormat,
                            ).asNavPayload(),
                )
            },
            viewModel = viewModel,
        )
    }
}
