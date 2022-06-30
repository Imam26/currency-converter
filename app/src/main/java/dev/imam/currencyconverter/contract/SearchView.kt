package dev.imam.currencyconverter.contract

import dev.imam.currencyconverter.model.Currency

interface SearchView {
    fun reloadData(data: MutableList<Currency>)
}