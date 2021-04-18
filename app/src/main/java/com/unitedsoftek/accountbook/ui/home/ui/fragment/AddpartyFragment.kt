package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.unitedsoftek.accountbook.Models.StatusMessageModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentAddPartyBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception


class AddpartyFragment : Fragment() {

    lateinit var binding: FragmentAddPartyBinding
    lateinit var name: TextInputEditText
    lateinit var number: TextInputEditText
    lateinit var email: TextInputEditText
    lateinit var sAddress: TextInputEditText
    lateinit var gst: TextInputEditText
    lateinit var saveBtn: Button
    lateinit var progressAdParty: ProgressBar
    lateinit var cardViewContact: CardView
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum: String
    val args: AddpartyFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPartyBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        name = binding.name
        number = binding.number
        sAddress = binding.sAddress
        email = binding.email
        gst = binding.gst
        saveBtn = binding.saveBtn
        progressAdParty = binding.progressAdParty
        cardViewContact = binding.cardViewContact

        var   mobileP= args.CustomerNo
        var  nameP=args.CustomerName
        name.setText(nameP)
        number.setText(mobileP)


        saveBtn.setOnClickListener({
            getLoginDetails()
        })

        cardViewContact.setOnClickListener({
            var action = AddpartyFragmentDirections.actionAddpartyFragmentToContactListFragment()
            findNavController().navigate(action)
        })

        return binding.root
    }


    private suspend fun Addparty(
        userMobile: String,
        partyNma: String,
        contactNumber: String,
        gstNo: String
    ): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.addParties(
                userMobile,
                partyNma,
                contactNumber,
                gstNo
            )
        }
    }

    fun partyAdd(MobNo: String) {
        
        progressAdParty.visibility = View.VISIBLE
        var nameP = name.text.toString()
        var Cnumber = number.text.toString()
        var Cemail = email.text.toString()
        var CsAddress = sAddress.text.toString()
        var cgst = gst.text.toString()

        if (nameP.isEmpty() || Cnumber.isEmpty() || CsAddress.isEmpty()) {
            progressAdParty.visibility = View.GONE
            Toast.makeText(requireContext(), "enter fields ", Toast.LENGTH_LONG).show()
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {

                val response = Addparty(

                    MobNo, nameP, Cnumber, cgst

                )

                if (response.isSuccessful) {

                    progressAdParty.visibility = View.GONE
                    var action=AddpartyFragmentDirections.actionAddpartyFragmentToAllPartyFragment()
                    findNavController().navigate(action)

                } else {

                    progressAdParty.visibility = View.GONE
                    Toast.makeText(requireContext(), "fail", Toast.LENGTH_LONG).show()

                }
            } catch (e: Exception) {

                progressAdParty.visibility = View.GONE
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

            }
        }
    }

    fun getLoginDetails() {
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner, {
            mobNum = it.toString()
            Toast.makeText(requireContext(), mobNum, Toast.LENGTH_LONG).show()
            partyAdd(mobNum)
        })
    }
}