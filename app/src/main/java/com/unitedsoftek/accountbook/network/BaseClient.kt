package com.unitedsoftek.accountbook.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseClient {

private val BaseUrl:String="http://ms-tps.com/invoice/public/api/"
//private val BaseUrl:String="http://192.168.0.101:8000/api/"

    val getInstance:Api by lazy {

        val retrofit=Retrofit.Builder().baseUrl(BaseUrl).
        addConverterFactory(GsonConverterFactory.create()).build()
        retrofit.create(Api::class.java)

    }
}