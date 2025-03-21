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
package co.anitrend.core.android

import android.view.ViewGroup
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.ui.view.widget.contract.ISupportStateLayout
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.navigation.model.common.IParam
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools
import org.ocpsoft.prettytime.PrettyTime
import org.threeten.bp.Instant
import java.util.Date

/**
 * Helper to resolve koin dependencies
 *
 * @param qualifier Help qualify a component
 * @param parameters Help define a DefinitionParameters
 *
 * @return [T]
 */
inline fun <reified T> koinOf(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val context = KoinPlatformTools.defaultContext()
    val koin = context.get()
    return koin.get(qualifier, parameters)
}

/**
 * Creates a string with locale applied from pretty time
 *
 * @return 2 hours from now, 3 hours ago .e.t.c
 */
fun AiringSchedule.asPrettyTime(): String {
    val prettyTime = koinOf<PrettyTime>()
    return prettyTime.format(
        Date(airingAt * 1000),
    )
}

/**
 * Creates a string with locale applied from pretty time
 *
 * @return 2 hours from now, 3 hours ago .e.t.c
 */
fun Instant.asPrettyTime(): String {
    val prettyTime = koinOf<PrettyTime>()
    return prettyTime.format(Date(toEpochMilli()))
}

/**
 * Displays an error message for missing parameters otherwise runs [block]
 */
inline fun ISupportStateLayout.assureParamNotMissing(
    param: IParam?,
    block: () -> Unit,
) {
    if (param == null) {
        this as ViewGroup
        loadStateFlow.value =
            LoadState.Error(
                RequestError(
                    topic = context.getString(R.string.app_controller_heading_missing_param),
                    description = context.getString(R.string.app_controller_message_missing_param),
                ),
            )
    } else {
        block()
    }
}
