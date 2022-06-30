package dev.imam.currencyconverter.domain.usecase

import dev.imam.currencyconverter.domain.model.Currency
import dev.imam.currencyconverter.domain.repository.CurrencyRepository

class GetCurrencyListUseCase(
    private val repository: CurrencyRepository
) {
    fun execute(): List<Currency> = repository.getCurrencies()
}