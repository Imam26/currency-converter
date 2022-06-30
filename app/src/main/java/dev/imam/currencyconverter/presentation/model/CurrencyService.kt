package dev.imam.currencyconverter.presentation.model

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.imam.currencyconverter.R

typealias CurrencyServiceListener = (list: MutableList<CurrencyModel>) -> Unit

const val CURRENCY_LIST = "currency_list"
const val SEARCH_HISTORY_LIST = "search_history_list"

class CurrencyService(
    private val sharedPreferences: SharedPreferences?
) {
    private var currencyModelList = mutableListOf<CurrencyModel>()
    private var searchHistoryList = mutableListOf<String>()
    private val listeners = mutableSetOf<CurrencyServiceListener>()

    init {
        val currencyListJson = sharedPreferences?.getString(CURRENCY_LIST, null)
        sharedPreferences?.getStringSet(SEARCH_HISTORY_LIST, null)?.let {
            searchHistoryList = it.toMutableList()
        }

        if (currencyListJson == null) {
            currencyModelList = mutableListOf(
                CurrencyModel(1, "Тенге, Казахстан", 0, R.drawable.image1, 1, true),
            )
            with(sharedPreferences?.edit()) {
                this?.putString(CURRENCY_LIST, Gson().toJson(currencyModelList))
                this?.apply()
            }
        } else {
            currencyModelList = Gson().fromJson(
                currencyListJson,
                object : TypeToken<MutableList<CurrencyModel>>() {}.type
            )
        }
    }

    fun getCurrencyList(): MutableList<CurrencyModel> {
        return ArrayList(currencyModelList.map { it.copy() })
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

    fun addCurrency(currencyModel: CurrencyModel): Int {
        val maxId = currencyModelList.maxOfOrNull { it.id } ?: 0
        currencyModel.id = maxId + 1;
        currencyModelList.add(0, currencyModel)
        updateCurrencyListInStorage()
        notifyChanges()
        return currencyModel.id
    }

    fun deleteCurrency(currencyModel: CurrencyModel) {
        val index = currencyModelList.indexOfFirst { it.id == currencyModel.id }
        currencyModelList.removeAt(index)
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
        listeners.forEach { it.invoke(ArrayList(currencyModelList.map { item -> item.copy() })) }
    }

    private fun updateCurrencyListInStorage() {
        with(sharedPreferences?.edit()) {
            this?.remove(CURRENCY_LIST)
            this?.putString(CURRENCY_LIST, Gson().toJson(currencyModelList))
            this?.apply()
        }
    }
}