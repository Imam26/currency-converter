package dev.imam.currencyconverter.domain.usecase

import dev.imam.currencyconverter.domain.model.Currency
import dev.imam.currencyconverter.domain.repository.CurrencyRepository

class AddCurrencyUseCase (
    private val repository: CurrencyRepository
) {
    fun execute(model: Currency): Int = repository.addCurrency(model)
}