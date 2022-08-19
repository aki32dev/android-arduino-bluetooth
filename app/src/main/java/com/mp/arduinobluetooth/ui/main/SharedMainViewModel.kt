package com.mp.arduinobluetooth.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.mp.arduinobluetooth.core.data.source.local.database.AppDatabase
import com.mp.arduinobluetooth.core.data.source.local.entity.Command
import com.mp.arduinobluetooth.core.data.source.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class SharedMainViewModel(
    application: Application
) : AndroidViewModel(application) {

    // SHARED

    var stateConnect = MutableLiveData<Boolean>()
    open fun setState(parValue: Boolean){ stateConnect.value = parValue }

    var sendData     = MutableLiveData<String>()
    open fun setSendData(parValue : String){ sendData.value = parValue }

    private var receiveData  = MutableLiveData<String>()
    open fun setReceiveData(parValue : String){ receiveData.postValue(parValue) }
    open fun getReceiveData(): LiveData<String> { return receiveData }

    val nameData = MutableLiveData<String>()
    fun setNameData(parValue : String) { nameData.value = parValue }

    val macData = MutableLiveData<String>()
    fun setMacData(parValue : String) { macData.value = parValue }

    // REPO

    val readAllData: LiveData<List<Command>>
    private val repository: AppRepository

    init {
        val appDao = AppDatabase.getDatabase(application).appDao()
        repository = AppRepository(appDao)
        readAllData = repository.readAllData
    }

    fun addCommand(command: Command) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCommand(command)
        }
    }

    fun updateCommand(command: Command) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCommand(command)
        }
    }

    fun deleteCommand(command: Command) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCommand(command)
        }
    }

    fun deleteAllCommand() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllCommand()
        }
    }
}