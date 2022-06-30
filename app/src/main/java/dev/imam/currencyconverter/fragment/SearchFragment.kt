package dev.imam.currencyconverter.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.contract.HasCustomTitle
import dev.imam.currencyconverter.contract.SearchView
import dev.imam.currencyconverter.model.Currency
import dev.imam.currencyconverter.model.CurrencyService
import dev.imam.currencyconverter.presenter.SearchPresenter
import dev.imam.currencyconverter.ui.RecyclerViewItemDecorator
import dev.imam.currencyconverter.ui.SearchedCurrencyAdapter

class SearchFragment : Fragment(), SearchView, HasCustomTitle {
    private lateinit var searchedCurrencyAdapter: SearchedCurrencyAdapter
    private lateinit var presenter: SearchPresenter
    private lateinit var searchTextView: TextInputEditText
    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currencyService = CurrencyService(activity?.getSharedPreferences("test", Context.MODE_PRIVATE))
        presenter = SearchPresenter(currencyService, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        setup(view)
        prepareChipGroup(view)

        searchTextView = view.findViewById(R.id.currencySearchTextField)

        searchTextView.setOnKeyListener { _, keyCode, keyEvent ->
            if(keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                val searchText = searchTextView.text.toString()
                presenter.filterCurrencyList(searchText)
                if(presenter.addSearchHistoryItem(searchText)) addNewChip(searchText)
                activity?.let{ hideKeyboard(it)}
            } else if (keyEvent.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                searchTextView.text?.clear()
                searchTextView.clearFocus()
                presenter.filterCurrencyList()
            }
            true
        }

        searchTextView.addTextChangedListener {
            chipGroup.clearCheck()
        }

        return view
    }

    override fun reloadData(data: MutableList<Currency>) {
        if(searchedCurrencyAdapter.itemCount == 0) searchedCurrencyAdapter.setItems(data)
        else searchedCurrencyAdapter.setItemsWithDiffCallback(data)
    }

    override fun getTitleRes(): Int = R.string.search

    override fun getSelectedPageRes(): Int = R.id.page_4

    private fun setup(view: View) {
        searchedCurrencyAdapter = SearchedCurrencyAdapter()
        presenter.filterCurrencyList()

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.searchedCurrencyRecyclerView).apply {
            this.adapter = searchedCurrencyAdapter
            this.layoutManager = layoutManager
            itemAnimator = DefaultItemAnimator()
        }

        val itemDecoration = RecyclerViewItemDecorator(10, 44)
        recyclerView.addItemDecoration(itemDecoration)
    }

    private fun prepareChipGroup(view: View) {
        chipGroup = view.findViewById(R.id.chipGroup)
        val arr = presenter.getSearchHistoryList()
        arr.forEach {
            addNewChip(it)
        }
    }

    private fun addNewChip(text: String) {
        if(text.isEmpty()) return
        (layoutInflater.inflate(R.layout.chip_search, chipGroup, false) as? Chip)?.let { chip ->
            chip.id = View.generateViewId()
            chip.text = text
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    searchTextView.text?.clear()
                    searchTextView.text?.append(text)
                    searchTextView.requestFocus()
                    presenter.filterCurrencyList(text)
                }
            }
            chipGroup.addView(chip, 0)
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}