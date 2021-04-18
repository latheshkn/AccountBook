package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.invoicekotlin.CustomBottomSheetDialogFragment
import com.unitedsoftek.accountbook.Models.AddSalesModel
import com.unitedsoftek.accountbook.Models.AddSalesResponseModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentSalesBinding
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class SalesFragment : Fragment() {
    val agrs: SalesFragmentArgs by navArgs()
    lateinit var binding: FragmentSalesBinding
    lateinit var mobile_gst_tv: TextView
    lateinit var pricing_tv: TextView
    lateinit var add_gst_tax_details_tv: TextView
    lateinit var card_view_cvvv: ConstraintLayout
    lateinit var Add_items_btn: Button
    lateinit var mobile_num_et: EditText

    lateinit var Tax_et: EditText
    lateinit var gst_et: EditText
    lateinit var enter_party_name_et: EditText
    lateinit var item_name_et: EditText
    lateinit var sales_price_et: EditText
    lateinit var purchace_price_et: EditText
    lateinit var discount_et: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_sales, container, false)

        binding = FragmentSalesBinding.inflate(inflater, container, false)

        mobile_gst_tv = binding.mobileGstTv
        pricing_tv = binding.pricingTv
        add_gst_tax_details_tv = binding.addGstTaxDetailsTv
        card_view_cvvv = binding.cardViewCvvv
        Add_items_btn = binding.AddItemsBtn
        mobile_num_et = binding.mobileNumEt
        Tax_et = binding.TaxEt
        gst_et = binding.gstEt
        item_name_et = binding.itemNameEt
        enter_party_name_et = binding.enterPartyNameEt
        sales_price_et = binding.salesPriceEt
        purchace_price_et = binding.purchacePriceEt
        discount_et = binding.discountEt

        val customBottomSheetDialogFragment = CustomBottomSheetDialogFragment()
        if(agrs==null)
        {
            var Cname = ""
            var Cmobile =""
            var Cgst = ""
        }else{
            var Cname = agrs.Cname
            var Cmobile = agrs.Cmobile
            var Cgst = agrs.Cgst
            enter_party_name_et.setText(Cname)
            gst_et.setText(Cgst)
            mobile_num_et.setText(Cmobile)

        }

        var one: Boolean = false
        mobile_gst_tv.setOnClickListener {
            if (one) {
                gst_et.isVisible = false
                mobile_num_et.isVisible = false
                one = false
            } else {
                gst_et.isVisible = true
                mobile_num_et.isVisible = true
                one = true
            }

        }

        pricing_tv.setOnClickListener {
            if (one) {
                card_view_cvvv.isVisible = false

                one = false
            } else {
                card_view_cvvv.isVisible = true

                one = true
            }

        }
        add_gst_tax_details_tv.setOnClickListener {
            if (one) {

                Tax_et.isVisible = false

                one = false
            } else {

                Tax_et.isVisible = true

                one = true
            }

        }

        Add_items_btn.setOnClickListener {
            sendToSalesList()

        }


        return binding.root
    }


    private fun sendToSalesList() {

        var itemName = item_name_et.text.toString()
        var partyName = enter_party_name_et.text.toString()
        var price = sales_price_et.text.toString()
        var quatity = purchace_price_et.text.toString()
        var tax = Tax_et.text.toString()
        var mobileNo = mobile_num_et.text.toString()
        var gstNo = gst_et.text.toString()

        var discount = discount_et.text.toString()

        if (itemName.isEmpty()) {
            item_name_et.setError("required")
            return
        }
        if (partyName.isEmpty()) {
            enter_party_name_et.setError("required")
            return
        }
        if (price.isEmpty()) {
            sales_price_et.setError("required")
            return
        }
        if (quatity.isEmpty()) {
            purchace_price_et.setError("required")
            return
        }
        if (mobileNo.isEmpty()) {
            mobile_num_et.setError("required")
            return
        }

        var action = SalesFragmentDirections.actionSalesFragmentToItemSalesListFragment(
            itemName, partyName,
            mobileNo, gstNo, price, quatity, discount, tax
        )
        findNavController().navigate(action)

    }


}