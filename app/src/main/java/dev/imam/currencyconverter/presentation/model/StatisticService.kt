package dev.imam.currencyconverter.presentation.model

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val STATISTIC = "statistic"

class StatisticService (
    private val sharedPreferences: SharedPreferences?
) {
    fun getStatistic(): Statistic {
        val statisticJson = sharedPreferences?.getString(STATISTIC, null)
        if(statisticJson != null){
            return Gson().fromJson(
                statisticJson,
                object : TypeToken<Statistic>() {}.type
            )
        }
        return Statistic(0)
    }

    fun updateStatistic(statistic: Statistic){
        with(sharedPreferences?.edit()) {
            this?.remove(STATISTIC)
            this?.putString(STATISTIC, Gson().toJson(statistic))
            this?.apply()
        }
    }
}