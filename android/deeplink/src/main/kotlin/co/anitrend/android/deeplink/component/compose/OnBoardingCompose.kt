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
package co.anitrend.android.deeplink.component.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.pageindicator.PageIndicator
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.android.deeplink.component.model.OnboardingPage
import co.anitrend.android.deeplink.component.presenter.OnBoardingPresenter
import kotlinx.coroutines.launch

@Composable
private fun PagingControls(
    progress: Float,
    pagerState: PagerState,
    onBoardingCompleted: () -> Unit,
    modifier: Modifier = Modifier,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    // Animate alpha values for a smooth transition.
    val pageIndicatorAlpha by animateFloatAsState(targetValue = 1f - progress)
    val textButtonAlpha by animateFloatAsState(targetValue = progress)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
    ) {
        IconButton(
            onClick = onPreviousClick,
            enabled = pagerState.settledPage > 0,
            colors =
                iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.NavigateBefore,
                    contentDescription = "Previous page",
                    modifier = Modifier.size(24.dp),
                )
            },
        )
        Row(horizontalArrangement = Arrangement.Center) {
            Box {
                TextButton(
                    onClick = {
                        if (progress == 1f) {
                            onBoardingCompleted()
                        }
                    },
                    enabled = progress == 1f,
                    modifier = Modifier.alpha(textButtonAlpha),
                    content = {
                        Text(
                            text = stringResource(co.anitrend.android.deeplink.R.string.onboarding_button_get_started),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                )
                PageIndicator(
                    pagerState = pagerState,
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .alpha(pageIndicatorAlpha),
                    pageIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    currentPageIndicatorColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        IconButton(
            onClick = onNextClick,
            enabled = progress < 1f,
            colors =
                iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.NavigateNext,
                    contentDescription = "Next page",
                    modifier = Modifier.size(24.dp),
                )
            },
        )
    }
}

@Composable
private fun OnBoardingItem(
    modifier: Modifier = Modifier,
    page: OnboardingPage,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(page.resource),
            contentDescription = null,
            modifier =
                Modifier
                    .size(300.dp)
                    .clip(MaterialTheme.shapes.extraLarge),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.padding(vertical = 20.dp))
        Text(
            text = stringResource(page.title),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(page.description),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun OnBoardingContent(
    modifier: Modifier = Modifier,
    onBoardingPages: List<OnboardingPage>,
    onBoardingCompleted: () -> Unit,
    onBoardingColorLists: List<List<Color>>,
) {
    // Track current brush state
    var brushState by remember { mutableStateOf(Brush.linearGradient(onBoardingColorLists.first())) }
    val pagerState = rememberPagerState(pageCount = onBoardingPages::size)
    val scrollScope = rememberCoroutineScope()

    // Determine the last page index.
    val lastPageIndex = pagerState.pageCount - 1

    // Calculate progress based on the current page and its offset:
    // When on the penultimate page, use the offset fraction; when on the last page, progress is 1.
    val progress =
        when (pagerState.currentPage) {
            lastPageIndex -> 1f
            lastPageIndex - 1 -> pagerState.currentPageOffsetFraction.coerceIn(0f, 1f)
            else -> 0f
        }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage + pagerState.currentPageOffsetFraction }
            .collect { visualPage ->
                val basePage = visualPage.toInt().coerceIn(0, onBoardingColorLists.lastIndex)
                val nextPage = (basePage + 1).coerceIn(0, onBoardingColorLists.lastIndex)
                val fraction = (visualPage - basePage).coerceIn(0f, 1f)

                val startColors = onBoardingColorLists[basePage]
                val endColors = onBoardingColorLists[nextPage]

                val interpolatedColors =
                    startColors.zip(endColors) { start, end ->
                        androidx.compose.ui.graphics
                            .lerp(start, end, fraction)
                    }

                brushState =
                    Brush.linearGradient(
                        colors = interpolatedColors,
                        start = Offset.Zero,
                        end = Offset.Infinite,
                        tileMode = TileMode.Clamp,
                    )
            }
    }

    Box(modifier = modifier.background(brush = brushState)) {
        HorizontalPager(
            state = pagerState,
            modifier =
                Modifier
                    .fillMaxSize(),
        ) { page ->
            OnBoardingItem(page = onBoardingPages[page])
        }
        PagingControls(
            progress = progress,
            pagerState = pagerState,
            onBoardingCompleted = onBoardingCompleted,
            modifier = Modifier.align(Alignment.BottomCenter),
            onNextClick = {
                scrollScope.launch {
                    pagerState.animateScrollToPage(pagerState.settledPage + 1)
                }
            },
            onPreviousClick = {
                scrollScope.launch {
                    pagerState.animateScrollToPage(pagerState.settledPage - 1)
                }
            },
        )
    }
}

@Composable
fun OnBoardingScreenContent(
    onBoardingPresenter: OnBoardingPresenter,
    onBoardingCompleted: () -> Unit,
) {
    val surface = MaterialTheme.colorScheme.surface
    val onBoardingPages = onBoardingPresenter.onBoardingPages
    val onBoardingColorLists =
        remember(onBoardingPages) {
            onBoardingPages.map { page -> page.background.plus(surface) }
        }

    OnBoardingContent(
        modifier = Modifier.fillMaxSize(),
        onBoardingPages = onBoardingPages,
        onBoardingColorLists = onBoardingColorLists,
        onBoardingCompleted = {
            onBoardingPresenter.updateInstallationStatus()
            onBoardingCompleted()
        },
    )
}

@Composable
@AniTrendPreview.Default
private fun OnBoardingScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    val onBoardingPages =
        listOf(
            OnboardingPage(
                resource = co.anitrend.android.deeplink.R.drawable.welcome,
                background = listOf(Color(0xFFEADDFF), Color(0xFF6750A4)),
                title = co.anitrend.android.deeplink.R.string.onboarding_title_welcome,
                description = co.anitrend.android.deeplink.R.string.onboarding_desc_welcome,
            ),
            OnboardingPage(
                resource = co.anitrend.android.deeplink.R.drawable.trends,
                background = listOf(Color(0xFFE0F2F1), Color(0xFF009688)),
                title = co.anitrend.android.deeplink.R.string.onboarding_title_trends,
                description = co.anitrend.android.deeplink.R.string.onboarding_desc_trends,
            ),
        )
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        OnBoardingContent(
            onBoardingPages = onBoardingPages,
            onBoardingColorLists =
                onBoardingPages.map { page ->
                    page.background.plus(MaterialTheme.colorScheme.surface)
                },
            onBoardingCompleted = {},
        )
    }
}
