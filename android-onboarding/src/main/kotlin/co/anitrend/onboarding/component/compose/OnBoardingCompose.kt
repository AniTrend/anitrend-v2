package co.anitrend.onboarding.component.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.compose.design.pageindicator.PageIndicator
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.onboarding.component.model.OnboardingPage
import kotlinx.coroutines.launch

@Composable
private fun PagingControls(
    isFirstPage: Boolean,
    pagerState: PagerState,
    onBoardingCompleted: () -> Unit,
    modifier: Modifier = Modifier,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    // Determine the last page index.
    val lastPageIndex = pagerState.pageCount - 1

    // Calculate progress based on the current page and its offset:
    // When on the penultimate page, use the offset fraction; when on the last page, progress is 1.
    val progress = when (pagerState.currentPage) {
        lastPageIndex -> 1f
        lastPageIndex - 1 -> pagerState.currentPageOffsetFraction.coerceIn(0f, 1f)
        else -> 0f
    }

    // Animate alpha values for a smooth transition.
    val pageIndicatorAlpha by animateFloatAsState(targetValue = 1f - progress)
    val textButtonAlpha by animateFloatAsState(targetValue = progress)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        IconButton(
            onClick = onPreviousClick,
            enabled = !isFirstPage,
            colors = iconButtonColors(
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
                            text = stringResource(co.anitrend.onboarding.R.string.onboarding_button_get_started),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                )
                PageIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.align(Alignment.Center).alpha(pageIndicatorAlpha),
                    pageIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    currentPageIndicatorColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        IconButton(
            onClick = onNextClick,
            enabled = progress < 1f,
            colors = iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.NavigateNext,
                    contentDescription = "Next page",
                    modifier = Modifier.size(24.dp),
                )
            }
        )
    }
}

@Composable
private fun OnBoardingContent(
    modifier: Modifier = Modifier,
    onBoardingPages: List<OnboardingPage>,
    onBoardingCompleted: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = onBoardingPages::size)
    val coroutineScope = rememberCoroutineScope()
    val isFirstPage = pagerState.settledPage == 0

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            OnBoardingItem(page = onBoardingPages[page])
        }
        PagingControls(
            isFirstPage = isFirstPage,
            pagerState = pagerState,
            onBoardingCompleted = onBoardingCompleted,
            modifier = Modifier.align(Alignment.BottomCenter),
            onNextClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            onPreviousClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }
        )
    }
}

@Composable
fun OnBoardingScreenContent(
    onBoardingPages: List<OnboardingPage>,
    onBoardingCompleted: () -> Unit,
) {
    OnBoardingContent(
        modifier = Modifier.fillMaxSize(),
        onBoardingPages = onBoardingPages,
        onBoardingCompleted = onBoardingCompleted
    )
}

@Composable
@AniTrendPreview.Default
private fun OnBoardingScreenContent(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        OnBoardingContent(
            onBoardingPages = listOf(
                OnboardingPage(
                    resource = co.anitrend.onboarding.R.drawable.welcome,
                    background = listOf(Color(0xFFEADDFF), Color(0xFF6750A4)),
                    title = co.anitrend.onboarding.R.string.onboarding_title_welcome,
                    description = co.anitrend.onboarding.R.string.onboarding_desc_welcome
                ),
                OnboardingPage(
                    resource = co.anitrend.onboarding.R.drawable.trends,
                    background = listOf(Color(0xFFE0F2F1), Color(0xFF009688)),
                    title = co.anitrend.onboarding.R.string.onboarding_title_trends,
                    description = co.anitrend.onboarding.R.string.onboarding_desc_trends
                ),
            ),
            onBoardingCompleted = {},
        )
    }
}
