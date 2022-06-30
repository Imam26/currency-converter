package dev.imam.currencyconverter.presenter

import dev.imam.currencyconverter.MenuType
import dev.imam.currencyconverter.contract.CurrencyView
import dev.imam.currencyconverter.model.Currency
import dev.imam.currencyconverter.model.CurrencyService
import dev.imam.currencyconverter.model.CurrencyServiceListener

enum class SortType {
    DEFAULT, BY_ALPHA, BY_AMOUNT
}

class CurrencyPresenter(
    var currencyService: CurrencyService,
    private val currencyView: CurrencyView
) {
    var currencyList: MutableList<Currency> = arrayListOf()
    var sortType = SortType.DEFAULT
    private var mainCurrency: Currency? = null
    private var deletedCurrency: Currency? = null
    private var deletedCurrencyPosition = 0
    private val currencyServiceListener: CurrencyServiceListener = { list ->
        currencyList = list
        calculate()
        sort()
        currencyView.reloadData(currencyList)
    }

    init {
        currencyService.addListener(currencyServiceListener)
        currencyList = currencyService.getCurrencyList()
        setMainCurrency()
        calculate()
    }

    fun addCurrency(currency: Currency) {
        currencyService.addCurrency(currency)
        currencyView.closeAddCurrencyBottomSheet()
    }

    fun sortCurrencyList() {
        sort()
        currencyView.reloadData(currencyList)
    }

    fun resetPreparedToDeleteCurrency() {
        deletedCurrency = null
        deletedCurrencyPosition = 0
    }

    fun prepareCurrencyToDelete(currency: Currency, position: Int) {
        if (currency.isMain) return
        setPreparedToDeleteCurrency(currency, position)
        currencyView.changeToolBarState(
            MenuType.DELETE,
            currencyView.getToolBarTitle(MenuType.DELETE, currency.fullName)
        )
    }

    fun deleteSwipedCurrency(currency: Currency, position: Int) {
        if (deletedCurrency != null) {
            deleteCurrencyFromDB()
        }
        setPreparedToDeleteCurrency(currency, position)
        deleteCurrencyFromUI()
    }

    fun deleteCurrencyFromDB() {
        deletedCurrency?.let {
            currencyService.deleteCurrency(it)
        }
        deletedCurrency = null
        deletedCurrencyPosition = 0
    }

    fun deleteCurrencyFromUI() {
        deletedCurrency?.let { currency ->
            val index = currencyList.indexOfFirst { it.id == currency.id }
            currencyList.removeAt(index)
            currencyView.reloadData(currencyList)
            currencyView.changeToolBarState(
                MenuType.DEFAULT,
                currencyView.getToolBarTitle(MenuType.DEFAULT)
            )
            currencyView.showShackBar()
        }
    }

    fun undoDeleteCurrency() {
        deletedCurrency?.let {
            currencyList.add(deletedCurrencyPosition, it)
        }
        deletedCurrency = null
        deletedCurrencyPosition = 0
        currencyView.reloadData(currencyList)
    }

    fun updateMainCurrencyAmount(amount: Int) {
        mainCurrency?.amount = amount
        calculate()
        currencyView.reloadData(currencyList)
    }

    private fun setMainCurrency() {
        mainCurrency = currencyList.find { it -> it.isMain }
    }

    private fun calculate() {
        currencyList.forEach {
            if (it.exchangeRate > 0) it.amount = getConvertedAmount(it.exchangeRate)
        }
    }

    private fun getConvertedAmount(exchangeRate: Int): Int {
        if (mainCurrency != null) return mainCurrency!!.amount / exchangeRate
        return 0
    }

    private fun sort() {
        when (sortType) {
            SortType.BY_AMOUNT -> currencyList.sortBy { (it as? Currency)?.amount }
            SortType.BY_ALPHA -> currencyList.sortBy { (it as? Currency)?.fullName }
            else -> currencyList.sortWith(compareBy<Currency> { it.id }.thenBy { it.isMain })
        }
    }

    private fun setPreparedToDeleteCurrency(currency: Currency, position: Int) {
        deletedCurrency = currency
        deletedCurrencyPosition = position
    }
}