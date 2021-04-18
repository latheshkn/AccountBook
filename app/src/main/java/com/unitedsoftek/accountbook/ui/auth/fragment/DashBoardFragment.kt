package com.unitedsoftek.accountbook.ui.auth.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.unitedsoftek.accountbook.Models.GetBalanceModel
import com.unitedsoftek.accountbook.Models.GetUserDetailModel
import com.unitedsoftek.accountbook.databinding.FragmentDashBoardBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashBoardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashBoardFragment : Fragment() {
    lateinit var binding: FragmentDashBoardBinding
    lateinit var textProfile: TextView
    lateinit var action: NavDirections
    lateinit var btnInvoice: Button
    lateinit var txtDue: TextView
    lateinit var txtSalesAmountNo: TextView
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String
    lateinit var progressTotal:ProgressBar
    lateinit var progressDue:ProgressBar
    lateinit var textProfile:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_dash_board, container, false)
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        textProfile = binding.textProfile
        btnInvoice = binding.btnInvoice
        txtDue = binding.txtAmountNo
        txtSalesAmountNo = binding.txtSalesAmountNo
        progressDue = binding.progressDue
        progressTotal = binding.progressTotal

        getLoginDetails(requireContext())

        textProfile.setOnClickListener({

            action = DashBoardFragmentDirections.actionDashBoardFragment2ToBusinessGstFragment()
            it.findNavController().navigate(action)

        })
        btnInvoice.setOnClickListener({

            action = DashBoardFragmentDirections.actionDashBoardFragment2ToSalesFragment("","","")
            it.findNavController().navigate(action)

        })

        return binding.root
    }

    private suspend fun getBalance(mobileNo:String):Response<GetBalanceModel>{
       return withContext(Dispatchers.IO){
            BaseClient.getInstance.getBalanceAmount(mobileNo)
        }

    }

    private fun getBal(mobileNo:String,context:Context){

        CoroutineScope(Dispatchers.Main).launch {
            var response=getBalance(mobileNo)

            try{

                if (response.isSuccessful){
                    progressDue.visibility=View.GONE
                    progressTotal.visibility=View.GONE
                    txtDue.visibility=View.VISIBLE
                    txtSalesAmountNo.visibility=View.VISIBLE
                    var totalAmont=response.body()?.total_amount!!.toInt()
                    var paidAmount=response.body()?.amount_paid!!.toInt()
                    var dueAmouant=  totalAmont - paidAmount
                    txtDue.text="₹"+dueAmouant.toString()
                    txtSalesAmountNo.text="₹"+response.body()?.total_amount
                }else{
                    progressDue.visibility=View.GONE
                    progressTotal.visibility=View.GONE
                    txtDue.visibility=View.VISIBLE
                    txtSalesAmountNo.visibility=View.VISIBLE
                    Toast.makeText(context,response.message(),Toast.LENGTH_LONG).show()


                }
            }catch (e:Exception){
                progressDue.visibility=View.GONE
                progressTotal.visibility=View.GONE
                txtDue.visibility=View.VISIBLE
                txtSalesAmountNo.visibility=View.VISIBLE
                Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()

            }

        }
    }

    private suspend fun GetUserDeetails(Monbno: String): Response<GetUserDetailModel> {

        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getUserDetails(Monbno)
        }
    }

    private fun getDetail(MobNo: String) {


        CoroutineScope(Dispatchers.Main).launch {
            try {
                var response = GetUserDeetails(MobNo)

                if (response.isSuccessful) {

                    Toast.makeText(requireContext(), "Details"+response.message(), Toast.LENGTH_LONG).show()

                } else {

                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {

                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

            }
        }
    }

    fun getLoginDetails(context: Context){

        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()
            getBal(mobNum,context)
            getDetail(mobNum)
        })

    }
}