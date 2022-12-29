package com.arbl.arduinobluetooth.core.base.application

import android.app.Application
import com.arbl.arduinobluetooth.BuildConfig
import com.arbl.arduinobluetooth.core.di.databaseModule
import com.arbl.arduinobluetooth.core.di.repositoryModule
import com.arbl.arduinobluetooth.core.di.useCaseModule
import com.arbl.arduinobluetooth.core.di.viewModelModule
import com.chibatching.kotpref.Kotpref
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Kotpref.init(this)
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    databaseModule,
                    useCaseModule,
                    viewModelModule,
                    repositoryModule
                )
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}