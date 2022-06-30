package dev.imam.currencyconverter.model

import kotlin.time.DurationUnit
import kotlin.time.toDuration

class VideoContentService {
    private var videoContentList = mutableListOf<VideoContent>()

    init {
        videoContentList = mutableListOf(
            VideoContent(1, "видео 1", "Описание к видео 1", 742.0.toDuration(DurationUnit.SECONDS), "https://images.squarespace-cdn.com/content/v1/5bc8c5defb22a53463942f43/1611887801930-SFPEEPVLTZHCO9NZFDQ8/Movie+Poster+16x9.jpg?format=500w"),
            VideoContent(2, "видео 2", "Описание к видео 2", 765.0.toDuration(DurationUnit.SECONDS), "https://wallpapersmug.com/download/1600x900/526278/justice-league-fan-art-poster-4k-8k.jpg"),
            VideoContent(3, "видео 3", "Описание к видео 3",765.0.toDuration(DurationUnit.SECONDS), "https://images.squarespace-cdn.com/content/v1/5bc8c5defb22a53463942f43/1611887801930-SFPEEPVLTZHCO9NZFDQ8/Movie+Poster+16x9.jpg?format=500w"),
            VideoContent(4, "видео 4", "Описание к видео 4",767.0.toDuration(DurationUnit.SECONDS), "https://wallpapersmug.com/download/1600x900/526278/justice-league-fan-art-poster-4k-8k.jpg"),
            VideoContent(5, "видео 5", "Описание к видео 5",796.0.toDuration(DurationUnit.SECONDS), "https://images.squarespace-cdn.com/content/v1/5bc8c5defb22a53463942f43/1611887801930-SFPEEPVLTZHCO9NZFDQ8/Movie+Poster+16x9.jpg?format=500w"),
            VideoContent(6, "видео 6", "Описание к видео 6",798.0.toDuration(DurationUnit.SECONDS), "https://wallpapersmug.com/download/1600x900/526278/justice-league-fan-art-poster-4k-8k.jpg"),
        )
    }

    fun getVideoContentList() : MutableList<VideoContent> {
        return videoContentList
    }
}