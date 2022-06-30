package dev.imam.currencyconverter.presentation.viewmodel

import dev.imam.currencyconverter.presentation.contract.SearchView
import dev.imam.currencyconverter.presentation.model.CurrencyModel
import dev.imam.currencyconverter.presentation.model.CurrencyService

class SearchPresenter(
    var currencyService: CurrencyService,
    private val searchView: SearchView
) {
    var currencyModelList: MutableList<CurrencyModel> = arrayListOf()
    private var filteredCurrencyModelList: MutableList<CurrencyModel> = arrayListOf()
    private var searchHistoryList: MutableList<String> = currencyService.getSearchHistoryList()
    var sortType = SortType.DEFAULT

    init {
        currencyModelList = currencyService.getCurrencyList()
    }

    fun filterCurrencyList(value: String = "") {
        filteredCurrencyModelList = if (value.isNotEmpty()) {
            sort()
            currencyModelList.filter { it -> it.fullName.contains(value, ignoreCase = true) }
                    as MutableList<CurrencyModel>
        } else {
            sort()
            currencyModelList
        }
        searchView.reloadData(filteredCurrencyModelList)
    }

    fun getSearchHistoryList(): MutableList<String> {
        return searchHistoryList
    }

    fun addSearchHistoryItem(item: String): Boolean {
        return currencyService.addSearchHistoryItem(item) == 1
    }

    private fun sort() {
        when (sortType) {
            SortType.BY_AMOUNT -> currencyModelList.sortBy { (it as? CurrencyModel)?.amount }
            SortType.BY_ALPHA -> currencyModelList.sortBy { (it as? CurrencyModel)?.fullName }
            else -> currencyModelList.sortWith(compareBy<CurrencyModel> { it.id }.thenBy { it.isMain })
        }
    }
}