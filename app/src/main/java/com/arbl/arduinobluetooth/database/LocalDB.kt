package com.arbl.arduinobluetooth.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDB(context : Context?) : SQLiteOpenHelper(context, "local.db", null, 1) {
    override fun onCreate(database: SQLiteDatabase?) {
        database!!.execSQL("create Table listitems(title TEXT primary key, data TEXT)")
    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        database!!.execSQL("drop Table if exists listitems")
    }

    fun inputItem(title : String?, data : String?): Boolean {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("title"  , title)
        contentValues.put("data", data)
        val result = database.insert("listitems", null, contentValues)
        return result != -1L
    }

    @SuppressLint("Recycle")
    fun updateItem(title : String?, data : String?): Boolean {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("data", data)
        val cursor = database.rawQuery("Select * from listitems where title = ?", arrayOf(title))
        return if (cursor.count > 0) {
            val result = database.update("listitems", contentValues, "title=?", arrayOf(title)).toString()
            result != ""
        }
        else {false}
    }

    @SuppressLint("Recycle")
    fun deleteItem(title : String?): Boolean {
        val database = this.writableDatabase
        val cursor = database.rawQuery("Select * from listitems where title = ?", arrayOf(title))
        return if (cursor.count > 0) {
            val result = database.delete("listitems", "title=?", arrayOf(title)).toLong()
            result != -1L
        }
        else {false}
    }

    fun getItem(): Cursor {
        val database = this.writableDatabase
        return database.rawQuery("Select * from listitems", null)
    }
}