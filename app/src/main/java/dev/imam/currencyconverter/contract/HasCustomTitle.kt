package dev.imam.currencyconverter.contract

import androidx.annotation.IdRes
import androidx.annotation.StringRes

interface HasCustomTitle {
    @StringRes
    fun getTitleRes(): Int

    @IdRes
    fun getSelectedPageRes(): Int
}