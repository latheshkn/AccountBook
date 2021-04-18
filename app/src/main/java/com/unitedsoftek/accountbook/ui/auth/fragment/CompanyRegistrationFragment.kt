package com.unitedsoftek.accountbook.ui.auth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavArgs
import androidx.navigation.NavArgument
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.unitedsoftek.accountbook.Models.LoginResposeModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentCompanyRegistrationBinding
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.*
import retrofit2.Response
import androidx.navigation.fragment.navArgs


class CompanyRegistrationFragment : Fragment() {

    val args: CompanyRegistrationFragmentArgs by navArgs()
    lateinit var binding: FragmentCompanyRegistrationBinding
    lateinit var saveBusDetailBtn: Button
    lateinit var action: NavDirections
    lateinit var yourNameEt: EditText
    lateinit var yourBusinessNameEt: EditText
    lateinit var gstNoEt: EditText
    lateinit var mobile: String
    lateinit var selectGst: RadioGroup
    lateinit var gstNo: RadioButton
    lateinit var noGst: RadioButton
    var selected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_company_registration, container, false)
        binding = FragmentCompanyRegistrationBinding.inflate(inflater, container, false)

        saveBusDetailBtn = binding.saveBusDetailBtn
        yourNameEt = binding.yourNameEt
        yourBusinessNameEt = binding.yourBusinessNameEt
        gstNoEt = binding.gstNoEt
        selectGst = binding.selectGst
        gstNo = binding.gstNo
        noGst = binding.noGst

        if (args != null) {
            mobile = args.mobile

        }
        if (!selected) {
        } else {
            gstNoEt.visibility = View.INVISIBLE

        }





        saveBusDetailBtn.setOnClickListener({

            register()

        })



        selectGst.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {

                R.id.noGst -> gstNoEt.visibility = View.GONE

                R.id.gstNo -> gstNoEt.visibility = View.VISIBLE

            }

        }

        return binding.root


    }

    private suspend fun companyRegister(
        mobile: String,
        userName: String,
        businessName: String,
        gst: String
    ): Response<LoginResposeModel> {

        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.registerCompany(mobile, userName, businessName, gst)

        }
    }

    private fun register() {

        val name = yourNameEt.text.toString()
        val businessName = yourBusinessNameEt.text.toString()
        val gstNo = gstNoEt.text.toString()

        if(gstNoEt.visibility==View.VISIBLE){
            if (gstNo.isEmpty()){
                Toast.makeText(requireContext(), "enter gst number", Toast.LENGTH_LONG).show()
                return
            }
        }

        if (name.isEmpty() || businessName.isEmpty()) {
            Toast.makeText(requireContext(), "fill the fields", Toast.LENGTH_LONG).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                val response = companyRegister(mobile, name, businessName, gstNo)

                if (response.isSuccessful && response.body()?.status.equals("1")) {
                    action =
                        CompanyRegistrationFragmentDirections.actionCompanyRegistrationFragmentToMainActivity()
                    findNavController().navigate(action)
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_LONG).show()
                } else {

                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG)
                        .show()

                }
            }
        }

    }


}