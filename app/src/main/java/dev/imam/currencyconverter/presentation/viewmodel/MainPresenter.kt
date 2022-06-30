package dev.imam.currencyconverter.presentation.viewmodel

import dev.imam.currencyconverter.presentation.contract.MainView
import dev.imam.currencyconverter.presentation.model.Statistic
import dev.imam.currencyconverter.presentation.model.StatisticService

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