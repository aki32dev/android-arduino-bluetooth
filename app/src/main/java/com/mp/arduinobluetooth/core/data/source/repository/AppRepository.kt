package com.mp.arduinobluetooth.core.data.source.repository

import androidx.lifecycle.LiveData
import com.mp.arduinobluetooth.core.data.source.local.database.AppDao
import com.mp.arduinobluetooth.core.data.source.local.entity.Command

class AppRepository(
    private val appDao: AppDao
) {
    val readAllData: LiveData<List<Command>> = appDao.readAllData()

    suspend fun addCommand(command: Command) {
        appDao.addCommand(command)
    }

    suspend fun updateCommand(command: Command) {
        appDao.updateCommand(command)
    }

    suspend fun deleteCommand(command: Command) {
        appDao.deleteCommand(command)
    }

    suspend fun deleteAllCommand() {
        appDao.deleteAllUser()
    }
}