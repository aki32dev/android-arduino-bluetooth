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

    val messageStateChanged     : Int               = 0
    val messageRead             : Int               = 1
    val messageWrite            : Int               = 2
    val messageDeviceName       : Int               = 3
    val messageToast            : Int               = 4
    val messageConnect          : Int               = 5
    val deviceName              : String            = "deviceName"
    val deviceMac               : String            = "deviceMac"
    val toast                   : String            = "toast"

    val appUUID                 : UUID              = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    val appName                 : String            = "Arduino Bluetooth"

    val stateNone               : Int               = 0
    val stateListen             : Int               = 1
    val stateConnecting         : Int               = 2
    val stateConnected          : Int               = 3
}
