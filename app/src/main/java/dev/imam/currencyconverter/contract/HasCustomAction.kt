package dev.imam.currencyconverter.contract

import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes

interface HasCustomAction {
    @MenuRes
    fun getLayoutRes(): Int

    fun getCustomActionMap(): Map<ActionType, CustomAction<Any>>
}

class CustomAction<T> (
    val onCustomAction: (param: T?) -> Unit
)

enum class ActionType {
    SORT_BY_ALPHA,
    SORT_BY_AMOUNT,
    SORT_DEFAULT,
    DELETE
}