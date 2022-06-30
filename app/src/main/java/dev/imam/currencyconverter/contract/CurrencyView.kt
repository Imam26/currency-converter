package dev.imam.currencyconverter.contract

import dev.imam.currencyconverter.MenuType
import dev.imam.currencyconverter.model.Currency

interface CurrencyView {
    fun reloadData(data: MutableList<Currency>)
    fun closeAddCurrencyBottomSheet()
    fun changeToolBarState(menuType: MenuType, title: String)
    fun showShackBar()
    fun getToolBarTitle(menuType: MenuType, title: String = ""): String
}