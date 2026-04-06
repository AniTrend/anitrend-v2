/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.news.component.compose

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import co.anitrend.android.core.settings.common.locale.ILocaleSettings
import co.anitrend.android.core.settings.helper.locale.model.AniTrendLocale.Companion.asLocaleString
import co.anitrend.domain.news.entity.News
import co.anitrend.domain.news.model.NewsParam
import co.anitrend.navigation.NewsRouter
import co.anitrend.news.R
import co.anitrend.news.component.content.viewmodel.NewsContentViewModel
import coil.compose.AsyncImage
import org.jsoup.Jsoup

@Composable
fun NewsCompose(
    settings: ILocaleSettings,
    viewModel: NewsContentViewModel,
    onNewsClick: (NewsRouter.NewsParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val locale by settings.locale.flow.collectAsStateWithLifecycle(
        initialValue = settings.locale.value,
    )
    val newsItems =
        remember(locale) {
            viewModel.news(NewsParam(locale.asLocaleString()))
        }.collectAsLazyPagingItems()
    val refreshState = newsItems.loadState.refresh

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        when {
            refreshState is LoadState.Loading ->
                NewsState(
                    title = stringResource(R.string.label_news_loading_title),
                    subtitle = stringResource(R.string.message_news_loading),
                )

            refreshState is LoadState.Error ->
                NewsRetryState(
                    title = stringResource(R.string.label_news_error_title),
                    actionLabel = stringResource(R.string.action_news_retry),
                    onRetry = newsItems::retry,
                )

            newsItems.itemCount > 0 ->
                NewsFeed(
                    newsItems = List(newsItems.itemCount) { index -> newsItems[index] }.filterNotNull(),
                    appendState = newsItems.loadState.append,
                    onNewsClick = onNewsClick,
                    onRetry = newsItems::retry,
                )

            else ->
                NewsState(
                    title = stringResource(R.string.label_news_empty_title),
                    subtitle = stringResource(R.string.message_news_empty),
                )
        }
    }
}

@Composable
private fun NewsFeed(
    newsItems: List<News>,
    appendState: LoadState,
    onNewsClick: (NewsRouter.NewsParam) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(
            items = newsItems,
            key = News::guid,
        ) { news ->
            NewsCard(
                news = news,
                onClick = { onNewsClick(news.asNewsParam()) },
            )
        }

        when (appendState) {
            is LoadState.Loading -> {
                item {
                    Text(
                        text = stringResource(R.string.message_news_loading_more),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }

            is LoadState.Error -> {
                item {
                    NewsRetryState(
                        title = stringResource(R.string.label_news_error_title),
                        actionLabel = stringResource(R.string.action_news_retry),
                        onRetry = onRetry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun NewsCard(
    news: News,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val preview = remember(news.description) { news.description.asPlainText() }
    val publishedOn = remember(news.publishedOn) { news.publishedOn.relativeTimeLabel() }

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .clickable(onClick = onClick),
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (news.subTitle.isNotBlank() || publishedOn != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (news.subTitle.isNotBlank()) {
                        Text(
                            text = news.subTitle.asPlainText(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    publishedOn?.also {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                        )
                    }
                }
            }

            Text(
                text = news.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                news.image?.also { image ->
                    AsyncImage(
                        model = image,
                        contentDescription = news.title,
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .size(width = 116.dp, height = 84.dp)
                                .clip(MaterialTheme.shapes.large),
                    )
                }

                if (preview.isNotBlank()) {
                    Text(
                        text = preview,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = if (news.image != null) 4 else 6,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun NewsState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun NewsRetryState(
    title: String,
    actionLabel: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        OutlinedButton(onClick = onRetry) {
            Text(text = actionLabel)
        }
    }
}

private fun News.asNewsParam() =
    NewsRouter.NewsParam(
        link = link,
        title = title,
        subTitle = subTitle.asPlainText(),
        description = description,
        content = content,
    )

private fun String?.asPlainText(): String = Jsoup.parse(this.orEmpty()).text().trim()

private fun Long?.relativeTimeLabel(): String? {
    if (this == null) {
        return null
    }

    return DateUtils.getRelativeTimeSpanString(
        this,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS,
    ).toString()
}
