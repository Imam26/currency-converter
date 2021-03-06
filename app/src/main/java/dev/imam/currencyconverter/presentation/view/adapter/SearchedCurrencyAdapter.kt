package dev.imam.currencyconverter.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.imam.currencyconverter.presentation.helper.CurrencyDiffCallback
import dev.imam.currencyconverter.presentation.model.CurrencyModel
import dev.imam.currencyconverter.presentation.view.viewholder.SearchedCurrencyViewHolder

class SearchedCurrencyAdapter: RecyclerView.Adapter<SearchedCurrencyViewHolder>() {

    private val data = mutableListOf<CurrencyModel>()
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

    fun setItems(list: List<CurrencyModel>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun setItemsWithDiffCallback(list: List<CurrencyModel>) {
        diffCallback.setItems(data, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback, false)
        data.clear()
        data.addAll(0, list)
        diffResult.dispatchUpdatesTo(this)
    }
}