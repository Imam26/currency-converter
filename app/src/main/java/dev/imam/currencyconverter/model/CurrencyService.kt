package dev.imam.currencyconverter.model

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.imam.currencyconverter.R

typealias CurrencyServiceListener = (list: MutableList<Currency>) -> Unit

const val CURRENCY_LIST = "currency_list"
const val SEARCH_HISTORY_LIST = "search_history_list"

class CurrencyService(
    private val sharedPreferences: SharedPreferences?
) {
    private var currencyList = mutableListOf<Currency>()
    private var searchHistoryList = mutableListOf<String>()
    private val listeners = mutableSetOf<CurrencyServiceListener>()

    init {
        val currencyListJson = sharedPreferences?.getString(CURRENCY_LIST, null)
        sharedPreferences?.getStringSet(SEARCH_HISTORY_LIST, null)?.let {
            searchHistoryList = it.toMutableList()
        }

        if (currencyListJson == null) {
            currencyList = mutableListOf(
                Currency(1, "Тенге, Казахстан", 0, R.drawable.image1, 1, true),
            )
            with(sharedPreferences?.edit()) {
                this?.putString(CURRENCY_LIST, Gson().toJson(currencyList))
                this?.apply()
            }
        } else {
            currencyList = Gson().fromJson(
                currencyListJson,
                object : TypeToken<MutableList<Currency>>() {}.type
            )
        }
    }

    fun getCurrencyList(): MutableList<Currency> {
        return ArrayList(currencyList.map { it.copy() })
    }

    fun getSearchHistoryList(): MutableList<String> {
        return searchHistoryList
    }

    fun getFlagList(): List<Flag> {
        return listOf(
            Flag(0, "Лира, Турция", R.drawable.image2),
            Flag(0, "Доллары, США", R.drawable.image3)
        )
    }

    fun addSearchHistoryItem(item: String): Int {
        if (searchHistoryList.any { it == item }) return 0
        searchHistoryList.add(0, item)
        with(sharedPreferences?.edit()) {
            this?.remove(SEARCH_HISTORY_LIST)
            this?.putStringSet(SEARCH_HISTORY_LIST, searchHistoryList.toSet())
            this?.apply()
        }
        return 1
    }

    fun addCurrency(currency: Currency): Int {
        val maxId = currencyList.maxOfOrNull { it.id } ?: 0
        currency.id = maxId + 1;
        currencyList.add(0, currency)
        updateCurrencyListInStorage()
        notifyChanges()
        return currency.id
    }

    fun deleteCurrency(currency: Currency) {
        val index = currencyList.indexOfFirst { it.id == currency.id }
        currencyList.removeAt(index)
        updateCurrencyListInStorage()
        notifyChanges()
    }

    fun addListener(listener: CurrencyServiceListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: CurrencyServiceListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(ArrayList(currencyList.map { item -> item.copy() })) }
    }

    private fun updateCurrencyListInStorage() {
        with(sharedPreferences?.edit()) {
            this?.remove(CURRENCY_LIST)
            this?.putString(CURRENCY_LIST, Gson().toJson(currencyList))
            this?.apply()
        }
    }
}