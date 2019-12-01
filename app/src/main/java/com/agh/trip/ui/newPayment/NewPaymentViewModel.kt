package com.agh.trip.ui.newPayment

import android.content.Context
import androidx.lifecycle.ViewModel
import com.agh.trip.backend.data.PaymentsDAO
import com.agh.trip.backend.data.dto.PaymentData

class NewPaymentViewModel : ViewModel() {

    lateinit var newPaymentData: PaymentData

    fun persistNewPaymentData(context: Context) {
        PaymentsDAO.addItem(context, newPaymentData)
    }
}