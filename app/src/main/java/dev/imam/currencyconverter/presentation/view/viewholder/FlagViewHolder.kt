package dev.imam.currencyconverter.presentation.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.presentation.model.Flag

class FlagViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_flag, parent, false)) {

    private val imageViewFlag = itemView.findViewById<ImageView>(R.id.imageViewFlag)
    private val textViewFlagLabel = itemView.findViewById<TextView>(R.id.textViewFlagLabel)
    private val cardViewItemFlag = itemView.findViewById<CardView>(R.id.cardViewItemFlag)

    fun onBind(item: Flag, clickListener: (flag: Flag) -> Unit) {
        imageViewFlag.setImageResource(item.imageRes)
        textViewFlagLabel.text = item.fullName
        cardViewItemFlag.setOnClickListener {
            clickListener.invoke(item)
        }
    }
}