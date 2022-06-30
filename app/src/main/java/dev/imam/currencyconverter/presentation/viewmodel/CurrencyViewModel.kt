package dev.imam.currencyconverter.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.imam.currencyconverter.domain.usecase.AddCurrencyUseCase
import dev.imam.currencyconverter.domain.usecase.DeleteCurrencyUseCase
import dev.imam.currencyconverter.domain.usecase.GetCurrencyListUseCase
import dev.imam.currencyconverter.presentation.model.CurrencyModel
import dev.imam.currencyconverter.presentation.mapper.CurrencyModelMapper

enum class SortType {
    DEFAULT, BY_ALPHA, BY_AMOUNT
}

class CurrencyViewModel(
    private val getCurrencyListUseCase: GetCurrencyListUseCase,
    private val addCurrencyUseCase: AddCurrencyUseCase,
    private val deleteCurrencyUseCase: DeleteCurrencyUseCase,
    private val currencyModelMapper:CurrencyModelMapper
): ViewModel() {
    var sortType = SortType.DEFAULT
    private var mainCurrencyModel: CurrencyModel? = null
    private var deletedCurrencyModel: CurrencyModel? = null
    private var deletedCurrencyPosition = 0

    val currencyModelListLiveData = MutableLiveData<MutableList<CurrencyModel>>()

    val addCurrencyResultLiveData = MutableLiveData<String>()

    val preparedToDeleteCurrencyName = MutableLiveData<String>()

    val deleteCurrencyResultLiveData = MutableLiveData<String>()

    fun loadCurrencyModelList(){
        val currencyList = getCurrencyListUseCase.execute()
        val currencyModelList = currencyModelMapper.map(currencyList).toMutableList()
        calculate(currencyModelList)
        sort(currencyModelList)
        currencyModelListLiveData.value = currencyModelList
        setMainCurrency()
    }

    fun addCurrency(currencyModel: CurrencyModel) {
        val currency = currencyModelMapper.map(currencyModel)
        val id = addCurrencyUseCase.execute(currency)

        if(id > 0){
            currencyModel.id = id
            val currencyModelList = currencyModelListLiveData.value!!
            currencyModelList.add(0, currencyModel)
            calculate(currencyModelList)
            sort(currencyModelList)
            currencyModelListLiveData.value = currencyModelList
            addCurrencyResultLiveData.value = "Successfully added!"
        }
    }

    fun sortCurrencyList() {
        val listToSort = currencyModelListLiveData.value ?: return
        calculate(listToSort)
        sort(listToSort)
        currencyModelListLiveData.value = listToSort
    }

    fun resetPreparedToDeleteCurrency() {
        deletedCurrencyModel = null
        deletedCurrencyPosition = 0
    }

    fun prepareCurrencyToDelete(currencyModel: CurrencyModel, position: Int) {
        if (currencyModel.isMain) return
        setPreparedToDeleteCurrency(currencyModel, position)
        preparedToDeleteCurrencyName.value = currencyModel.fullName
    }

    fun deleteSwipedCurrency(currencyModel: CurrencyModel, position: Int) {
        if (deletedCurrencyModel != null) {
            deleteCurrencyFromDB()
        }
        setPreparedToDeleteCurrency(currencyModel, position)
        deleteCurrencyFromUI()
    }

    fun deleteCurrencyFromDB() {
        deletedCurrencyModel?.let {
            deleteCurrencyUseCase.execute(currencyModelMapper.map(it))
        }
        deletedCurrencyModel = null
        deletedCurrencyPosition = 0
    }

    fun deleteCurrencyFromUI() {
        deletedCurrencyModel?.let { currency ->
            val currencyModelList = currencyModelListLiveData.value!!
            val index = currencyModelList.indexOfFirst { it.id == currency.id }
            currencyModelList.removeAt(index)
            calculate(currencyModelList)
            sort(currencyModelList)
            currencyModelListLiveData.value = currencyModelList
            deleteCurrencyResultLiveData.value = "Currency is deleted!"
        }
    }

    fun undoDeleteCurrency() {
        deletedCurrencyModel?.let {
            val currencyModelList = currencyModelListLiveData.value!!
            currencyModelList.add(deletedCurrencyPosition, it)
            calculate(currencyModelList)
            sort(currencyModelList)
            currencyModelListLiveData.value = currencyModelList
        }
        deletedCurrencyModel = null
        deletedCurrencyPosition = 0
    }

    fun updateMainCurrencyAmount(amount: Int) {
        mainCurrencyModel?.amount = amount
        val currencyModelList = currencyModelListLiveData.value!!
        calculate(currencyModelList)
        sort(currencyModelList)
        currencyModelListLiveData.value = currencyModelList
    }

    private fun setMainCurrency() {
        mainCurrencyModel = currencyModelListLiveData.value!!.find { it -> it.isMain }
    }

    private fun calculate(data: MutableList<CurrencyModel>) {
        data.forEach {
            if (it.exchangeRate > 0) it.amount = getConvertedAmount(it.exchangeRate)
        }
    }

    private fun getConvertedAmount(exchangeRate: Int): Int {
        if (mainCurrencyModel != null) return mainCurrencyModel!!.amount / exchangeRate
        return 0
    }

    private fun sort(data: MutableList<CurrencyModel>) {
        when (sortType) {
            SortType.BY_AMOUNT -> data.sortBy { (it as? CurrencyModel)?.amount }
            SortType.BY_ALPHA -> data.sortBy { (it as? CurrencyModel)?.fullName }
            else -> data.sortWith(compareBy<CurrencyModel> { it.id }.thenBy { it.isMain })
        }
    }

    private fun setPreparedToDeleteCurrency(currencyModel: CurrencyModel, position: Int) {
        deletedCurrencyModel = currencyModel
        deletedCurrencyPosition = position
    }
}