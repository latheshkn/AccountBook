package com.unitedsoftek.accountbook.Models.getCustomerList

data class GetCustomerListModelResponse(

var id:String,
var user_mobile:String,
var customer_name:String,
var customer_contact_no:String,
var customer_gst_no:String,
var selected:Boolean=false
)


