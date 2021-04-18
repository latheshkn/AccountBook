package com.unitedsoftek.accountbook.Models.getInvoiceReportModel

data class InvoiceReportModelResponse(
    var id: String,
    var customer_id: String,
    var user_id: String,
    var invoice_no: String,
    var total_amount: String,
    var amount_paid: String,
    var invoice_pdf: String,
    var invoice_date: String,
    var selected:Boolean=false
)
