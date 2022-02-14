package com.example.arduinobluetooth.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDB(context : Context?) : SQLiteOpenHelper(context, "local.db", null, 1) {
    override fun onCreate(DB: SQLiteDatabase?) {
        DB!!.execSQL("create Table listitems(name TEXT primary key, id TEXT, code TEXT, stock TEXT)")
    }

    override fun onUpgrade(DB: SQLiteDatabase?, p1: Int, p2: Int) {
        DB!!.execSQL("drop Table if exists listitems")
    }

    fun inputItem(name : String?, data : String?): Boolean {
        val DB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name"  , name)
        contentValues.put("data", data)
        val result = DB.insert("listitems", null, contentValues)
        return if (result == -1L) {false}
        else{true}
    }
    @SuppressLint("Recycle")
    fun updateItem(name : String?, data : String?): Boolean {
        val DB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("data", data)
        val cursor = DB.rawQuery("Select * from listitems where name = ?", arrayOf(name))
        return if (cursor.count > 0) {
            val result = DB.update("listitems", contentValues, "name=?", arrayOf(name)).toString()
            if (result == "") {false}
            else {true}
        }
        else {false}
    }

    @SuppressLint("Recycle")
    fun deleteItem(name : String?): Boolean {
        val DB = this.writableDatabase
        val cursor = DB.rawQuery("Select * from listitems where name = ?", arrayOf(name))
        return if (cursor.count > 0) {
            val result = DB.delete("listitems", "name=?", arrayOf(name)).toLong()
            if (result == -1L) {false}
            else {true}
        }
        else {false}
    }

    fun getItem(): Cursor {
        val DB = this.writableDatabase
        return DB.rawQuery("Select * from listitems", null)
    }
}