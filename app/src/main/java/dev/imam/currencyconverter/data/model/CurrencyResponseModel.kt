package dev.imam.currencyconverter.data.model

data class CurrencyResponseModel(
    var id: Int,
    val fullName: String,
    var amount: Int = 0,
    val imageRes: Int,
    val exchangeRate: Int = 0,
    val isMain: Boolean = false
) {
}