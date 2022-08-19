package com.mp.arduinobluetooth.core.data.source.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mp.arduinobluetooth.core.data.source.local.entity.Command

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCommand(command: Command)

    @Update
    suspend fun updateCommand(command: Command)

    @Delete
    suspend fun deleteCommand(command: Command)

    @Query("DELETE FROM command_table")
    suspend fun deleteAllUser()

    @Query("SELECT * FROM command_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Command>>
}