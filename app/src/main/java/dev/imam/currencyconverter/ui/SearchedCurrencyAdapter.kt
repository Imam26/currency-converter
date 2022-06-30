package dev.imam.currencyconverter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.imam.currencyconverter.helper.CurrencyDiffCallback
import dev.imam.currencyconverter.model.Currency

class SearchedCurrencyAdapter: RecyclerView.Adapter<SearchedCurrencyViewHolder>() {

    private val data = mutableListOf<Currency>()
    private val diffCallback = CurrencyDiffCallback()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedCurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchedCurrencyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SearchedCurrencyViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setItems(list: List<Currency>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun setItemsWithDiffCallback(list: List<Currency>) {
        diffCallback.setItems(data, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback, false)
        data.clear()
        data.addAll(0, list)
        diffResult.dispatchUpdatesTo(this)
    }
}