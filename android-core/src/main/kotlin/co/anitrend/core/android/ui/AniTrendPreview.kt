package co.anitrend.core.android.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

annotation class AniTrendPreview {

    @Preview(
        uiMode = UI_MODE_NIGHT_NO,
        name = "Light"
    )
    annotation class Light

    @Preview(
        uiMode = UI_MODE_NIGHT_YES,
        name = "Dark"
    )
    annotation class Dark

    @Preview(
        showSystemUi = true,
        device = "spec:width=411dp,height=891dp",
        name = "Mobile"
    )
    annotation class Mobile

    @Preview(
        showSystemUi = true,
        device = "spec:width=673.5dp,height=841dp,dpi=480",
        name = "Foldable"
    )
    annotation class Foldable

    @Preview(
        showSystemUi = true,
        device = "spec:width=1280dp,height=800dp,dpi=480",
        name = "Tablet"
    )
    annotation class Tablet

    @Preview(
        showSystemUi = true,
        device = "spec:width=1920dp,height=1080dp,dpi=480",
        name = "Desktop"
    )
    annotation class Desktop
}
