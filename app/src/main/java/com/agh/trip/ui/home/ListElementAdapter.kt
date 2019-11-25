package com.agh.trip.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.agh.trip.backend.data.dto.PaymentData
import com.agh.trip.R

class ListElementAdapter(
    context: Context,
    data: List<PaymentData>
) : ArrayAdapter<PaymentData>(context, R.layout.row_item, data) {

    @SuppressLint("StaticFieldLeak")
    private object ViewHolder {
        lateinit var name: TextView
        lateinit var description: TextView
        lateinit var amount: TextView
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val viewHolder: ViewHolder
        val result: View

        if (convertView == null) {
            viewHolder = ViewHolder
            val cv = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
            viewHolder.name = cv.findViewById(R.id.name)
            viewHolder.description = cv.findViewById(R.id.description)
            viewHolder.amount = cv.findViewById(R.id.amount)
            cv.tag = viewHolder
            result = cv
        }
        else {
            viewHolder = convertView.tag as ViewHolder
            result = convertView
        }

        viewHolder.name.text = item?.name
        viewHolder.description.text = item?.description
        viewHolder.amount.text = item?.amount?.format(2) + " " + context.getString(R.string.default_currency)
        return result
    }
}