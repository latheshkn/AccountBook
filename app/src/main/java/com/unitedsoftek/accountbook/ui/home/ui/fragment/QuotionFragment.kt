package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.unitedsoftek.accountbook.Models.AddQuatationResponseModel
import com.unitedsoftek.accountbook.Models.AddQuotionModel
import com.unitedsoftek.accountbook.Models.AddSalesModel
import com.unitedsoftek.accountbook.Models.AddSalesResponseModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentQuotionBinding

import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.random.Random

class QuotionFragment : Fragment() {
    val args: QuotionFragmentArgs by navArgs()
    lateinit var binding: FragmentQuotionBinding
    lateinit var mobile_gst_tv: TextView
    lateinit var pricing_tv: TextView
    lateinit var gst_et: EditText
    lateinit var mobile_num_et: EditText
    lateinit var item_name_et: EditText
    lateinit var sales_price_et: EditText
    lateinit var enter_party_name_et: EditText
    lateinit var purchace_price_et: EditText
    lateinit var discount_et: EditText
    lateinit var gst_num_et: EditText
    lateinit var Add_items_btn: Button
    lateinit var card_view_cvvv: ConstraintLayout
    lateinit var add_gst_tax_details_tv: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentQuotionBinding.inflate(inflater, container, false)

        mobile_gst_tv = binding.mobileGstTv
        gst_et = binding.gstEt
        mobile_num_et = binding.mobileNumEt
        pricing_tv = binding.pricingTv
        card_view_cvvv = binding.cardViewCvvv
        add_gst_tax_details_tv = binding.addGstTaxDetailsTv
        item_name_et = binding.itemNameEt
        enter_party_name_et = binding.enterPartyNameEt
        sales_price_et = binding.salesPriceEt
        purchace_price_et = binding.purchacePriceEt
        discount_et = binding.discountEt
        gst_num_et = binding.gstNumEt
        Add_items_btn = binding.AddItemsBtn


        var Cname = args.Cname
        var Cmobile = args.Cmobile
        var Cgst = args.Cgst

        enter_party_name_et.setText(Cname)
        gst_et.setText(Cgst)
        mobile_num_et.setText(Cmobile)
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

                gst_num_et.isVisible = false

                one = false
            } else {

                gst_num_et.isVisible = true

                one = true
            }

        }
        Add_items_btn.setOnClickListener({
            sendToSalesList()
        })
        return binding.root
    }

    companion object {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun sendToSalesList() {

        var itemName = item_name_et.text.toString()
        var partyName = enter_party_name_et.text.toString()
        var price = sales_price_et.text.toString()
        var quatity = purchace_price_et.text.toString()
        var tax = gst_num_et.text.toString()
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

        var action = QuotionFragmentDirections.actionQuotionFragmentToItemQuotationListFragment(
            itemName, partyName,
            mobileNo, gstNo, price, quatity, discount, tax
        )
        findNavController().navigate(action)

    }
}