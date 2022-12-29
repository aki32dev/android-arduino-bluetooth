package com.arbl.arduinobluetooth.core.data.source.local

import com.arbl.arduinobluetooth.core.data.source.local.database.AppDao
import com.arbl.arduinobluetooth.core.data.source.local.entity.CommandEntity

class LocalDataSource(
    private val appDao: AppDao
) {
    fun insertCommand(command: CommandEntity) = appDao.insertCommand(command)

    fun updateCommand(command: CommandEntity) = appDao.updateCommand(command)

    fun deleteCommand(command: CommandEntity) = appDao.deleteCommand(command)

    fun deleteAllCommand() = appDao.deleteAllCommand()

    fun getAllCommand() = appDao.getAllCommand()
}