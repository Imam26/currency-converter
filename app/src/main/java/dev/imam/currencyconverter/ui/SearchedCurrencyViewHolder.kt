package dev.imam.currencyconverter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.model.Currency

class SearchedCurrencyViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_searched_currency, parent, false)) {

    private val imageViewFlag = itemView.findViewById<ImageView>(R.id.imageViewSearchedFlag)
    private val textViewFlagLabel = itemView.findViewById<TextView>(R.id.textViewSearchedLabel)

    fun onBind(item: Currency) {
        imageViewFlag.setImageResource(item.imageRes)
        textViewFlagLabel.text = item.fullName
    }
}