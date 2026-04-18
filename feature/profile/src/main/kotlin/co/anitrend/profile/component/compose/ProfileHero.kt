package co.anitrend.profile.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.helpers.image.toCoverImage
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.profile.R
import coil.transform.CircleCropTransformation

private val HeroBannerHeight = 232.dp
private val HeroAvatarSize = 104.dp

@Composable
internal fun ProfileHeroSection(
    user: User,
    details: ProfileDetails?,
    isViewer: Boolean,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
) {
    val accentColor = profileAccentColor(details?.profileOption)
    val bannerDescription = stringResource(R.string.description_profile_banner, user.name.toString())
    val avatarDescription = stringResource(R.string.description_profile_avatar, user.name.toString())
    val heroMetaItems = remember(user) { user.heroMetaItems() }
    val relationshipLabels = profileRelationshipLabels(user = user)

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(HeroBannerHeight),
            ) {
                AniTrendImage(
                    image = user.avatar.banner.toCoverImage(),
                    imageType = RequestImage.Media.ImageType.BANNER,
                    onClick = onImageClick,
                    contentDescription = bannerDescription,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors =
                                        listOf(
                                            MaterialTheme.colorScheme.scrim.copy(alpha = 0.06f),
                                            MaterialTheme.colorScheme.scrim.copy(alpha = 0.18f),
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                                        ),
                                ),
                            ),
                )
                Column(
                    modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = 18.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    if (!isViewer && relationshipLabels.isNotEmpty()) {
                        FlowRow(
                            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            relationshipLabels.forEach { label ->
                                ProfilePill(
                                    label = label,
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.24f),
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = SectionHorizontalPadding, vertical = 18.dp)
                        .padding(start = HeroAvatarSize + 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = user.name.toString(),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    user.status.donationBadge
                        ?.takeIf { it.isNotBlank() }
                        ?.let { badge ->
                            ProfilePill(
                                label = badge.toString(),
                                containerColor = accentColor.copy(alpha = 0.18f),
                                contentColor = accentColor,
                            )
                        }
                }

                ProfileHeroMetaLine(heroMetaItems = heroMetaItems)
            }
        }

        Surface(
            modifier =
                Modifier
                    .padding(start = SectionHorizontalPadding, top = HeroBannerHeight - (HeroAvatarSize / 2))
                    .size(HeroAvatarSize),
            shape = RoundedCornerShape(30.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(3.dp, MaterialTheme.colorScheme.surface),
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
        ) {
            AniTrendImage(
                image = user.avatar,
                imageType = RequestImage.Media.ImageType.POSTER,
                onClick = onImageClick,
                contentDescription = avatarDescription,
                transformations = listOf(CircleCropTransformation()),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun profileRelationshipLabels(
    user: User,
): List<String> =
    buildList {
        if (user.status.isFollowing == true) {
            add(stringResource(R.string.label_profile_relationship_following))
        }
        if (user.status.isFollower == true) {
            add(stringResource(R.string.label_profile_relationship_follows_you))
        }
        if (user.status.isBlocked == true) {
            add(stringResource(R.string.label_profile_relationship_blocked))
        }
    }

@Composable
private fun ProfileHeroMetaLine(
    heroMetaItems: List<ProfileHeroMetaItem>,
) {
    if (heroMetaItems.isEmpty()) {
        return
    }

    val joinedLabel = stringResource(R.string.label_profile_fact_joined)
    val updatedLabel = stringResource(R.string.label_profile_fact_updated)

    Text(
        text =
            heroMetaItems.joinToString(separator = " • ") { item ->
                when (item.kind) {
                    ProfileHeroMetaKind.Joined -> "$joinedLabel ${item.value}"
                    ProfileHeroMetaKind.Updated -> "$updatedLabel ${item.value}"
                }
            },
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
