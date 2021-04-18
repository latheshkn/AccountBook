package com.unitedsoftek.accountbook

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.navigation.NavigationView
import com.unitedsoftek.accountbook.Utils.NavToolbarController
import com.unitedsoftek.accountbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),NavToolbarController {

    lateinit var binding:ActivityMainBinding
    lateinit var navController :NavController
    lateinit var navigationView:NavigationView
    lateinit var drawer_layout:DrawerLayout
 lateinit var  toolbar:androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar=binding.toolbar
        navigationView=binding.navigationView
        drawer_layout=binding.drawerLayout


        setSupportActionBar(toolbar)
        navController=Navigation.findNavController(this,R.id.fragment2)

        NavigationUI.setupWithNavController(navigationView,navController)
        NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)
        navigationView.itemIconTintList=null



    }

    override fun onSupportNavigateUp(): Boolean {

        return NavigationUI.navigateUp(
            navController,drawer_layout
        )

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.fragment2)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)


    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun showToolbar() {
        toolbar.visibility = VISIBLE
    }

    override fun hideToolbar() {
        toolbar.visibility = GONE
    }


}
