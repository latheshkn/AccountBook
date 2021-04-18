package com.unitedsoftek.accountbook.Models

 data class LoginResponseData (
      val id:String,
      val username:String,
      val email:String,
      val password:String,
      val mobile:String,
      val secondary_mobile:String,
      val otp:String,
      val image:String,
      val logo:String,
      val business_name:String,
      val gst:String,
      val business_address:String,
 )
