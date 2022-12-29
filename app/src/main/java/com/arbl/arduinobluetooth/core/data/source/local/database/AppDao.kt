package com.arbl.arduinobluetooth.core.data.source.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.arbl.arduinobluetooth.core.data.source.local.entity.CommandEntity

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCommand(command: CommandEntity)

    @Update
    fun updateCommand(command: CommandEntity)

    @Delete
    fun deleteCommand(command: CommandEntity)

    @Query("DELETE FROM command_table")
    fun deleteAllCommand()

    @Query("SELECT * FROM command_table ORDER BY id ASC")
    fun getAllCommand(): LiveData<List<CommandEntity>>
}