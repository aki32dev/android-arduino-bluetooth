package com.arbl.arduinobluetooth.utils.constant

import androidx.annotation.StringRes
import com.arbl.arduinobluetooth.R

object Constants {
    @StringRes
    val TAB_TITLES = intArrayOf(
        R.string.stringTabHome,
        R.string.stringTabDatabase
    )
    const val bluetoothRequestPermit    : Int               = 101
    const val messageStateChanged       : Int               = 0
    const val messageRead               : Int               = 1
    const val messageWrite              : Int               = 2
    const val messageDeviceName         : Int               = 3
    const val messageToast              : Int               = 4
    const val messageString             : String            = "messageString"
    const val appName                   : String            = "Arduino Bluetooth"
    const val stateNone                 : Int               = 0
    const val stateListen               : Int               = 1
    const val stateConnecting           : Int               = 2
    const val stateConnected            : Int               = 3
}
