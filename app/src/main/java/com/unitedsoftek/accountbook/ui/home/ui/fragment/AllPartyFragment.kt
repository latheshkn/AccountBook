package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unitedsoftek.accountbook.MainActivity
import com.unitedsoftek.accountbook.Models.StatusMessageModel
import com.unitedsoftek.accountbook.Models.getCustomerList.GetCustomerListModel
import com.unitedsoftek.accountbook.Models.getCustomerList.GetCustomerListModelResponse
import com.unitedsoftek.accountbook.Models.getInvoiceReportModel.InvoiceReportModelResponse
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentAllPartyBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import com.unitedsoftek.accountbook.ui.home.ui.adapters.ContactListadapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*


class AllPartyFragment : Fragment(), ContactListadapter.sendClick {
    lateinit var binding: FragmentAllPartyBinding
    lateinit var fab: FloatingActionButton
    lateinit var action: NavDirections
    lateinit var addparty: Button
    lateinit var add_rv: RecyclerView
    lateinit var constraintNew: ConstraintLayout
    lateinit var recycler_header: ConstraintLayout
    lateinit var contactlistAdapter: ContactListadapter
    lateinit var progressNewacon: ProgressBar
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum: String
    var list = ArrayList<GetCustomerListModelResponse>()
    var displaylist=ArrayList<GetCustomerListModelResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        getActivity()?.getWindow()?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

//        val view= inflater.inflate(R.layout.fragment_all_party, container, false)
        (activity as MainActivity?)?.hideToolbar()
        binding = FragmentAllPartyBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        fab = binding.fab
        addparty = binding.addparty
        add_rv = binding.addRv
        constraintNew = binding.constraintNew
        recycler_header = binding.recyclerHeader
        progressNewacon = binding.progressNewacon

        setHasOptionsMenu(true)
        getLoginDetails(requireContext())


        addparty.setOnClickListener(

            {
                action =
                    AllPartyFragmentDirections.actionAllPartyFragmentToAddpartyFragment(null, null)
                it.findNavController().navigate(action)
            }
        )
        fab.setOnClickListener({
            action = AllPartyFragmentDirections.actionAllPartyFragmentToAddpartyFragment(null, null)
            it.findNavController().navigate(action)

        })
        return binding.root;

    }


    override fun onResume() {
        super.onResume()

        (activity as MainActivity?)?.hideToolbar()

    }

    override fun onPause() {
        super.onPause()

        (activity as MainActivity?)?.showToolbar()
    }

    private suspend fun getCustomerList(Mob: String): Response<GetCustomerListModel> {


        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getCustomerList(Mob)
        }
    }

    private fun GetList(context: Context, Mob: String) {

        progressNewacon.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            try {
                var response = getCustomerList(Mob)

                if (response.isSuccessful) {

                    if (response.body()?.status.equals("1")) {
                        add_rv.layoutManager = LinearLayoutManager(context)
                        list= response.body()?.customer_list!!
                        displaylist=list
                        contactlistAdapter = ContactListadapter(
                            displaylist,
                            requireContext(),
                            this@AllPartyFragment
                        )
                        add_rv.adapter = contactlistAdapter
                        constraintNew.visibility = View.GONE
                        recycler_header.visibility = View.VISIBLE
                        progressNewacon.visibility = View.GONE
                        fab.visibility = View.VISIBLE
                    } else {
                        progressNewacon.visibility = View.GONE
                        constraintNew.visibility = View.VISIBLE
                        add_rv.visibility = View.GONE
                        recycler_header.visibility = View.GONE
                        fab.visibility = View.GONE
                        Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()

                    }


                } else {
                    progressNewacon.visibility = View.GONE
                    constraintNew.visibility = View.VISIBLE

                }
            } catch (e: Exception) {
                progressNewacon.visibility = View.GONE
                constraintNew.visibility = View.VISIBLE
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()

            }
        }
    }

    fun getLoginDetails(context: Context) {
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner, {
            mobNum = it.toString()
            GetList(context, mobNum)
        })
    }


    private suspend fun deleteParty(customer: String): Response<StatusMessageModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.deleteParty(mobNum, customer)


        }
    }

    private fun delete(customer: String) {

        CoroutineScope(Dispatchers.Main).launch {
            var response = deleteParty(customer)

            if (response.isSuccessful) {
                contactlistAdapter.notifyDataSetChanged()
                getLoginDetails(requireContext())
            } else {
                Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun senditemClick(str: String, position: Int) {
        delete(str)

    }

    override fun sendDialogClick(
        str: String,
        Cname: String,
        Cmobile: String,
        Cgst: String,
        position: Int
    ) {
        showAlert(Cname,Cmobile,Cgst)
    }


    fun showAlert(Cname: String,Cmobile: String,Cgst: String) {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.alert_dialog_layout)
        var btn_sales = dialog.findViewById<Button>(R.id.btn_sales)
        var btn_qnt = dialog.findViewById<Button>(R.id.btn_qtn)

        btn_sales.setOnClickListener({

            var action = AllPartyFragmentDirections.actionAllPartyFragmentToSalesFragment2(Cname,Cmobile,Cgst)
            findNavController().navigate(
                action
            )
            dialog.dismiss()
        })

        btn_qnt.setOnClickListener({

            var action = AllPartyFragmentDirections.actionAllPartyFragmentToQuotionFragment(Cname,Cmobile,Cgst)
            findNavController().navigate(
                action
            )

            dialog.dismiss()
        })
        dialog.show()

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        var item: MenuItem = menu.findItem(R.id.action_search)

        var search: androidx.appcompat.widget.SearchView =
            item.actionView as androidx.appcompat.widget.SearchView
        search.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()){
                    displaylist.clear()
                    val search=newText.toLowerCase(Locale.getDefault())

                    list.forEach{
                        if (it.customer_name.toLowerCase(Locale.getDefault()).contains(search)){

                            displaylist.add(it)
                        }
                        add_rv.adapter?.notifyDataSetChanged()
                    }
                }else{
                    displaylist.clear()
                    displaylist.addAll(list)
                    add_rv.adapter?.notifyDataSetChanged()
                }

                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}