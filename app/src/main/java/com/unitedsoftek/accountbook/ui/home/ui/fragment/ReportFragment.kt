package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unitedsoftek.accountbook.Models.StatusMessageModel
import com.unitedsoftek.accountbook.Models.getInvoiceReportModel.InvoiceReportModelResponse
import com.unitedsoftek.accountbook.Models.getInvoiceReportModel.invoiceReportModel
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore
import com.unitedsoftek.accountbook.network.BaseClient
import com.unitedsoftek.accountbook.ui.home.ui.adapters.SalesReportAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class ReportFragment : Fragment(), SalesReportAdapter.sendClick {
    lateinit var reportRecycler: RecyclerView
    lateinit var salesReportAdapter: SalesReportAdapter
    lateinit var searhEdt: SearchView
    lateinit var progressReportSales: ProgressBar
    lateinit var constraint_top: ConstraintLayout
    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum: String
    var list = ArrayList<InvoiceReportModelResponse>()

    val displaylist=ArrayList<InvoiceReportModelResponse>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.report, container, false)
        userLoginPrefrence = UserLoginPrefrence(requireContext().datastore)
        reportRecycler = view.findViewById(R.id.reportRecycler)
        searhEdt = view.findViewById(R.id.searhEdt)
        progressReportSales = view.findViewById(R.id.progressReportSales)
        constraint_top = view.findViewById(R.id.constraint_top)
        reportRecycler.layoutManager = LinearLayoutManager(requireContext())
        setHasOptionsMenu(true)

        getLoginDetails(requireContext())


        return view
    }


    private suspend fun getInvoice(MobNom: String): Response<invoiceReportModel> {

        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getInvoicereport(MobNom)
        }
    }

    private fun invoiceReport(context: Context, MobNom: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                var response = getInvoice(MobNom)

                if (response.isSuccessful) {

                    if (response.body()!!.status.equals("1")) {

                        progressReportSales.visibility = View.GONE
                        constraint_top.visibility = View.VISIBLE
                        list = response.body()?.invoice_list!!
                        displaylist.addAll(list)
                        salesReportAdapter =
                            SalesReportAdapter(displaylist, this@ReportFragment, requireContext())

                        reportRecycler.adapter = salesReportAdapter
                        salesReportAdapter.notifyDataSetChanged()
                        Toast.makeText(context, "success", Toast.LENGTH_LONG).show()

                    } else {

                        progressReportSales.visibility = View.GONE
                        constraint_top.visibility = View.GONE
                        reportRecycler.visibility = View.GONE
                        setHasOptionsMenu(false)
                        Toast.makeText(context, "No data", Toast.LENGTH_LONG).show()
                    }

                } else {
                    progressReportSales.visibility = View.GONE
                    constraint_top.visibility = View.VISIBLE
                    reportRecycler.visibility = View.GONE

                    Toast.makeText(context, "Something went", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                progressReportSales.visibility = View.GONE
                constraint_top.visibility = View.VISIBLE

                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }

        }

    }

    fun getLoginDetails(context: Context) {
        userLoginPrefrence.mobileFlow.asLiveData().observe(viewLifecycleOwner, {
            mobNum = it.toString()
            invoiceReport(context, mobNum)
        })
    }

    override fun senditemClick(str: String, position: Int) {
        salesDelete(str)
        Toast.makeText(requireContext(), "delete", Toast.LENGTH_LONG).show()
    }

    private suspend fun delteSales(invoiceNo: String): Response<StatusMessageModel> {

        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.deleteSale(invoiceNo)

        }
    }

    private fun salesDelete(invoiceNo: String) {

        CoroutineScope(Dispatchers.Main).launch {
            var response = delteSales(invoiceNo)
            if (response.isSuccessful) {
                getLoginDetails(requireContext())

                Toast.makeText(requireContext(), "success", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "fail", Toast.LENGTH_LONG).show()
            }

        }
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
                        if (it.invoice_no.toLowerCase(Locale.getDefault()).contains(search)){

                            displaylist.add(it)
                        }
                        reportRecycler.adapter?.notifyDataSetChanged()
                    }
                }else{
                    displaylist.clear()
                    displaylist.addAll(list)
                    reportRecycler.adapter?.notifyDataSetChanged()
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