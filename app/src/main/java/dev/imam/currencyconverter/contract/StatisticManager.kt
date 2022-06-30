package dev.imam.currencyconverter.contract

import androidx.fragment.app.Fragment
import dev.imam.currencyconverter.model.Statistic

fun Fragment.statisticManager(): StatisticManager {
    return requireActivity() as StatisticManager
}

interface StatisticManager {

    fun getStatistic(): Statistic

    fun increaseConvertCount()
}