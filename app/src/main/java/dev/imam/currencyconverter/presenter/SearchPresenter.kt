package dev.imam.currencyconverter.presenter

import dev.imam.currencyconverter.contract.SearchView
import dev.imam.currencyconverter.model.Currency
import dev.imam.currencyconverter.model.CurrencyService

class SearchPresenter(
    var currencyService: CurrencyService,
    private val searchView: SearchView
) {
    var currencyList: MutableList<Currency> = arrayListOf()
    private var filteredCurrencyList: MutableList<Currency> = arrayListOf()
    private var searchHistoryList: MutableList<String> = currencyService.getSearchHistoryList()
    var sortType = SortType.DEFAULT

    init {
        currencyList = currencyService.getCurrencyList()
    }

    fun filterCurrencyList(value: String = "") {
        filteredCurrencyList = if (value.isNotEmpty()) {
            sort()
            currencyList.filter { it -> it.fullName.contains(value, ignoreCase = true) }
                    as MutableList<Currency>
        } else {
            sort()
            currencyList
        }
        searchView.reloadData(filteredCurrencyList)
    }

    fun getSearchHistoryList(): MutableList<String> {
        return searchHistoryList
    }

    fun addSearchHistoryItem(item: String): Boolean {
        return currencyService.addSearchHistoryItem(item) == 1
    }

    private fun sort() {
        when (sortType) {
            SortType.BY_AMOUNT -> currencyList.sortBy { (it as? Currency)?.amount }
            SortType.BY_ALPHA -> currencyList.sortBy { (it as? Currency)?.fullName }
            else -> currencyList.sortWith(compareBy<Currency> { it.id }.thenBy { it.isMain })
        }
    }
}