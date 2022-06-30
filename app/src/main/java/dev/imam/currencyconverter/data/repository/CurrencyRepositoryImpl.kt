package dev.imam.currencyconverter.data.repository

import dev.imam.currencyconverter.data.mapper.CurrencyMapper
import dev.imam.currencyconverter.data.repository.datasource.DataSource
import dev.imam.currencyconverter.domain.model.Currency
import dev.imam.currencyconverter.domain.repository.CurrencyRepository

class CurrencyRepositoryImpl(
    private val dataSource: DataSource,
    private val mapper: CurrencyMapper
): CurrencyRepository {

    override fun getCurrencies(): List<Currency> {
        val currencyList = dataSource.currencyList()
        return mapper.map(currencyList)
    }

    override fun addCurrency(model: Currency): Int {
        val responseModel = mapper.map(model)
        return dataSource.addCurrency(responseModel)
    }

    override fun deleteCurrency(model: Currency) {
        val responseModel = mapper.map(model)
        dataSource.deleteCurrency(responseModel)
    }
}