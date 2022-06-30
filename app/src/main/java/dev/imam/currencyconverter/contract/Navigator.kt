package dev.imam.currencyconverter.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showAddCurrencyBottomSheet()

    fun closeAddCurrencyBottomSheet()

    fun showAddFlagBottomSheet()

    fun closeAddFlagBottomSheet()

    fun showConfirmDialog()

    fun closeConfirmDialog()

    fun <T : Parcelable> publishResult(result: T)

    fun <T : Parcelable> listenResult(clazz: Class<T>, owner: LifecycleOwner, listener: (T) -> Unit)
}