package dev.imam.currencyconverter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.imam.currencyconverter.model.Currency
import dev.imam.currencyconverter.model.Flag

class FlagAdapter(
    private val clickListener: (flag:Flag) -> Unit,
): RecyclerView.Adapter<FlagViewHolder>() {

    private val data = mutableListOf<Flag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FlagViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: FlagViewHolder, position: Int) {
        holder.onBind(data[position], clickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setItems(list: List<Flag>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}