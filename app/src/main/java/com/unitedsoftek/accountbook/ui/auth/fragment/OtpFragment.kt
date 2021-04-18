package com.unitedsoftek.accountbook.ui.auth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.asLiveData
import androidx.navigation.NavArgs
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.unitedsoftek.accountbook.Models.LoginResposeModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentOtpBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class OtpFragment : Fragment() {

    val args: OtpFragmentArgs by navArgs()
    lateinit var submitOtp: Button
    lateinit var binding: FragmentOtpBinding
    lateinit var otpEdt: EditText
    lateinit var progressOtp: ProgressBar
    lateinit var mobile: String
    lateinit var otpNumTxt: TextView
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String
    lateinit var userExist:String
    lateinit var otp: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentOtpBinding.inflate(inflater, container, false)

        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        getLoginDetails()

        submitOtp = binding.submitOtp
        progressOtp = binding.progressOtp
        otpEdt = binding.otpEdt
        otpNumTxt = binding.otpNumTxt

        mobile = args.mobileNum
        userExist = args.UserExistsOrNot
        otp=args.otp!!
        otpNumTxt.setText(mobile)
        otpEdt.setText(otp)
        submitOtp.setOnClickListener {

            progressOtp.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.Main).launch {

                val mobileotp = otpEdt.text.toString()

                if (mobileotp.length < 5) {
                    Toast.makeText(
                        requireContext(),
                        "please enter valid mobile number",
                        Toast.LENGTH_LONG
                    ).show()
                    progressOtp.visibility = View.INVISIBLE

                } else {
//                    try {
                        val response = login(mobile, mobileotp)

                        if (response.isSuccessful) {
                            val data = response.body()?.data
             /*               Toast.makeText(context, "mobile"+data?.mobile, Toast.LENGTH_LONG)
                                .show()*/
                            userLoginPrefrence.saveUserDetails(
                                data!!.mobile
                            )
                          if (userExist.equals("otp genereted")){
                              val action =
                                  OtpFragmentDirections.actionOtpFragmentToMainActivity()
                              it.findNavController().navigate(action)
                          }else{
                              val action =
                                  OtpFragmentDirections.actionOtpFragmentToCompanyRegistrationFragment2(mobile)
                              it.findNavController().navigate(action)
                          }
                            Toast.makeText(context, "res"+data?.mobile, Toast.LENGTH_LONG)
                                .show()
                            progressOtp.visibility = View.INVISIBLE
                        } else {

                            progressOtp.visibility = View.INVISIBLE
                            Toast.makeText(context, "fail"+response.body()?.message, Toast.LENGTH_LONG)
                                .show()

                        }

//                    } catch (e: Exception) {
//                        Toast.makeText(context, "error"+e.message, Toast.LENGTH_LONG).show()
//                        progressOtp.visibility = View.INVISIBLE
//                    }
                }

            }


        }
        return binding.root


    }


    suspend fun login(mobile: String, otp: String): Response<LoginResposeModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.lolgin(mobile, otp)
        }
    }

    fun getLoginDetails(){
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()

        })
    }



}