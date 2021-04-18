package com.unitedsoftek.accountbook.Models.getInvoiceReportModel

data class invoiceReportModel(
    var status:String,
    var message:String,
    var invoice_list:ArrayList<InvoiceReportModelResponse>,
)
