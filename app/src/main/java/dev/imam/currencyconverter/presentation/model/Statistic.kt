package dev.imam.currencyconverter.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Statistic(
    var convertCount: Int
): Parcelable
