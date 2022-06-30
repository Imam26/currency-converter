package dev.imam.currencyconverter.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    var id: Int,
    val fullName: String,
    var amount: Int = 0,
    @DrawableRes val imageRes: Int,
    val exchangeRate: Int = 0,
    val isMain: Boolean = false
) : IItem, Parcelable
