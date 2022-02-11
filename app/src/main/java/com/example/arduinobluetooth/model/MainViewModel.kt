package com.example.arduinobluetooth.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MainViewModel : ViewModel() {
    private var stringData = MutableLiveData<String>()

    open fun getData(): LiveData<String> {
        return stringData
    }

    open fun setData(data : String){
        stringData.postValue(data)
    }
}