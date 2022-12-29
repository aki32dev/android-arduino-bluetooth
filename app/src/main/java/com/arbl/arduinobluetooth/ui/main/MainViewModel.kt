package com.arbl.arduinobluetooth.ui.main

import androidx.lifecycle.viewModelScope
import com.arbl.arduinobluetooth.core.base.viewmodel.BaseViewModel
import com.arbl.arduinobluetooth.core.domain.model.CommandModel
import com.arbl.arduinobluetooth.core.domain.usecase.MainUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainUseCase: MainUseCase
) : BaseViewModel() {

    val commandliveData = mainUseCase.getAllCommand()

    fun insertCommand(command: CommandModel) {
        viewModelScope.launch(Dispatchers.IO) { mainUseCase.insertCommand(command) }
    }

    fun updateCommand(command: CommandModel) {
        viewModelScope.launch(Dispatchers.IO) { mainUseCase.updateCommand(command) }
    }

    fun deleteCommand(command: CommandModel) {
        viewModelScope.launch(Dispatchers.IO) { mainUseCase.deleteCommand(command) }
    }

}