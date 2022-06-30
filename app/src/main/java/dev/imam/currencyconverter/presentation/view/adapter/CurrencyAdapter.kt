package dev.imam.currencyconverter.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.presentation.helper.CurrencyDiffCallback
import dev.imam.currencyconverter.presentation.model.Add
import dev.imam.currencyconverter.presentation.model.CurrencyModel
import dev.imam.currencyconverter.presentation.model.IItem
import dev.imam.currencyconverter.presentation.view.viewholder.AddViewHolder
import dev.imam.currencyconverter.presentation.view.viewholder.CurrencyViewHolder

class CurrencyAdapter(
    private val clickListener: () -> Unit,
    private val onLongClickListener: (item: IItem, position:Int) -> Unit,
    private val onTextChange:(text:String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = mutableListOf<IItem>()
    private val diffCallback = CurrencyDiffCallback()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when(viewType){
            R.layout.item_currency -> CurrencyViewHolder(inflater, parent)
            R.layout.item_add -> AddViewHolder(inflater, parent)
            else -> CurrencyViewHolder(inflater, parent)
        }

        if(viewType == R.layout.item_currency){
            viewHolder.itemView.setOnLongClickListener{
                var item = getItemByPosition(viewHolder.adapterPosition)
                onLongClickListener(item, viewHolder.adapterPosition)
                return@setOnLongClickListener true
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CurrencyViewHolder -> holder.onBind(data[position] as CurrencyModel, onTextChange)
            is AddViewHolder -> holder.onBind(clickListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads:MutableList<Any>) {
        when(holder){
            is CurrencyViewHolder -> {
                if (payloads.isEmpty()) {
                    onBindViewHolder(holder, position)
                } else {
                    holder.onBind(data[position] as CurrencyModel, payloads.first() as? Set<*>)
                }
            }
            is AddViewHolder -> holder.onBind(clickListener)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when(data[position]){
            is CurrencyModel -> R.layout.item_currency
            is Add ->  R.layout.item_add
            else ->  R.layout.item_currency
        }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItemByPosition(position:Int) : IItem {
        return data[position]
    }

    fun setItemsWithDiffCallback(list: List<CurrencyModel>) {
        data.removeLast()
        diffCallback.setItems(data, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback, false)
        diffResult.dispatchUpdatesTo(this)
        data.clear()
        data.add(Add())
        data.addAll(0, list)
    }

    fun setItems(list: List<IItem>) {
        data.clear()
        data.add(Add())
        data.addAll(0, list)
        notifyDataSetChanged()
    }

    fun moveItem(from: Int, to: Int) {
        if(to > data.size) return
        val fromItem = data[from]
        data.removeAt(from)
        if (to >= data.size){
            data.add(to - 1, fromItem)
            notifyItemMoved(from, to - 1)
        } else {
            data.add(to, fromItem)
            notifyItemMoved(from, to)
        }
    }
}
