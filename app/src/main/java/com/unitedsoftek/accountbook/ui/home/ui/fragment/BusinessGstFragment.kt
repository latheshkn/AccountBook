package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.asLiveData
import com.unitedsoftek.accountbook.Models.GetUserDetailModel
import com.unitedsoftek.accountbook.Models.StatusMessageModel
import com.unitedsoftek.accountbook.databinding.FragmentBusinessGstBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BusinessGstFragment : Fragment(), PhoneNumberBottomSheetFragment.sendView,
    GstBottomSheet.GstBottomSheetsendView, UpiBottomSheet.UpiBottomSheetsendView,
    BankBottomSheetFragment.BankBottomSheetsendView,
    AdressBottomSheetFragment.AddressBottomSheetsendView {

    lateinit var binding: FragmentBusinessGstBinding
    lateinit var busNumLayout: ConstraintLayout
    lateinit var gstNumLayout: ConstraintLayout
    lateinit var upiLayout: ConstraintLayout
    lateinit var busBankLayout: ConstraintLayout
    lateinit var busAddressLayout: ConstraintLayout
    lateinit var btnUpd: Button
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum: String
    lateinit var busNum: TextView
    lateinit var gstNum: TextView
    lateinit var busAddress: TextView
    lateinit var upiId: TextView
    lateinit var busBankDetail: TextView
    lateinit var progressProfile: ProgressBar
    lateinit var editBName: EditText


    val adressBottomSheetFragment = AdressBottomSheetFragment(this)

    //     var  bottomSheetFragment=PhoneNumberBottomSheetFragment(this)

    val bottomSheet by lazy {
        PhoneNumberBottomSheetFragment(this)
    }
    val gstBottomSheet = GstBottomSheet(this)
    val bottomupifragment = UpiBottomSheet(this)
    val bankBottomSheetFragment = BankBottomSheetFragment(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBusinessGstBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        busNumLayout = binding.busNumLayout
        gstNumLayout = binding.gstNumLayout
        upiLayout = binding.upiLayout
        busBankLayout = binding.busBankLayout
        busAddressLayout = binding.busAddressLayout
        editBName = binding.editBName
        btnUpd = binding.btnUpd
        busNum = binding.busNum
        gstNum = binding.gstNum
        busAddress = binding.busAddress
        upiId = binding.upiId
        busBankDetail = binding.busBankDetail
        progressProfile = binding.progressProfile


//        bottomSheetFragment.phone_cancel_iv.setOnClickListener({
//            bankBottomSheetFragment.dismiss()
//        })
        getLoginDetails()

        busNumLayout.setOnClickListener({

            if (!bottomSheet.isAdded) {
                bottomSheet.isCancelable = false
                bottomSheet.show(parentFragmentManager, "BottomSheetDialog")
            }
        })

        gstNumLayout.setOnClickListener({
            if (!bottomSheet.isAdded) {
                gstBottomSheet.isCancelable = false
                gstBottomSheet.show(parentFragmentManager, "BottomSheetDialog")

            }
        })

        upiLayout.setOnClickListener({
            if (!bottomSheet.isAdded) {
                bottomupifragment.isCancelable = false
                bottomupifragment.show(parentFragmentManager, "BottomSheetDialog")
            }
        })
        busBankLayout.setOnClickListener({
            if (!bottomSheet.isAdded) {
                bankBottomSheetFragment.isCancelable = false
                bankBottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")
            }
        })

        busAddressLayout.setOnClickListener({
            if (!bottomSheet.isAdded) {
                bankBottomSheetFragment.isCancelable = false
                adressBottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")
            }
        })

        editBName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnUpd.visibility = View.VISIBLE
                if (s?.length!! <= 0) {
                    btnUpd.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        btnUpd.setOnClickListener({
            /*update business name*/
            updatebusinessName(mobNum)
            editBName.text.clear()
        })


        return binding.root
    }

    private suspend fun updateBname(mobiel: String, bname: String): Response<StatusMessageModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.updateBname(mobiel, bname)
        }
    }

    private fun updatebusinessName(MobNo: String) {

        var name = editBName.text.toString()

        if (name.isEmpty()) {
            editBName.error = "required"
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {

                val response = updateBname(MobNo, name)

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()

                }


            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }


        }
    }

    override fun sendingview(view: View) {
        bottomSheet.dismiss()

    }

    override fun sendSaveClick(one: Boolean) {
        if (one) {
            bottomSheet.dismiss()
            getLoginDetails()
        }

    }

    override fun GstBottomSheetsendViewsendingview(view: View) {

        gstBottomSheet.dismiss()
        getLoginDetails()
    }

    override fun GstBottomSheetsendViewsendSaveClick(one: Boolean) {
        gstBottomSheet.dismiss()
        getLoginDetails()
    }

    override fun UpiBottomSheetsendViewsendingview(view: View) {
        bottomupifragment.dismiss()
        getLoginDetails()
    }

    override fun UpiBottomSheetsendViewsendSaveClick(one: Boolean) {
        bottomupifragment.dismiss()
        getLoginDetails()
    }

    override fun BankBottomSheetsendViewsendingview(view: View) {
        bankBottomSheetFragment.dismiss()
        getLoginDetails()

    }

    override fun BankBottomSheetsendViewsendSaveClick(one: Boolean) {
        bankBottomSheetFragment.dismiss()
        getLoginDetails()
    }

    override fun AddressBottomSheetsendViewsendingview(view: View) {
        adressBottomSheetFragment.dismiss()
        getLoginDetails()
    }

    override fun AddressBottomSheetsendViewsendSaveClick(one: Boolean) {
        adressBottomSheetFragment.dismiss()
        getLoginDetails()
    }

    fun getLoginDetails() {

        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner, {
            mobNum = it.toString()

            getDetail(mobNum)
        })
    }

    private suspend fun GetUserDeetails(Monbno: String): Response<GetUserDetailModel> {

        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getUserDetails(Monbno)
        }
    }

    private fun getDetail(MobNo: String) {
        progressProfile.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.Main).launch {
            try {
                var response = GetUserDeetails(MobNo)

                if (response.isSuccessful) {
                    progressProfile.visibility = View.GONE
                    busBankDetail.text = response.body()?.message?.get(0)?.bank_name
                    busNum.text = response.body()?.message?.get(0)?.secondary_mobile
                    gstNum.text = response.body()?.message?.get(0)?.gst
                    busAddress.text = response.body()?.message?.get(0)?.business_address
                    upiId.text = response.body()?.message?.get(0)?.upi_id
                    editBName.setText(response.body()?.message?.get(0)?.business_name)
                    btnUpd.visibility=View.GONE
                } else {
                    progressProfile.visibility = View.GONE
                    busBankDetail.text = ""
                    busNum.text = ""
                    gstNum.text = ""
                    busAddress.text = ""
                    upiId.text = ""
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()

                }

            } catch (e: Exception) {
                progressProfile.visibility = View.GONE
                busBankDetail.text = ""
                busNum.text = ""
                gstNum.text = ""
                busAddress.text = ""
                upiId.text = ""
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

            }
        }
    }


}