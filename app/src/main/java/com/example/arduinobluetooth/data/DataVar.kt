package com.example.arduinobluetooth.data

import androidx.annotation.StringRes
import com.example.arduinobluetooth.R
import java.util.*

object DataVar {
    @StringRes
    val TAB_TITLES = intArrayOf(
        R.string.stringTab1,
        R.string.stringTab2,
        R.string.stringTab3
    )

    val TAB_ICONS = intArrayOf(
        R.drawable.ic_home,
        R.drawable.ic_control,
        R.drawable.ic_list
    )

    const val bluetoothRequestPermit    : Int               = 101

    const val dbEdit                    : Int               = 0
    const val dbDelete                  : Int               = 1
    const val dbSend                    : Int               = 2
    const val dbTitle                   : String            = "dbTitle"
    const val dbData                    : String            = "dbData"

    const val messageStateChanged       : Int               = 0
    const val messageRead               : Int               = 1
    const val messageWrite              : Int               = 2
    const val messageDeviceName         : Int               = 3
    const val messageToast              : Int               = 4
    const val messageConnect            : Int               = 5
    const val deviceName                : String            = "deviceName"
    const val deviceMac                 : String            = "deviceMac"
    const val toast                     : String            = "toast"

    val appUUID                         : UUID              = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    const val appName                   : String            = "Arduino Bluetooth"

    const val stateNone                 : Int               = 0
    const val stateListen               : Int               = 1
    const val stateConnecting           : Int               = 2
    const val stateConnected            : Int               = 3
}
