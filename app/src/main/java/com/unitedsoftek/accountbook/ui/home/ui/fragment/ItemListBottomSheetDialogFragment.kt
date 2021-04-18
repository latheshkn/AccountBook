package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unitedsoftek.accountbook.databinding.LayoutModalBottomSheetBinding

class ItemListBottomSheetDialogFragment(var senddata: SendData) : BottomSheetDialogFragment() {
    lateinit var binding: LayoutModalBottomSheetBinding
    lateinit var item_name_et: EditText
    lateinit var add_sale: Button
    lateinit var sales_price_et: EditText
    lateinit var purchace_price_et: EditText/*quantity*/
    lateinit var discount_et: EditText
    lateinit var gst_num_et: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutModalBottomSheetBinding.inflate(inflater, container, false)
        item_name_et = binding.itemNameEt
        add_sale = binding.addSale
        sales_price_et = binding.salesPriceEt
        purchace_price_et = binding.purchacePriceEt
        discount_et = binding.discountEt
        gst_num_et = binding.gstNumEt

        add_sale.setOnClickListener({
            var itemName = item_name_et.text.toString()
            var price = sales_price_et.text.toString()
            var quantity = purchace_price_et.text.toString()
            var discount = discount_et.text.toString()
            var tax = gst_num_et.text.toString()

            senddata.senddata(itemName,price,quantity,discount,tax)
        })

        return binding.root

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var one: Boolean = false

//      binding.morePricingTv.setOnClickListener {
//            //handle click event
//          if (one){
//              binding.cardViewCvvvv.isVisible = false
//
//              one=false
//          }else{
//              binding.cardViewCvvvv.isVisible = true
//
//              one=true
//          }
//
//            Toast.makeText(context, "First Button Clicked", Toast.LENGTH_SHORT).show()
//        }
//        binding.addMoreGstTaxDetailsTv.setOnClickListener {
//            if (one){
//              binding.gstNumEt.isVisible = false
//                binding.TaxEt.isVisible = false
//
//                one=false
//            }else{
//                binding.gstNumEt.isVisible = true
//                binding.TaxEt.isVisible = true
//
//                one=true
//            }
//            //handle click event
//            Toast.makeText(context, "Second Button Clicked", Toast.LENGTH_SHORT).show()
//        }


    }

    interface SendData {

        fun senddata(Itemname: String,price:String,quantity:String,discount:String,tax:String)
    }
}