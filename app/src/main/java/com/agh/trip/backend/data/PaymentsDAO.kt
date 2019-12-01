package com.agh.trip.backend.data

import android.content.ContentValues
import android.content.Context
import com.agh.trip.backend.data.db.PaymentsOpenHelper
import com.agh.trip.backend.data.dto.PaymentData

class PaymentsDAO {
    companion object {
        private val payments = ArrayList<PaymentData>()

        fun getAllItemsList(context: Context): List<PaymentData> {
            val db = PaymentsOpenHelper(context, null)
            val cursor = db.readableDatabase.rawQuery(
                "SELECT * FROM ${PaymentsOpenHelper.TABLE_PAYMENTS}",
                null
            )
            val result = ArrayList<PaymentData>()
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(PaymentsOpenHelper.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(PaymentsOpenHelper.COLUMN_NAME))
                val description =
                    cursor.getString(cursor.getColumnIndex(PaymentsOpenHelper.COLUMN_DESCRIPTION))
                val amount =
                    cursor.getDouble(cursor.getColumnIndex(PaymentsOpenHelper.COLUMN_AMOUNT))
                result.add(PaymentData(id, name, description, amount))
            }
            cursor.close()
            return result
        }

        fun addItem(context: Context, item: PaymentData) {
            val db = PaymentsOpenHelper(context, null)
            val values = ContentValues()
            values.put(PaymentsOpenHelper.COLUMN_AMOUNT, item.amount)
            values.put(PaymentsOpenHelper.COLUMN_NAME, item.name)
            values.put(PaymentsOpenHelper.COLUMN_DESCRIPTION, item.description)
            db.readableDatabase.insert(PaymentsOpenHelper.TABLE_PAYMENTS, null, values)
            db.close()
        }
    }
}