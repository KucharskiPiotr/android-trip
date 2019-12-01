package com.agh.trip.ui.reckonings

import android.content.Context
import android.os.AsyncTask
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

class ReckoningFragment : Fragment() {

    private lateinit var reckoningViewModel: ReckoningViewModel

    fun Double.format(digits: Int) = "%.${digits}f".format(this)


    inner class ReckoningCalulcator : AsyncTask<Context, Void, HashMap<Pair<String, String>, Double>>() {
        override fun doInBackground(vararg context: Context?): HashMap<Pair<String, String>, Double> {
            context[0]?. let { context ->
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
                participantsAndPays.forEach {
                    participantsAndPays[it.key] = amountPerParticipant - it.value
                }
                val underpaidMap = participantsAndPays.filter { it.value >= 0 }.toMutableMap()
                val overpaidMap = participantsAndPays.filter { it.value < 0 }.toMutableMap()
                val reckonings = HashMap<Pair<String, String>, Double>()

                underpaidMap.forEach { underpaid ->
                    overpaidMap.forEach { overpaid ->
                        if (underpaid.value > 0.0 && overpaid.value < 0.0) {
                            if (underpaid.value >= -overpaid.value) {
                                reckonings[Pair(underpaid.key, overpaid.key)] = -overpaid.value
                                underpaidMap[underpaid.key] = underpaid.value + overpaid.value
                                overpaidMap[overpaid.key] = 0.0
                            } else {
                                reckonings[Pair(underpaid.key, overpaid.key)] = underpaid.value
                                overpaidMap[overpaid.key] = overpaid.value + underpaid.value
                                underpaidMap[underpaid.key] = 0.0
                            }
                        }
                    }
                }
                return reckonings
            }
            return HashMap()
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onPostExecute(result: HashMap<Pair<String, String>, Double>?) {
            val reckoningTexts = result?.map {
                Html.fromHtml(
                    "<b>${it.key.first}</b> should give <b>${it.key.second}</b>: ${it.value.format(
                        2
                    )} ${getString(R.string.default_currency)}", Html.FROM_HTML_MODE_COMPACT
                )
            }
            val listView = view!!.findViewById<ListView>(R.id.reckoning_list)
            val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, reckoningTexts!!)
            listView.adapter = adapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reckoningViewModel =
            ViewModelProviders.of(this).get(ReckoningViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reckonings, container, false)
        context?.let { ReckoningCalulcator().execute(it) }
        return root
    }
}