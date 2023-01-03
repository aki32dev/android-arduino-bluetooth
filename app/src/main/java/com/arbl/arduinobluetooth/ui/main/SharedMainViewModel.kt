package com.arbl.arduinobluetooth.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.arbl.arduinobluetooth.core.data.source.local.database.AppDatabase
import com.arbl.arduinobluetooth.core.data.source.local.entity.CommandEntity
import com.arbl.arduinobluetooth.core.data.source.repository.AppRepositoryImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class SharedMainViewModel(
    application: Application
) : AndroidViewModel(application) {

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

}