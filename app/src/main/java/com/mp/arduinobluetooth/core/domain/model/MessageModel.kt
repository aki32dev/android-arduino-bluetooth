package com.mp.arduinobluetooth.core.domain.model

import java.util.*

class MessageModel(
    var message: String,
    var messageType: Int
    ) {
    var messageTime : Date = Date()
}