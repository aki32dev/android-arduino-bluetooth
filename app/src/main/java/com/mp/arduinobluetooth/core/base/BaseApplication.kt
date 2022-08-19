package com.mp.arduinobluetooth.core.base

import android.app.Application
import com.mp.arduinobluetooth.BuildConfig
import timber.log.Timber

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}