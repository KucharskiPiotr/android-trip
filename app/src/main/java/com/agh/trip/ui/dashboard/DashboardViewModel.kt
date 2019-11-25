package com.agh.trip.ui.dashboard

import androidx.lifecycle.ViewModel
import com.agh.trip.backend.data.PaymentsDAO
import com.agh.trip.backend.data.dto.PaymentData

class DashboardViewModel : ViewModel() {

    lateinit var newPaymentData: PaymentData

    fun persistNewPaymentData() {
        PaymentsDAO.addItem(newPaymentData)
    }
}