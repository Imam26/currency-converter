package dev.imam.currencyconverter

import android.app.Application
import dev.imam.currencyconverter.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*

class App : Application() {
    val startTime: Long = System.currentTimeMillis()
    private var timer: Timer? = null

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(mainModule)
        }
    }

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