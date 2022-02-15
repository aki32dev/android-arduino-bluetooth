package com.example.arduinobluetooth.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MainViewModel : ViewModel() {
    private var stringData = MutableLiveData<String>()
    private var sendData = MutableLiveData<String>()

    open fun getData(): LiveData<String> {
        return stringData
    }

    open fun getSendData(): LiveData<String> {
        return sendData
    }

    open fun setData(data : String){
        stringData.postValue(data)
    }

    open fun setSendData(data : String){
        sendData.postValue(data)
    }
}