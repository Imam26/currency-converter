package dev.imam.currencyconverter

import android.app.Application
import java.util.*

class App : Application() {
    val startTime: Long = System.currentTimeMillis()
    private var timer: Timer? = null

    fun startTimer(timerTask: TimerTask) {
        if (timer == null) {
            timer = Timer()
            timer?.schedule(timerTask, 0, 1000)
        }
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }
}