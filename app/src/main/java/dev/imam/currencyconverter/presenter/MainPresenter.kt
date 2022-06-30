package dev.imam.currencyconverter.presenter

import dev.imam.currencyconverter.contract.CurrencyView
import dev.imam.currencyconverter.contract.MainView
import dev.imam.currencyconverter.model.CurrencyService
import dev.imam.currencyconverter.model.Statistic
import dev.imam.currencyconverter.model.StatisticService

class MainPresenter(
    var statisticService: StatisticService,
) {
    private var mainView: MainView? = null
    private var statistic: Statistic = statisticService.getStatistic()

    fun attachView(view: MainView) {
        if(mainView == null) mainView = view
    }

    fun increaseConvertCount() {
        statistic.convertCount++
    }

    fun getStatistic(): Statistic {
        return statistic
    }

    fun updateStatistic(){
        statisticService.updateStatistic(statistic)
    }
}