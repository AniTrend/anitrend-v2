/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.data.recommendation.koin

import co.anitrend.data.core.extensions.store
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.recommendation.converter.MediaRecommendationConnectionEntityConverter
import co.anitrend.data.recommendation.mapper.MediaRecommendationMapper
import org.koin.dsl.module

private val sourceModule =
    module {
    }

private val converterModule =
    module {
        factory {
            MediaRecommendationConnectionEntityConverter()
        }
    }

private val mapperModule =
    module {
        factory {
            MediaRecommendationMapper(
                localSource = store().mediaRecommendationConnectionDao(),
                converter = get<MediaConverter>(),
            )
        }
    }

private val useCaseModule =
    module {
    }

private val repositoryModule =
    module {
    }

internal val recommendationModules =
    listOf(
        sourceModule,
        converterModule,
        mapperModule,
        useCaseModule,
        repositoryModule,
    )
