package com.unitedsoftek.accountbook.ui.home.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.unitedsoftek.accountbook.Models.getCustomerList.GetCustomerListModelResponse
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.ContactListLayoutBinding

class ContactListadapter(
    var model: ArrayList<GetCustomerListModelResponse>,
 var context:Context,var sendclik:sendClick
) : RecyclerView.Adapter<ContactListadapter.VhHolde>() {
    lateinit var binding: ContactListLayoutBinding
    var currentselectedIndex = -1
 var click:Boolean?=false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhHolde {

//        binding = ContactListLayoutBinding.inflate(LayoutInflater.from(parent.context))
//
//        return VhHolde(binding.root)

        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_list_layout, parent, false)

        return VhHolde(view)
    }

    override fun onBindViewHolder(holder: VhHolde, position: Int) {

        holder.pName.text = model.get(position).customer_name
        holder.MobileNo.text = model.get(position).customer_contact_no



        holder.itemView.setOnLongClickListener {

            markSelectedItem(position)


        }
        holder.itemView.setOnClickListener {
            diselectItem(position)
            if (holder.imagesselceted.visibility==View.GONE){

                sendclik.sendDialogClick(model.get(position).customer_contact_no,
                    model.get(position).customer_name,model.get(position).customer_contact_no,
                    model.get(position).customer_gst_no
                    ,position)
            }

        }
        holder.imagesselceted.setOnClickListener {

            sendclik.senditemClick(model.get(position).customer_contact_no,position)
            notifyDataSetChanged()
        }

        if (model.get(position).selected==true) {

            holder.imagesselceted.visibility = View.VISIBLE

        } else {

            holder.imagesselceted.visibility = View.GONE
        }

    }

    private fun diselectItem(position: Int) {
        if (currentselectedIndex==position){
            currentselectedIndex=-1
            model.get(position).selected=false
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

        var pName = itemView.findViewById<TextView>(R.id.pName)
        var MobileNo = itemView.findViewById<TextView>(R.id.MobileNo)
        var Constraint = itemView.findViewById<ConstraintLayout>(R.id.constraintParty)
        var imagesselceted = itemView.findViewById<ImageView>(R.id.imagesselceted)
    }

interface sendClick{

        fun senditemClick(str:String,position:Int)
        fun sendDialogClick(str:String,Cname:String,Cmobile:String,Cgst:String,position:Int)
    }
}