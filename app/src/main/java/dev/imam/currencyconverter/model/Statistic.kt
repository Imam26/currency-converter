package dev.imam.currencyconverter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic(
    var convertCount: Int
): Parcelable
