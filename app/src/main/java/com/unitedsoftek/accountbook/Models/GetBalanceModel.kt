package com.unitedsoftek.accountbook.Models

data class GetBalanceModel(
    var status:String,
    var message:String,
    var total_amount:String,
    var amount_paid:String,
)
