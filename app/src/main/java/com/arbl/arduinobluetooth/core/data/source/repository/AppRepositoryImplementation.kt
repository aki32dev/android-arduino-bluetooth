package com.arbl.arduinobluetooth.core.data.source.repository

import androidx.lifecycle.LiveData
import com.arbl.arduinobluetooth.core.data.source.local.LocalDataSource
import com.arbl.arduinobluetooth.core.data.source.local.entity.CommandEntity
import com.arbl.arduinobluetooth.core.domain.model.CommandModel
import com.arbl.arduinobluetooth.core.domain.repository.AppRepository
import com.arbl.arduinobluetooth.utils.mapper.DataMapper.mapCommandDomainToEntity

class AppRepositoryImplementation(
    private val localDataSource: LocalDataSource
): AppRepository {

    val getAllCommand: LiveData<List<CommandEntity>> = localDataSource.getAllCommand()

    override fun insertCommand(command: CommandModel) {
        localDataSource.insertCommand(mapCommandDomainToEntity(command))
    }

    override fun updateCommand(command: CommandModel) {
        localDataSource.updateCommand(mapCommandDomainToEntity(command))
    }

    override fun deleteCommand(command: CommandModel) {
        localDataSource.deleteCommand(mapCommandDomainToEntity(command))
    }

    override fun deleteAllCommand() {
        localDataSource.deleteAllCommand()
    }

    override fun getAllCommand(): LiveData<List<CommandEntity>> {
        return getAllCommand
    }
}