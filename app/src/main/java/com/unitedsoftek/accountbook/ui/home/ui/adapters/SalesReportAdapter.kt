package com.unitedsoftek.accountbook.ui.home.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.unitedsoftek.accountbook.Models.getInvoiceReportModel.InvoiceReportModelResponse
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.SalesListItemBinding
import java.util.*
import kotlin.collections.ArrayList

class SalesReportAdapter(
    var model: ArrayList<InvoiceReportModelResponse>, var sendclik: sendClick, var context: Context
) : RecyclerView.Adapter<SalesReportAdapter.VhHolde>() {
    lateinit var binding: SalesListItemBinding
    var countSerialNo: Int? = 0
    var currentselectedIndex = -1



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhHolde {

        binding = SalesListItemBinding.inflate(LayoutInflater.from(parent.context))

        return VhHolde(binding.root)
    }

    override fun onBindViewHolder(holder: VhHolde, position: Int) {

//        countSerialNo = countSerialNo!! + 1
//        holder.text_serial_no.text = countSerialNo.toString() + "."
        holder.txt_amount.text = model.get(position).invoice_no
        holder.txt_subtotal_amount.text = model.get(position).total_amount
        holder.txt_tax_amount.text = model.get(position).invoice_date

        holder.itemView.setOnLongClickListener {

            markSelectedItem(position)


        }
        holder.itemView.setOnClickListener {
            diselectItem(position)
        }
        holder.imagesselceted.setOnClickListener {

            sendclik.senditemClick(model.get(position).invoice_no, position)
            notifyDataSetChanged()
        }

        if (model.get(position).selected == true) {

            holder.imagesselceted.visibility = View.VISIBLE

        } else {

            holder.imagesselceted.visibility = View.GONE
        }
    }

    private fun diselectItem(position: Int) {
        if (currentselectedIndex == position) {
            currentselectedIndex = -1
            model.get(position).selected = false
            notifyDataSetChanged()
        }

    }

    fun markSelectedItem(index: Int): Boolean {

        for (item in model) {

            item.selected = false

        }
        model.get(index).selected = true


        currentselectedIndex = index
        notifyDataSetChanged()
        return true
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class VhHolde(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text_serial_no = itemView.findViewById<TextView>(R.id.text_serial_no)
        var txt_amount = itemView.findViewById<TextView>(R.id.txt_amount)
        var txt_subtotal_amount = itemView.findViewById<TextView>(R.id.txt_subtotal_amount)
        var txt_tax_amount = itemView.findViewById<TextView>(R.id.txt_tax_amount)
        var imagesselceted = itemView.findViewById<ImageView>(R.id.imagesselceted)

    }

    interface sendClick {
        fun senditemClick(str: String, position: Int)
    }

//    override fun getFilter(): Filter {
//
//
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                Log.d("theresult", constraint.toString())
//                val resultList = ArrayList<InvoiceReportModelResponse>()
//                val charSearch = constraint.toString()
//                if (charSearch.isEmpty()) {
//
//                    resultList.addAll(model)
//                    Log.d("theresult", list.toString()
//                    )
//
//                } else {
//
//                    for (row in model) {
//                        if (row.equals(constraint.toString())) {
//                            resultList.add(row)
//                        }
//                        list=resultList
//                    }
//                }
//                val filterResults = FilterResults()
//                filterResults.values = resultList
//                return filterResults
//
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                list.clear()
//                list = results?.values as ArrayList<InvoiceReportModelResponse>
//                notifyDataSetChanged()
//            }
//        }
//
//    }

}