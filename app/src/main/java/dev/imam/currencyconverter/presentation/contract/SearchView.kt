package dev.imam.currencyconverter.presentation.contract

import dev.imam.currencyconverter.presentation.model.CurrencyModel

interface SearchView {
    fun reloadData(data: MutableList<CurrencyModel>)
}