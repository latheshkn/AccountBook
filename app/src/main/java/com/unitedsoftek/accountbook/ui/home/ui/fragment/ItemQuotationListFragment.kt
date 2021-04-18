package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unitedsoftek.accountbook.Models.AddQuatationResponseModel
import com.unitedsoftek.accountbook.Models.AddQuotionModel
import com.unitedsoftek.accountbook.Models.AddSalesResponseModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentItemQuotationListBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import com.unitedsoftek.accountbook.ui.home.ui.adapters.QuotationListadapter
import com.unitedsoftek.accountbook.ui.home.ui.adapters.SalesListadapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Math.random
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt

class ItemQuotationListFragment : Fragment(), ItemListBottomSheetDialogFragment.SendData {
    val args:ItemQuotationListFragmentArgs by navArgs()
    lateinit var binding: FragmentItemQuotationListBinding
    lateinit var recycler_quotation: RecyclerView
    lateinit var add_more_item: Button
    lateinit var confirm_btn: Button
    var itemName: String? = null
    var partyName: String? = null
    var price: String? = null
    var quatity: String? = null
    var tax: String? = null
    var mobileNo: String? = null
    var gstNo: String? = null
    var discount: String? = null
    var subtotal: Int? = 0
    var itelistfragment = ItemListBottomSheetDialogFragment(this)
    var total: Int? = 0
    var iftaxIsNotZero: Int? = 0
    var afterdiscount: Int? = 0
    var list = ArrayList<AddQuatationResponseModel>()
    lateinit var salesListadapter: QuotationListadapter
    var amount: Int? = null
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentItemQuotationListBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        recycler_quotation = binding.recyclerQuotation
        add_more_item = binding.addMoreItem
        confirm_btn = binding.confirmBtn

        itemName = args.itemName
        partyName = args.partyName
        price = args.price
        quatity = args.quantity
        mobileNo = args.mobilrNo
        gstNo = args.gst
        discount = args.discount
        tax = args.tax

        add_more_item.setOnClickListener({
            if (!itelistfragment.isAdded) {
                itelistfragment.show(parentFragmentManager, "show")

            }

        })

        amount=price!!.toInt()*quatity!!.toInt()
        subtotal = (amount!!.toInt() * discount!!.toInt()) / 100
        if (!discount!!.isEmpty()){
            afterdiscount=amount!!-subtotal!!
        }else{
            afterdiscount=amount
        }

        if (!tax!!.isEmpty()){
            var aftrtax=(afterdiscount!!*tax!!.toInt())/100
            Log.d("iftax",iftaxIsNotZero.toString())
            iftaxIsNotZero=aftrtax+afterdiscount!!
            Log.d("iftax",iftaxIsNotZero.toString())
        }else{
            iftaxIsNotZero=afterdiscount
            Log.d("iftax",iftaxIsNotZero.toString())
        }
        Log.d("subtotal",subtotal.toString())
        list.add(
            AddQuatationResponseModel(
                "1",
                itemName!!,
                quatity!!,
                price!!
            )
        )
        recycler_quotation.layoutManager = LinearLayoutManager(requireContext())
        salesListadapter = QuotationListadapter(list)
        recycler_quotation.adapter = salesListadapter

        confirm_btn.setOnClickListener({
            getLoginDetails(requireContext())
        })

        return binding.root
    }

    companion object {

    }

    override fun senddata(
        Itemname: String,
        price: String,
        quantity: String,
        discount: String,
        tax: String
    ) {

        itelistfragment.dismiss()
        amount=0
        amount=price.toInt()*quantity.toInt()
        subtotal = (amount!!.toInt() * discount!!.toInt()) / 100
        afterdiscount=amount!!-subtotal!!
        if (!discount!!.isEmpty()){
            afterdiscount=amount!!-subtotal!!
        }else{
            afterdiscount=amount!!
        }
        if (!tax.isEmpty()){
            var aftrtax=(afterdiscount!!*tax.toInt())/100
            iftaxIsNotZero=aftrtax
        }else{
            iftaxIsNotZero=subtotal
        }

        var num= (0..10).random()
        list.add(
            AddQuatationResponseModel(
                num.toString(),
                Itemname!!,
                quatity!!,
                price!!
            )
        )
        recycler_quotation.layoutManager = LinearLayoutManager(requireContext())
        salesListadapter = QuotationListadapter(list)
        recycler_quotation.adapter = salesListadapter
    }

    private suspend fun quotation(MobNo:String): Response<AddQuotionModel> {
        return withContext(Dispatchers.IO) {

            for (i in 0..list.size - 1) total=list.get(i).price.toInt()

            var num= (0..10).random()

            var sales = AddQuotionModel(
                MobNo,mobileNo!!, gstNo!!,
                partyName!!,num.toString(),
                total.toString(), MobNo,list
            )
            Log.d("Itemlist",mobileNo!!)
            Log.d("Itemlist",partyName!!)
            Log.d("Itemlist",gstNo!!)
            Log.d("Itemlist",total.toString())
            Log.d("Itemlist",num.toString())
            BaseClient.getInstance.AddQuotation(sales)
        }
    }

    private fun AddQuotation(context: Context,MobNo:String) {

        CoroutineScope(Dispatchers.Main).launch {
            try {

                var response = quotation(MobNo)

                if (response.isSuccessful) {
                    Toast.makeText(context, "success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()

                }

            } catch (e: Exception) {
                Toast.makeText(context?.applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

        }
    }

    fun getLoginDetails(context: Context){
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()
            AddQuotation(context,mobNum)
        })
    }
}