package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.content.Context
import android.os.Binder
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unitedsoftek.accountbook.Models.StatusMessageModel
import com.unitedsoftek.accountbook.Models.getInvoiceReportModel.InvoiceReportModelResponse
import com.unitedsoftek.accountbook.Models.getQuotationReportModel.QuotationReportModelResponse
import com.unitedsoftek.accountbook.Models.getQuotationReportModel.QutatiionReportModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentQuotationReportBinding
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import com.unitedsoftek.accountbook.ui.home.ui.adapters.QuotationReportAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class QuotationReportFragment : Fragment() ,QuotationReportAdapter.sendClick{
    lateinit var binding: FragmentQuotationReportBinding
    lateinit var QuotationreportRecycler: RecyclerView
    lateinit var constraint_top: ConstraintLayout
    lateinit var progressReportQuotation: ProgressBar
    lateinit var quotationReportAdapter:QuotationReportAdapter
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String
    var Displaylist = ArrayList<QuotationReportModelResponse>()
    var list = ArrayList<QuotationReportModelResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuotationReportBinding.inflate(inflater, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)

        QuotationreportRecycler = binding.QuotationreportRecycler
        progressReportQuotation = binding.progressReportQuotation
        constraint_top = binding.constraintTop
        setHasOptionsMenu(true)
        QuotationreportRecycler.layoutManager = LinearLayoutManager(requireContext())

        getLoginDetails(requireContext())
        return binding.root
    }

    companion object {

    }

    private suspend fun getQuotationReport(Mob:String): Response<QutatiionReportModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getQuotationreport(Mob)

        }
    }

    private fun QuotationReport(context: Context,Mob: String) {

        CoroutineScope(Dispatchers.Main).launch {

            try {

                var response = getQuotationReport(Mob)

                if (response.isSuccessful) {
                    if (response.body()!!.status.equals("1")){

                        progressReportQuotation.visibility = View.GONE
                        constraint_top.visibility = View.VISIBLE
                        list=response.body()?.quotation_list!!
                        Displaylist=list
                        quotationReportAdapter= QuotationReportAdapter(Displaylist,this@QuotationReportFragment)
                        QuotationreportRecycler.adapter=quotationReportAdapter
                        Toast.makeText(context, "success", Toast.LENGTH_LONG).show()
                    }else{
                        progressReportQuotation.visibility = View.GONE
                        constraint_top.visibility = View.GONE
                        QuotationreportRecycler.visibility=View.GONE
                        setHasOptionsMenu(false)
                        Toast.makeText(context, "No data", Toast.LENGTH_LONG).show()

                    }



                } else {
                    progressReportQuotation.visibility = View.GONE
                    constraint_top.visibility = View.GONE
                    QuotationreportRecycler.visibility=View.GONE
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                progressReportQuotation.visibility = View.GONE
                constraint_top.visibility = View.VISIBLE
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }

        }
    }

    fun getLoginDetails(context: Context){

        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner,{
            mobNum = it.toString()
            QuotationReport(context,mobNum)
        })
    }

    override fun senditemClick(str: String, position: Int) {
        Toast.makeText(requireContext(),"deleted",Toast.LENGTH_LONG).show()
        deletQnt(str)
    }

    private suspend fun deleteQutation(qntNo:String):Response<StatusMessageModel>{

     return  withContext(Dispatchers.IO){
           BaseClient.getInstance.deleteQuotation(qntNo)
       }
    }

    private fun deletQnt(qntNo:String){
        CoroutineScope(Dispatchers.Main).launch {
            var response=deleteQutation(qntNo)
            quotationReportAdapter.notifyDataSetChanged()
            QuotationReport(requireContext(),mobNum)

            if (response.isSuccessful){
                Toast.makeText(requireContext(),"success",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(),"fail",Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)
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
                    Displaylist.clear()
                    val search=newText.toLowerCase(Locale.getDefault())

                    list.forEach{
                        if (it.quotation_no.toLowerCase(Locale.getDefault()).contains(search)){

                            Displaylist.add(it)
                        }
                        QuotationreportRecycler.adapter?.notifyDataSetChanged()
                    }
                }else{
                    Displaylist.clear()
                    Displaylist.addAll(list)
                    QuotationreportRecycler.adapter?.notifyDataSetChanged()
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