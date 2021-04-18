package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unitedsoftek.accountbook.Models.StatusMessageModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentDashBoardBinding
import com.unitedsoftek.accountbook.databinding.FragmentUpiPaymentBSBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class UpiBottomSheet(val upilisner:UpiBottomSheetsendView) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentUpiPaymentBSBinding
    lateinit var save_phone_tv: Button
    lateinit var upi_et: EditText
    lateinit var gst_cancel_iv: ImageView
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view:View=LayoutInflater.from(requireContext()).inflate(R.layout.fragment_upi_payment_b_s,container,false)

        binding = FragmentUpiPaymentBSBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        save_phone_tv = binding.savePhoneTv
        upi_et = binding.upiEt
        gst_cancel_iv = binding.gstCancelIv

        save_phone_tv.setOnClickListener({

            getLoginDetails()
        })
        gst_cancel_iv.setOnClickListener({
            upilisner.UpiBottomSheetsendViewsendingview(it)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    private suspend fun updatUpi(
        mobile: String,
        upi: String
    ): Response<StatusMessageModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.updateUpi(mobile, upi)
        }
    }

    private fun updateUpi(MobileNo:String) {

        var upi = upi_et.text.toString()

        if (upi.isEmpty()) {
            Toast.makeText(requireContext(), "enter the upi to update", Toast.LENGTH_LONG).show()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {

            try {

                val response = updatUpi(MobileNo, upi)

                if (response.isSuccessful) {
                    upilisner.UpiBottomSheetsendViewsendSaveClick(one = true)
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

            }

        }
    }

    interface  UpiBottomSheetsendView{
        fun UpiBottomSheetsendViewsendingview(view:View)
        fun UpiBottomSheetsendViewsendSaveClick(one:Boolean)
    }

    fun getLoginDetails(){
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()
            updateUpi(mobNum)
        })
    }

}