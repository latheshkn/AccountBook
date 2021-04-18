package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unitedsoftek.accountbook.Models.StatusMessageModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentBusinessPhoneNumberBSBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class PhoneNumberBottomSheetFragment (val send:sendView): BottomSheetDialogFragment() {

    lateinit var binding: FragmentBusinessPhoneNumberBSBinding
    lateinit var save_phone_tv: Button
    lateinit var number_et: EditText
    lateinit var phone_cancel_iv: ImageView
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String
    lateinit var progressBarPhone: ProgressBar
     var one:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val view: View = LayoutInflater.from(requireContext())
//            .inflate(R.layout.fragment_business_phone_number_b_s, container, false)
//
        binding = FragmentBusinessPhoneNumberBSBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)
        save_phone_tv = binding.savePhoneTv
        number_et = binding.numberEt
        phone_cancel_iv = binding.phoneCancelIv
        progressBarPhone = binding.progressBarPhone


        phone_cancel_iv.setOnClickListener({
            send.sendingview(it)
        })
        save_phone_tv.setOnClickListener({
            getLoginDetails()

        })
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private suspend fun updatSecondaryNumber(
        mobile: String,
        sNumber: String
    ): Response<StatusMessageModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.updatePhone(mobile, sNumber)
        }
    }

    private fun updateSNumber(mobNum:String) {
        progressBarPhone.visibility=View.VISIBLE
        val snumber = number_et.text.toString()

        if (snumber.length <10) {
            progressBarPhone.visibility=View.GONE
            Toast.makeText(requireContext(), "enter the secondary number", Toast.LENGTH_LONG).show()
            return
        }
        CoroutineScope(Dispatchers.Main).launch {

            try {
                val response = updatSecondaryNumber(mobNum, snumber)

                if (response.isSuccessful) {
                    one=true
                    progressBarPhone.visibility=View.GONE
                    send.sendSaveClick(one)
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_LONG).show()
                    one=false
                } else {
                    progressBarPhone.visibility=View.GONE

                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                progressBarPhone.visibility=View.GONE

                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

            }

        }
    }

    interface  sendView{

        fun sendingview(view:View)
        fun sendSaveClick(one:Boolean)

    }

    fun getLoginDetails(){
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()
            updateSNumber(mobNum)
        })
    }
}