package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unitedsoftek.accountbook.Models.StatusMessageModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentBussinessAddressBSBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.*
import retrofit2.Response

class AdressBottomSheetFragment(val addressBottomSheetsendView:AddressBottomSheetsendView):BottomSheetDialogFragment() {
    lateinit var binding:FragmentBussinessAddressBSBinding
    lateinit var ba_cancel_iv:ImageView
    lateinit var save_address_tv:Button
    lateinit var number_et:EditText
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentBussinessAddressBSBinding.inflate(inflater,container,false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        ba_cancel_iv=binding.baCancelIv
        save_address_tv=binding.saveAddressTv
        number_et=binding.numberEt

        ba_cancel_iv.setOnClickListener({
            addressBottomSheetsendView.AddressBottomSheetsendViewsendingview(it)
        })
        save_address_tv.setOnClickListener({
            getLoginDetails()
        })

        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    suspend fun changeAddress(bisinessAdd:String,mobileNo:String):Response<StatusMessageModel>{
        return withContext(Dispatchers.IO){

            BaseClient.getInstance.updateBisinessAddress(mobileNo,bisinessAdd)
        }
    }

    private fun changeBisinessAddress(mobileNo:String){

        val address=number_et.text.toString()
        if (address.isEmpty()){
            Toast.makeText(requireContext(),"enter the address to update",Toast.LENGTH_LONG).show()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {


                val response=changeAddress(address,mobileNo)


                if (response.isSuccessful){
                    addressBottomSheetsendView.AddressBottomSheetsendViewsendSaveClick(true)
                    Toast.makeText(requireContext(),"success",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(),"fail",Toast.LENGTH_LONG).show()
                }

            }catch (e:Exception){
                Toast.makeText(requireContext(),e.message,Toast.LENGTH_LONG).show()
            }

        }
    }


    interface AddressBottomSheetsendView {
        fun AddressBottomSheetsendViewsendingview(view: View)
        fun AddressBottomSheetsendViewsendSaveClick(one: Boolean)
    }

    fun getLoginDetails(){
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()
            changeBisinessAddress(mobNum)
        })
    }
}