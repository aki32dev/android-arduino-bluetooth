package com.arbl.arduinobluetooth.core.domain.usecase

import androidx.lifecycle.LiveData
import com.arbl.arduinobluetooth.core.data.source.local.entity.CommandEntity
import com.arbl.arduinobluetooth.core.domain.model.CommandModel
import com.arbl.arduinobluetooth.core.domain.repository.AppRepository

class MainInteractor(
    private val repository: AppRepository
) : MainUseCase {
    override fun insertCommand(command: CommandModel) = repository.insertCommand(command)

    override fun updateCommand(command: CommandModel) = repository.updateCommand(command)

    override fun deleteCommand(command: CommandModel) = repository.deleteCommand(command)

    override fun deleteAllCommand() = repository.deleteAllCommand()

    override fun getAllCommand(): LiveData<List<CommandEntity>> = repository.getAllCommand()
}