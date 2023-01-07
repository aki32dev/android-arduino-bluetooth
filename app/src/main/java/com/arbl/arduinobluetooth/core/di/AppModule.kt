package com.arbl.arduinobluetooth.core.di

import androidx.room.Room
import com.arbl.arduinobluetooth.core.data.source.local.LocalDataSource
import com.arbl.arduinobluetooth.core.data.source.local.database.AppDatabase
import com.arbl.arduinobluetooth.core.data.source.repository.AppRepositoryImplementation
import com.arbl.arduinobluetooth.core.domain.repository.AppRepository
import com.arbl.arduinobluetooth.core.domain.usecase.MainInteractor
import com.arbl.arduinobluetooth.core.domain.usecase.MainUseCase
import com.arbl.arduinobluetooth.ui.main.MainViewModel
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { get<AppDatabase>().appDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("arbl".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "arbl.db"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .openHelperFactory(factory)
            .build()
    }
}

val useCaseModule = module {
    single<MainUseCase> { MainInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single<AppRepository> {
        AppRepositoryImplementation(
            get()
        )
    }
}