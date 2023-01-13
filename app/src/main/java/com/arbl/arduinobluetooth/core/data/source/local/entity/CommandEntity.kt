package com.arbl.arduinobluetooth.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "command_table")
data class CommandEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val command: String
)