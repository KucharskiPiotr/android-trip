package com.agh.trip.backend.data

import com.agh.trip.backend.data.dto.PaymentData

class PaymentsDAO {
    companion object {
        private val payments = ArrayList<PaymentData>()

        fun getAllItems(): List<PaymentData> {
            return payments
        }

        fun addItem(item: PaymentData) {
            payments.add(item)
        }
    }
}