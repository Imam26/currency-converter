package dev.imam.currencyconverter.data.repository.datasource

import dev.imam.currencyconverter.data.model.CurrencyResponseModel

interface DataSource {
    fun currencyList(): List<CurrencyResponseModel>
    fun addCurrency(model: CurrencyResponseModel): Int
    fun deleteCurrency(model:CurrencyResponseModel)
}