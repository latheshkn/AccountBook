package com.unitedsoftek.accountbook.Models

import com.google.gson.annotations.SerializedName

data class AddSalesResponseModel(
    var invoice_id:String,
    var item_name:String,
    var quantity:String,
    var price:String,/*sub total */
    var tax:String,
    var discount:String,
    var item_amount:String,
)
