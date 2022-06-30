package dev.imam.currencyconverter.presentation.contract

import androidx.fragment.app.Fragment
import dev.imam.currencyconverter.presentation.model.Statistic

fun Fragment.statisticManager(): StatisticManager {
    return requireActivity() as StatisticManager
}

interface StatisticManager {

    fun getStatistic(): Statistic

    fun increaseConvertCount()
}