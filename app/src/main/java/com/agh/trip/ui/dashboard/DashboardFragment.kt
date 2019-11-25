package com.agh.trip.ui.dashboard

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.agh.trip.backend.data.dto.PaymentData
import com.agh.trip.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private var amountOfFieldsEdited = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val addPaymentButton: Button = root.findViewById(R.id.add_payment_button)
        addPaymentButton.setOnClickListener {
            if (allFieldsAreFilled(root)) {
                createNewPaymentInModel(root)
                dashboardViewModel.persistNewPaymentData()
                hideKeyboard()
                findNavController().navigate(R.id.navigation_home)
            }
        }
        return root
    }

    private fun allFieldsAreFilled(root: View): Boolean {
        val name = root.findViewById<EditText>(R.id.name_edit).text.toString()
        val paymentDescription =
            root.findViewById<EditText>(R.id.description_edit).text.toString()
        val amountPayed =
            root.findViewById<EditText>(R.id.amount_edit).text.toString()
        return name.isNotEmpty() && paymentDescription.isNotEmpty() && amountPayed.isNotEmpty()
    }

    private fun createNewPaymentInModel(root: View) {
        val name = root.findViewById<EditText>(R.id.name_edit).text.toString()
        val paymentDescription =
            root.findViewById<EditText>(R.id.description_edit).text.toString()
        val amountPayed =
            root.findViewById<EditText>(R.id.amount_edit).text.toString().toDouble()
        dashboardViewModel.newPaymentData =
            PaymentData(name, paymentDescription, amountPayed)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun handleEditText(it: EditText, root: View) {
        it.text.clear()
        amountOfFieldsEdited++

        if (amountOfFieldsEdited == 3) {
            val btn: Button = root.findViewById(R.id.add_payment_button)
            btn.isEnabled = true
        }
    }
}