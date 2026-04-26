package com.android.mobile.games.app.games.fruitninja.assets

import androidx.annotation.DrawableRes
import com.android.mobile.games.app.R
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaItemType

data class FruitNinjaItemAssets(
    @DrawableRes val whole: Int,
    @DrawableRes val halfOne: Int?,
    @DrawableRes val halfTwo: Int?,
    @DrawableRes val splash: Int?
)

@DrawableRes
fun fruitNinjaBackgroundAsset(): Int {
    return R.drawable.fruit_ninja_background
}

@DrawableRes
fun fruitNinjaExplosionAsset(): Int {
    return R.drawable.fruit_ninja_explosion
}

fun FruitNinjaItemType.assets(): FruitNinjaItemAssets {
    return when (this) {
        FruitNinjaItemType.APPLE -> FruitNinjaItemAssets(
            whole = R.drawable.fruit_ninja_apple,
            halfOne = R.drawable.fruit_ninja_apple_half_1,
            halfTwo = R.drawable.fruit_ninja_apple_half_2,
            splash = R.drawable.fruit_ninja_splash_red
        )

        FruitNinjaItemType.BANANA -> FruitNinjaItemAssets(
            whole = R.drawable.fruit_ninja_banana,
            halfOne = R.drawable.fruit_ninja_banana_half_1,
            halfTwo = R.drawable.fruit_ninja_banana_half_2,
            splash = R.drawable.fruit_ninja_splash_yellow
        )

        FruitNinjaItemType.WATERMELON -> FruitNinjaItemAssets(
            whole = R.drawable.fruit_ninja_watermelon,
            halfOne = R.drawable.fruit_ninja_watermelon_half_1,
            halfTwo = R.drawable.fruit_ninja_watermelon_half_2,
            splash = R.drawable.fruit_ninja_splash_red
        )

        FruitNinjaItemType.ORANGE -> FruitNinjaItemAssets(
            whole = R.drawable.fruit_ninja_orange,
            halfOne = R.drawable.fruit_ninja_orange_half_1,
            halfTwo = R.drawable.fruit_ninja_orange_half_2,
            splash = R.drawable.fruit_ninja_splash_orange
        )

        FruitNinjaItemType.PINEAPPLE -> FruitNinjaItemAssets(
            whole = R.drawable.fruit_ninja_pineapple,
            halfOne = R.drawable.fruit_ninja_pineapple_half_1,
            halfTwo = R.drawable.fruit_ninja_pineapple_half_2,
            splash = R.drawable.fruit_ninja_splash_yellow
        )

        FruitNinjaItemType.COCONUT -> FruitNinjaItemAssets(
            whole = R.drawable.fruit_ninja_coconut,
            halfOne = R.drawable.fruit_ninja_coconut_half_1,
            halfTwo = R.drawable.fruit_ninja_coconut_half_2,
            splash = R.drawable.fruit_ninja_splash_transparent
        )

        FruitNinjaItemType.BOMB -> FruitNinjaItemAssets(
            whole = R.drawable.fruit_ninja_bomb,
            halfOne = null,
            halfTwo = null,
            splash = null
        )
    }
}