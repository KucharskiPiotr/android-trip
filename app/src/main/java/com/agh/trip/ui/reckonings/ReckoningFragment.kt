package com.agh.trip.ui.reckonings

import android.os.Build
import android.os.Bundle
import android.text.Html
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

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

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

        context?.let { context ->
            val reckonings = reckoningViewModel.generateReckoning(context)
            val reckonignTexts = reckonings.map {
                Html.fromHtml(
                    "<b>${it.key.first}</b> should give <b>${it.key.second}</b>: ${it.value.format(
                        2
                    )} ${getString(R.string.default_currency)}", Html.FROM_HTML_MODE_COMPACT
                )
            }
            val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, reckonignTexts)
            listView.adapter = adapter
        }

        return root
    }
}