package dev.imam.currencyconverter.helper

import androidx.recyclerview.widget.DiffUtil
import dev.imam.currencyconverter.model.Currency
import dev.imam.currencyconverter.model.IItem

class CurrencyDiffCallback : DiffUtil.Callback() {
    private var oldList = emptyList<IItem>()
    private var newList = emptyList<IItem>()

    fun setItems(oldList: List<IItem>, newList: List<IItem>) {
        this.oldList = oldList
        this.newList = newList
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (newItem is Currency && oldItem is Currency) {
            return newItem.id == oldItem.id
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (newItem is Currency && oldItem is Currency) {
            return newItem.id == oldItem.id && newItem.amount == oldItem.amount
        }
        return oldItem == newItem
    }

    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        val fields = mutableSetOf<CurrencyPayload>()
        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        if (newItem is Currency && oldItem is Currency) {
            if (oldItem.amount != newItem.amount) fields.add(CurrencyPayload.AMOUNT)
        }

        return when {
            fields.isNotEmpty() -> fields
            else -> super.getChangePayload(oldPosition, newPosition) // null
        }
    }
}