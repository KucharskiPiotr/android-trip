package com.agh.trip.backend.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PaymentsOpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "InternalDb"
        const val TABLE_PAYMENTS = "payments"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_AMOUNT = "amount"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_PAYMENTS ( $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, $COLUMN_NAME TEXT NOT NULL, $COLUMN_DESCRIPTION TEXT, $COLUMN_AMOUNT NUMBER(10, 2) NOT NULL )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PAYMENTS")
    }
}