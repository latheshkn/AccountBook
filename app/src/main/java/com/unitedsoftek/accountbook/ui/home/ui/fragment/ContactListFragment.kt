package com.unitedsoftek.accountbook.ui.home.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.unitedsoftek.accountbook.MainActivity
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.databinding.FragmentContactListBinding


class ContactListFragment : Fragment() {

    lateinit var listView: ListView
    lateinit var searchView: SearchView

    var colms = listOf<String>(

        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone._ID

    ).toTypedArray()
    lateinit var binding:FragmentContactListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentContactListBinding.inflate(inflater,container,false)

        listView=binding.listView
        searchView=binding.searchView
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_CONTACTS), 11)

        } else {

            readContact()
        }
        return binding.root
    }

    private fun readContact() {

// A list of column names representing the data to bind to the UI.  Can be null
//     if the cursor is not available yet.

        var from = listOf<String>(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ).toTypedArray()


//this is to bind the from data into the ui
        var to = intArrayOf(android.R.id.text1, android.R.id.text2)

//        content resolver is like select query in sqlite

//        content resolver returs bak a cursor cursor is nothing but a
//        representatin of a table through which we want to itterate over

        var rs = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,/*its the table name in the local database*/
            colms,/*colomns which are to be selected from the local database*/
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

//it will take 6 parameters as input contex,layout,
        var adapter = SimpleCursorAdapter(requireContext(), android.R.layout.simple_list_item_2, rs, from, to, 0)
        listView.adapter = adapter
        listView.setOnItemClickListener { parent,
                                          view,
                                          position, id ->

            var action=ContactListFragmentDirections.actionContactListFragmentToAddpartyFragment(
                rs?.getString(0),rs?.getString(1))
            findNavController().navigate(action)

        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                var rs = context?.contentResolver?.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    colms,
                    "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE?",
                    Array(1) { "%$newText%" },
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                )
                listView.setOnItemClickListener { parent,
                                                  view,
                                                  position, id ->

                    var action=ContactListFragmentDirections.actionContactListFragmentToAddpartyFragment(
                        rs?.getString(0),rs?.getString(1))
                    findNavController().navigate(action)
                }
                adapter.changeCursor(rs)

                return false
            }

        })

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readContact()
        }
    }

}