package com.example.arduinobluetooth.model

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

open class MainViewModel : ViewModel() {
    private var clickCount : Int = 0
    private var countLiveData= MutableLiveData<Int>()

    open fun getInitialCount(): LiveData<Int> {
        return countLiveData
    }

    open fun getCurrentCount(){
        clickCount += 1
        countLiveData.postValue(clickCount)
    }
}