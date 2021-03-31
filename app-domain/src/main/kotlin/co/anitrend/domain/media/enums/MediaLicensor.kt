/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.domain.media.enums

import co.anitrend.domain.common.enums.contract.IAliasable

/**
 * Media streaming providers or licensors
 */
enum class MediaLicensor(
    val title: CharSequence,
    override val alias: CharSequence
) : IAliasable {
    CRUNCHYROLL("Crunchyroll","crunchyroll"),
    HIDIVE("Hidive","hidive"),
    FUNIMATION("Funimation","funimation"),
    NETFLIX("Netflix","netflix"),
    AMAZON("Amazon","amazon"),
    HULU("Hulu","hulu"),
    HBO_MAX("HBO Max","hbomax"),
    ANIMELAB("AnimeLab","animelab"),
    VIZ("Viz","viz"),
    ADULT_SWIM("Adult Swim","adultswim"),
    RETROCRUSH("RetroCrush","retrocrush"),
    MIDNIGHT_PULP("Midnight Pulp","midnightpulp.com"),
    TUBI_TV("Tubi TV","tubitv.com"),
    CONTV("CONtv","contv.com"),
    VRV("VRV","vrv")
}