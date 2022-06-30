package dev.imam.currencyconverter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.imam.currencyconverter.R
import dev.imam.currencyconverter.model.VideoContent

class VideoContentViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_video, parent, false)) {

    private val videoImageView = itemView.findViewById<ImageView>(R.id.videoItemImageView)
    private val videoNameTextView = itemView.findViewById<TextView>(R.id.videoNameTextView)
    private val videoFullTimeTextView = itemView.findViewById<TextView>(R.id.videoFullTimeTextView)

    fun onBind(item: VideoContent){
        Glide.with(itemView.context)
            .load(item.stringImageUrl)
            .error(R.drawable.video_image)
            .into(videoImageView);

        videoNameTextView.text = item.title
        videoFullTimeTextView.text = item.durationString
    }
}