package com.unitedsoftek.accountbook.ui.home.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.unitedsoftek.accountbook.Models.AddQuatationResponseModel
import com.unitedsoftek.accountbook.Models.AddSalesResponseModel
import com.unitedsoftek.accountbook.Models.SalesListModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.SalesListItemBinding

class QuotationListadapter(
    var model:ArrayList<AddQuatationResponseModel>
) : RecyclerView.Adapter<QuotationListadapter.VhHolde>() {
    lateinit var binding: SalesListItemBinding
    var countSerialNo:Int?=0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhHolde {

        binding = SalesListItemBinding.inflate(LayoutInflater.from(parent.context))

        return VhHolde(binding.root)
    }

    override fun onBindViewHolder(holder: VhHolde, position: Int) {

        holder.itemname.text=model.get(position).item_name
        holder.txt_price.text="₹"+model.get(position).price
        holder.txt_quantity.text=model.get(position).quantity
        holder.txt_subtotal_amount.text=(model.get(position).price.toInt() * model.get(position).quantity.toInt()).toString()
        holder.txt_amount.text=(model.get(position).price.toInt() * model.get(position).quantity.toInt()).toString()
        countSerialNo=countSerialNo!!+1
        holder.text_serial_no.text=countSerialNo.toString()+"."
        holder.txt_tax_amount.text="6"
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class VhHolde(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var itemname=itemView.findViewById<TextView>(R.id.txt_item_name)
    var  txt_amount=itemView.findViewById<TextView>(R.id.txt_amount)
    var  txt_quantity=itemView.findViewById<TextView>(R.id.txt_quantity)
    var  txt_price=itemView.findViewById<TextView>(R.id.txt_price)
    var  txt_subtotal_amount=itemView.findViewById<TextView>(R.id.txt_subtotal_amount)
    var  txt_tax_amount=itemView.findViewById<TextView>(R.id.txt_tax_amount)
    var  text_serial_no=itemView.findViewById<TextView>(R.id.text_serial_no)

    }
}