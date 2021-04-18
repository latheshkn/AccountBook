package com.unitedsoftek.accountbook.ui.auth.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.unitedsoftek.accountbook.Models.StatusMessageModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentSignUpBinding
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

class SignUpFragment : Fragment() {

    lateinit var binding: FragmentSignUpBinding
    lateinit var mobileNo: TextView
    lateinit var buttn_signup: Button

    lateinit var constSignUp: ConstraintLayout
    lateinit var progressSignup: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

//        return inflater.inflate(R.layout.fragment_sign_up, container, false)

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        mobileNo = binding.mobileNo
        constSignUp = binding.constSignUp
        progressSignup = binding.progressSignup
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        buttn_signup = binding.buttnSignup

        buttn_signup.setOnClickListener {

            login()

        }

    }

    suspend private fun saveMobile(mobile: String): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.saveMobile(mobile)
        }

    }

    private fun  login(){
        progressSignup.visibility=View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {

            val mobileNumber = mobileNo.text.toString()

            if (mobileNumber.length < 10){
                Toast.makeText(requireContext(),"please enter valid mobile number",Toast.LENGTH_LONG).show()
                progressSignup.visibility=View.INVISIBLE

            }else{
                try {

                    val response = saveMobile(mobileNumber)

                    if (response.isSuccessful) {

                        val data = response.body()
                        if (data?.status.equals("1")) {

                            progressSignup
                          val action = SignUpFragmentDirections.actionSignUpFragmentToOtpFragment(mobileNumber,data?.otp,data!!.message);
                            findNavController().navigate(action)
                            progressSignup.visibility=View.INVISIBLE
                            Toast.makeText(context, data?.message, Toast.LENGTH_SHORT).show()


                        } else {
                            Toast.makeText(context, data?.message, Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        progressSignup.visibility=View.INVISIBLE

                        Toast.makeText(
                            requireContext(),
                            "Response code ${response.code()} and Response Message ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    progressSignup.visibility=View.INVISIBLE

                    Toast.makeText(
                        requireContext(),
                        "Exception Occured ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

}