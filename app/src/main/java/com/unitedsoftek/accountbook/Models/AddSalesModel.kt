package com.unitedsoftek.accountbook.Models

data class AddSalesModel(
    var user_mobile:String,
    var customer_contact_no:String,
    var customer_gst_no:String,
    var customer_name:String,
    var invoice_no:String,
    var total_amount:String,
    var amount_paid:String,
    var user_id:String,
    var item_list:ArrayList<AddSalesResponseModel>,
)
