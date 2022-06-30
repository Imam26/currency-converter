package dev.imam.currencyconverter.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.imam.currencyconverter.R

class AddViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup
): RecyclerView.ViewHolder(inflater.inflate(R.layout.item_add, parent, false)) {

    private val addView = itemView.findViewById<View>(R.id.buttonAdd)

    fun onBind(clickListener: () -> Unit){
        addView.setOnClickListener {
            clickListener.invoke()
        }
    }
}