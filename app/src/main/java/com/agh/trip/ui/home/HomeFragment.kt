package com.agh.trip.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.agh.trip.R
import com.agh.trip.backend.data.PaymentsDAO
import com.agh.trip.backend.data.dto.PaymentData
import java.util.stream.Collectors
import java.util.stream.Collectors.toList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // TODO: Change from ListView to RecyclerView to improve performance

        val paymentsListView: ListView = root.findViewById(R.id.payments_list)
        context?.let { homeViewModel.getPaymentsFromDb(it) }
        val adapter = context?.let {
            ListElementAdapter(
                it,
                homeViewModel.paymentsList
            )
        }

        val sum = homeViewModel.paymentsList.stream().map(PaymentData::amount).collect(Collectors.toList()).sum()
        root.findViewById<TextView>(R.id.sum_label).text = sum.format(2) + " " + getString(R.string.default_currency)

        paymentsListView.adapter = adapter
        return root
    }
}