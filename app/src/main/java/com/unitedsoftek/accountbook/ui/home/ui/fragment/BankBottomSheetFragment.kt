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
import com.unitedsoftek.accountbook.databinding.FragmentBankDetailsBSBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BankBottomSheetFragment(val banklisner: BankBottomSheetsendView) :
    BottomSheetDialogFragment() {
    lateinit var binding: FragmentBankDetailsBSBinding
    lateinit var account_num_et: EditText
    lateinit var code_et: EditText
    lateinit var branch_et: EditText
    lateinit var holder_name_et: EditText
    lateinit var save_phone_tv: Button
    lateinit var bank_cancel_iv: ImageView
    lateinit var progressBank: ProgressBar
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBankDetailsBSBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        account_num_et = binding.accountNumEt
        code_et = binding.codeEt
        branch_et = binding.branchEt
        holder_name_et = binding.holderNameEt
        save_phone_tv = binding.savePhoneTv
        bank_cancel_iv = binding.bankCancelIv
        progressBank = binding.progressBank

        save_phone_tv.setOnClickListener({
            getLoginDetails()
        })

        bank_cancel_iv.setOnClickListener({
            banklisner.BankBottomSheetsendViewsendingview(it)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private suspend fun updatBank(
        mobile: String,
        account_no: String,
        ifsc_code: String,
        bank_name: String,
        holder_name: String,
    ): Response<StatusMessageModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.updateBankDetail(
                mobile,
                account_no,
                ifsc_code,
                bank_name,
                holder_name
            )
        }
    }

    private fun updateBankDetail(MobileNo:String) {
        progressBank.visibility=View.VISIBLE
        var accntNo = account_num_et.text.toString()
        var ifcNom = code_et.text.toString()
        var brantchNamr = branch_et.text.toString()
        var holderName = holder_name_et.text.toString()

        if (accntNo.isEmpty() || ifcNom.isEmpty() || brantchNamr.isEmpty() || holderName.isEmpty()) {
            progressBank.visibility=View.GONE

            Toast.makeText(requireContext(), "enter all detail", Toast.LENGTH_LONG).show()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {

            try {
                val response = updatBank(MobileNo, accntNo, ifcNom, brantchNamr, holderName)

                if (response.isSuccessful) {
                    progressBank.visibility=View.GONE
                    banklisner.BankBottomSheetsendViewsendSaveClick(one = true)
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_LONG).show()
                } else {
                    progressBank.visibility=View.GONE
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                progressBank.visibility=View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

            }
        }
    }

    interface BankBottomSheetsendView {
        fun BankBottomSheetsendViewsendingview(view: View)
        fun BankBottomSheetsendViewsendSaveClick(one: Boolean)
    }

    fun getLoginDetails(){
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()
            updateBankDetail(mobNum)
        })
    }

}