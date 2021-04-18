package com.unitedsoftek.accountbook.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.unitedsoftek.accountbook.MainActivity
import com.unitedsoftek.accountbook.R
import com.unitedsoftek.accountbook.datastore.UserLoginPrefrence
import com.unitedsoftek.accountbook.datastore.datastore

class SplashScreenActivity : AppCompatActivity() {

    lateinit var userLoginPrefrence: UserLoginPrefrence
    lateinit var mobNum:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        userLoginPrefrence = UserLoginPrefrence(datastore)

        getLoginDetails()
    }

    private fun getLoginDetails() {
        userLoginPrefrence.mobileFlow.asLiveData().observe(this, { number ->
            mobNum = number.toString()
            if (!mobNum.contains("null") && mobNum!=null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }
}