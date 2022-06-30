package dev.imam.currencyconverter.model

import kotlin.time.Duration

class VideoContent(
    val id:Int,
    val title:String,
    val description: String,
    private val duration: Duration,
    val stringImageUrl:String
){
    val durationString: String
        get() = duration.toComponents { minutes, seconds, _ ->
            var result:StringBuilder = StringBuilder()

            if(minutes > 0){
                result.append("$minutes:")
            } else {
                result.append("00:")
            }

            if(seconds > 0){
                result.append("$seconds")
            } else {
                result.append("00")
            }
            return@toComponents result.toString()
        }
}