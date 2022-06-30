package dev.imam.currencyconverter.presentation.contract

import dev.imam.currencyconverter.presentation.model.CurrencyModel
import dev.imam.currencyconverter.presentation.view.activity.MenuType

interface CurrencyView {
    fun reloadData(data: MutableList<CurrencyModel>)
    fun closeAddCurrencyBottomSheet()
    fun changeToolBarState(menuType: MenuType, title: String)
    fun showShackBar()
    fun getToolBarTitle(menuType: MenuType, title: String = ""): String
}