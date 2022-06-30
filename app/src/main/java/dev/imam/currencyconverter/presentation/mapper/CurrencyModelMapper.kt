package dev.imam.currencyconverter.presentation.mapper


import dev.imam.currencyconverter.domain.model.Currency
import dev.imam.currencyconverter.presentation.model.CurrencyModel

class CurrencyModelMapper {
    fun map(source: CurrencyModel): Currency {
        return Currency(
            source.id,
            source.fullName,
            source.amount,
            source.imageRes,
            source.exchangeRate,
            source.isMain
        )
    }

    fun map(source: Currency): CurrencyModel {
        return CurrencyModel(
            source.id,
            source.fullName,
            source.amount,
            source.imageRes,
            source.exchangeRate,
            source.isMain
        )
    }

    fun map(source: List<Currency>): List<CurrencyModel>{
        val target = mutableListOf<CurrencyModel>()

        source.forEach {
            target.add(0, map(it))
        }

        return target
    }
}