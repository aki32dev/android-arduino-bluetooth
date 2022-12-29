package com.arbl.arduinobluetooth.core.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    val showLoadingLiveData = MutableLiveData<Boolean>()
}