package com.arbl.arduinobluetooth.core.domain.usecase

import androidx.lifecycle.LiveData
import com.arbl.arduinobluetooth.core.data.source.local.entity.CommandEntity
import com.arbl.arduinobluetooth.core.domain.model.CommandModel

interface MainUseCase {
    fun insertCommand(command: CommandModel)

    fun updateCommand(command: CommandModel)

    fun deleteCommand(command: CommandModel)

    fun deleteAllCommand()

    fun getAllCommand(): LiveData<List<CommandEntity>>
}