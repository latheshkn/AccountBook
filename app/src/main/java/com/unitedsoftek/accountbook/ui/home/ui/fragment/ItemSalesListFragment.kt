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
import com.unitedsoftek.accountbook.Models.AddSalesModel
import com.unitedsoftek.accountbook.Models.AddSalesResponseModel
import com.unitedsoftek.accountbook.Models.SalesListModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentItemSalesListBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import com.unitedsoftek.accountbook.ui.home.ui.adapters.SalesListadapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class ItemSalesListFragment : Fragment(), ItemListBottomSheetDialogFragment.SendData {
    val args: ItemSalesListFragmentArgs by navArgs()
    lateinit var binding: FragmentItemSalesListBinding
    lateinit var recycler_sales: RecyclerView
    lateinit var salesListadapter: SalesListadapter
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
    var total: Int? = null
    var iftaxIsNotZero:Int?=0
    var afterdiscount:Int?=0
    var list = ArrayList<AddSalesResponseModel>()
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String
    var amount:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentItemSalesListBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        itemName = args.itemName
        partyName = args.partyName
        price = args.price
        quatity = args.quantity
        mobileNo = args.mobileNo
        gstNo = args.gst
        discount = args.discount
        tax = args.tax



        recycler_sales = binding.recyclerSales
        add_more_item = binding.addMoreItem
        confirm_btn = binding.confirmBtn

        add_more_item.setOnClickListener({
            if (!itelistfragment.isAdded) {
                itelistfragment.show(parentFragmentManager, "show")

            }

        })
        confirm_btn.setOnClickListener({
           getLoginDetails(requireContext())
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

        }
        var num= (0..10).random()
        Log.d("subtotal",subtotal.toString())
        list.add(
            AddSalesResponseModel(
                num.toString(),
                itemName!!,
                quatity!!,
                iftaxIsNotZero.toString(),
                tax!!,
                discount!!,
                price!!
            )
        )
        recycler_sales.layoutManager = LinearLayoutManager(requireContext())
        salesListadapter = SalesListadapter(list)
        recycler_sales.adapter = salesListadapter

        return binding.root
    }

    companion object {

    }

    override fun senddata(
        name: String,
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
            AddSalesResponseModel(
                num.toString(),
                name!!,
                quatity!!,
                iftaxIsNotZero.toString(),
                tax!!,
                discount!!,
                price!!
            )
        )
        recycler_sales.layoutManager = LinearLayoutManager(requireContext())
        salesListadapter = SalesListadapter(list)
        recycler_sales.adapter = salesListadapter

    }


    private fun AddSales(context: Context,MobNo: String) {

        CoroutineScope(Dispatchers.Main).launch {
            try {

                var response = sales(MobNo)

                if (response.isSuccessful) {
                    Toast.makeText(context, "success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()

                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()

            }

        }
    }

    private suspend fun sales(MobNo:String): Response<AddSalesModel> {


        return withContext(Dispatchers.IO) {
            for (i in 0..list.size - 1) {
                total=list.get(i).price.toInt()
            }
            var num= (0..10).random()
            var sales = AddSalesModel(
                MobNo, mobileNo!!, gstNo!!,
                partyName!!, num.toString(),
                total.toString(), "89009", MobNo, list
            )
            BaseClient.getInstance.AddSales(sales)
        }
    }

    fun getLoginDetails(context: Context){

        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()
            AddSales(context,mobNum)
        })
    }
}