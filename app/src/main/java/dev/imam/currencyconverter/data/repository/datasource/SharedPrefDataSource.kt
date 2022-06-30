package dev.imam.currencyconverter.data.repository.datasource

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.data.model.CurrencyResponseModel
import dev.imam.currencyconverter.presentation.model.CurrencyModel

const val CURRENCY_LIST = "currency_list"
const val SEARCH_HISTORY_LIST = "search_history_list"

class SharedPrefDataSource(
    private val sharedPref: SharedPreferences?
): DataSource {

    init {
        if(!contains(CURRENCY_LIST)){
            add(CURRENCY_LIST, mutableListOf(
                CurrencyModel(1, "Тенге, Казахстан", 0, R.drawable.image1, 1, true),
            ))
        }
    }

    override fun currencyList(): List<CurrencyResponseModel> {
        return getList(CURRENCY_LIST) ?: listOf()
    }

    override fun addCurrency(model: CurrencyResponseModel): Int {
        val list = getList<CurrencyResponseModel>(CURRENCY_LIST)?.toMutableList() ?: mutableListOf()
        val maxId = list.maxOfOrNull { it.id } ?: 0
        model.id = maxId + 1;
        list.add(0, model)
        add(CURRENCY_LIST, list)
        return model.id
    }

    override fun deleteCurrency(model: CurrencyResponseModel) {
        if(!contains(CURRENCY_LIST)) return
        val list = getList<CurrencyResponseModel>(CURRENCY_LIST)!!.toMutableList()
        val index = list.indexOfFirst { it.id == model.id }
        list.removeAt(index)
        add(CURRENCY_LIST, list)
    }

    private inline fun<reified T> getList(key: String): List<T>? {
        val jsonString = sharedPref?.getString(key, null) ?: return null
        return Gson().fromJson(
            jsonString,
            object : TypeToken<List<T>>() {}.type
        )
    }

    private fun<T> add(key: String, value: T){
        with(sharedPref?.edit()) {
            this?.remove(key)
            this?.putString(key, Gson().toJson(value))
            this?.apply()
        }
    }

    private fun contains(key: String): Boolean{
        return sharedPref?.contains(key) ?: false
    }
}