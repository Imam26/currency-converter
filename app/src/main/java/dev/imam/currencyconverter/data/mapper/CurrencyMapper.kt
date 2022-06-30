package dev.imam.currencyconverter.data.mapper

import dev.imam.currencyconverter.data.model.CurrencyResponseModel
import dev.imam.currencyconverter.domain.model.Currency

class CurrencyMapper {
    fun map(source: CurrencyResponseModel): Currency{
        return Currency(
            source.id,
            source.fullName,
            source.amount,
            source.imageRes,
            source.exchangeRate,
            source.isMain
        )
    }

    fun map(source: Currency): CurrencyResponseModel{
        return CurrencyResponseModel(
            source.id,
            source.fullName,
            source.amount,
            source.imageRes,
            source.exchangeRate,
            source.isMain
        )
    }

    fun map(source: List<CurrencyResponseModel>): List<Currency>{
        val target = mutableListOf<Currency>()

        source.forEach {
            target.add(0, map(it))
        }

        return target
    }
}