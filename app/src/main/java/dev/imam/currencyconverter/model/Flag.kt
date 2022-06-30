package dev.imam.currencyconverter.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Flag(
    var id: Int,
    val fullName: String,
    @DrawableRes val imageRes: Int
) : Parcelable