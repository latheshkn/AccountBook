package com.unitedsoftek.accountbook.Models.getCustomerList

data class GetCustomerListModel(
    var status:String,
    var message:String,
    var customer_list:ArrayList<GetCustomerListModelResponse>,
)
