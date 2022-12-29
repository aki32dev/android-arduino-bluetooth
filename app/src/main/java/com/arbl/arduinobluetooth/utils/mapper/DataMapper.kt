package com.arbl.arduinobluetooth.utils.mapper

import com.arbl.arduinobluetooth.core.data.source.local.entity.CommandEntity
import com.arbl.arduinobluetooth.core.domain.model.CommandModel

object DataMapper {

    fun mapCommandDomainToEntity(input: CommandModel): CommandEntity =
        input.let {
            CommandEntity(
                id = it.id,
                title = it.title,
                command = it.command
            )
        }

}