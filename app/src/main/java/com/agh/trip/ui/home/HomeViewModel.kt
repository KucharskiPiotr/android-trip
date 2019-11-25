package com.agh.trip.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agh.trip.backend.data.PaymentsDAO
import com.agh.trip.backend.data.dto.PaymentData

class HomeViewModel : ViewModel() {

    lateinit var paymentsList: List<PaymentData>

    fun getPaymentsFromDb() {
        paymentsList = PaymentsDAO.getAllItems()
    }
}