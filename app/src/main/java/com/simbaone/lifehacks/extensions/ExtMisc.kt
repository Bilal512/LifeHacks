package com.simbaone.lifehacks.extensions

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.simbaone.lifehacks.R
import com.simbaone.lifehacks.viewpagerhelper.HorizontalMarginItemDecoration

fun ViewPager2.setupCarousel() {

    offscreenPageLimit = 1

    val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
    val currentItemHorizontalMarginPx =
        resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
    val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = -pageTranslationX * position
        page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
        page.alpha = 0.25f + (1 - kotlin.math.abs(position))
    }
    setPageTransformer(pageTransformer)
    val itemDecoration = HorizontalMarginItemDecoration(
        this.context,
        R.dimen.viewpager_current_item_horizontal_margin
    )
    addItemDecoration(itemDecoration)
}