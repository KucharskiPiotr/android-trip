package com.agh.trip.ui.reckonings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.agh.trip.R
import com.agh.trip.backend.data.PaymentsDAO
import com.agh.trip.backend.data.dto.PaymentData
import java.util.stream.Collectors

class ReckoningFragment : Fragment() {

    private lateinit var reckoningViewModel: ReckoningViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reckoningViewModel =
            ViewModelProviders.of(this).get(ReckoningViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reckonings, container, false)

        val listView = root.findViewById<ListView>(R.id.reckoning_list)

        // Here is logic to calculate reckoning of payments, but its fucked up so feel free to delete this
        val reckonings = ArrayList<String >()
        val payments = PaymentsDAO.getAllItemsList(context!!)
        val sum = payments.stream().map(PaymentData::amount).collect(Collectors.toList()).sum()
        val participants = payments.stream().map(PaymentData::name).distinct().collect(Collectors.toList())
        val amountPerParticipant = sum / participants.size
        val moneySpentByParticipants = HashMap<String, Double>()
        val howMuchWhoShouldPay = HashMap<String, Double>()

        for (p in participants) {
            moneySpentByParticipants[p] = 0.0
        }

        for (p in payments) {
            moneySpentByParticipants[p.name]?.let { moneySpentByParticipants[p.name] = it + p.amount }
                ?: run { moneySpentByParticipants[p.name] = p.amount }
        }

        for (participantMoney in moneySpentByParticipants) {
            howMuchWhoShouldPay[participantMoney.key] = amountPerParticipant - participantMoney.value
        }

        for (participantReckoning in howMuchWhoShouldPay) {
//            if (participantReckoning.value > 0)
        }

        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_list_item_1,
                PaymentsDAO.getAllItemsList(context!!)
            )
        }

        return root
    }
}