package com.agh.trip.ui.reckonings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agh.trip.backend.data.PaymentsDAO
import com.agh.trip.backend.data.dto.PaymentData

class ReckoningViewModel : ViewModel() {

    fun generateReckoning(context: Context) : HashMap<Pair<String, String>, Double> {
        val payments = PaymentsDAO.getAllItemsList(context)
        val participantsAndPays = HashMap<String, Double>()
        payments.map {
            participantsAndPays[it.name]?.let { element ->
                participantsAndPays.put(it.name, element + it.amount)
            } ?: run {
                participantsAndPays.put(it.name, it.amount)
            }
        }
        val sum = payments.map(PaymentData::amount).sum()
        val amountPerParticipant = sum / participantsAndPays.keys.size
        participantsAndPays.forEach { participantsAndPays[it.key] = amountPerParticipant - it.value}
        val underpaidMap = participantsAndPays.filter { it.value >= 0 }.toMutableMap()
        val overpaidMap = participantsAndPays.filter { it.value < 0 }.toMutableMap()
        val reckonings = HashMap<Pair<String, String>, Double>()

        underpaidMap.forEach {underpaid ->
            overpaidMap.forEach { overpaid ->
                if (underpaid.value > 0.0 && overpaid.value < 0.0) {
                    if (underpaid.value >= -overpaid.value) {
                        reckonings[Pair(underpaid.key, overpaid.key)] = -overpaid.value
                        underpaidMap[underpaid.key] = underpaid.value + overpaid.value
                        overpaidMap[overpaid.key] = 0.0
                    }
                    else {
                        reckonings[Pair(underpaid.key, overpaid.key)] = underpaid.value
                        overpaidMap[overpaid.key] = overpaid.value + underpaid.value
                        underpaidMap[underpaid.key] = 0.0
                    }
                }
            }
        }
        return reckonings
    }
}