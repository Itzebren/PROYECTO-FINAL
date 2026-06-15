package com.android.mobile.games.app.games.fruitmerge.assets

import androidx.annotation.DrawableRes
import com.android.mobile.games.app.R
import com.android.mobile.games.app.games.fruitmerge.model.FruitMergeFruitType

@DrawableRes
fun FruitMergeFruitType.imageRes(): Int {
    return when (this) {
        FruitMergeFruitType.APPLE -> R.drawable.fruit_merge_apple
        FruitMergeFruitType.ORANGE -> R.drawable.fruit_merge_orange
        FruitMergeFruitType.BANANA -> R.drawable.fruit_merge_banana
        FruitMergeFruitType.COCONUT -> R.drawable.fruit_merge_coconut
        FruitMergeFruitType.PINEAPPLE -> R.drawable.fruit_merge_pineapple
        FruitMergeFruitType.WATERMELON -> R.drawable.fruit_merge_watermelon
    }
}
