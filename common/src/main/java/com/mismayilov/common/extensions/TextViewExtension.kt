package com.mismayilov.common.extensions

import android.graphics.BlurMaskFilter
import android.view.View
import android.widget.TextView

fun TextView.toBlurText(showText: Boolean = false) {
    val filter = BlurMaskFilter(80f, BlurMaskFilter.Blur.NORMAL)
    setLayerType(if (showText) View.LAYER_TYPE_SOFTWARE else View.LAYER_TYPE_NONE, null)
    paint.maskFilter = if (showText) filter else null
}