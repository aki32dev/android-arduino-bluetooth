package com.example.arduinobluetooth.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MainViewModel : ViewModel() {
    private var stateConnect    = MutableLiveData<Boolean>()
    private var sendData        = MutableLiveData<String>()

    open fun setState(state : Boolean){
        stateConnect.postValue(state)
    }

    open fun getState(): LiveData<Boolean> {
        return stateConnect
    }

    open fun getSendData(): LiveData<String> {
        return sendData
    }

    open fun setSendData(data : String){
        sendData.postValue(data)
    }
}