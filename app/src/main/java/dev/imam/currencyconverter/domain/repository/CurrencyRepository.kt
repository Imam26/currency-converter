package dev.imam.currencyconverter.domain.repository

import dev.imam.currencyconverter.domain.model.Currency

interface CurrencyRepository {
    fun getCurrencies(): List<Currency>
    fun addCurrency(model: Currency): Int
    fun deleteCurrency(model: Currency)
}