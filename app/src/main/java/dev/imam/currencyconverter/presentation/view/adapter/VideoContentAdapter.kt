package dev.imam.currencyconverter.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.imam.currencyconverter.presentation.model.VideoContent
import dev.imam.currencyconverter.presentation.view.viewholder.VideoContentViewHolder

class VideoContentAdapter(
    private val clickListener: (model: VideoContent) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = mutableListOf<VideoContent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VideoContentViewHolder(inflater, parent)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoContentViewHolder).onBind(data[position])
        holder.itemView.setOnClickListener { clickListener(data[position]) }
    }

    fun setItems(list: List<VideoContent>) {
        data.clear()
        data.addAll(0, list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size;
    }
}