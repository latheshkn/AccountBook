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
import com.unitedsoftek.accountbook.databinding.FragmentAllPartyBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class GstBottomSheet(val gsstlisner:GstBottomSheetsendView) : BottomSheetDialogFragment() {
    lateinit var gst_cancel_iv:ImageView
    lateinit var number_et:EditText
    lateinit var save_gst_tv: Button
    lateinit var progrssGst: ProgressBar
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_gst_number_b_s, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        gst_cancel_iv=view.findViewById(R.id.gst_cancel_iv)
        number_et=view.findViewById(R.id.number_et)
        save_gst_tv=view.findViewById(R.id.save_gst_tv)
        progrssGst=view.findViewById(R.id.progrssGst)

        gst_cancel_iv.setOnClickListener({

            gsstlisner.GstBottomSheetsendViewsendingview(it)

        })
        save_gst_tv.setOnClickListener({

            getLoginDetails()
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    private suspend fun updatSecondaryNumber(
        mobile: String,
        gst: String
    ): Response<StatusMessageModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.updateGst(mobile,gst)
        }
    }

    private fun updateSNumber(mobNo:String) {
        progrssGst.visibility=View.VISIBLE
        val gst=number_et.text.toString()
        if (gst.isEmpty()){
            progrssGst.visibility=View.GONE
            Toast.makeText(requireContext(), "please enter the fields", Toast.LENGTH_LONG).show()
            return
        }
        CoroutineScope(Dispatchers.Main).launch {

            try {
                val response = updatSecondaryNumber(mobNo, gst)

                if (response.isSuccessful) {

                    progrssGst.visibility=View.GONE
                    gsstlisner.GstBottomSheetsendViewsendSaveClick(one=true)
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_LONG).show()
                } else {
                    progrssGst.visibility=View.GONE
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                }
            }catch (e:Exception){

                progrssGst.visibility=View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }

        }
    }

    interface  GstBottomSheetsendView{
        fun GstBottomSheetsendViewsendingview(view:View)
        fun GstBottomSheetsendViewsendSaveClick(one:Boolean)
    }

    fun getLoginDetails(){
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()
            updateSNumber(mobNum)
        })
    }
}