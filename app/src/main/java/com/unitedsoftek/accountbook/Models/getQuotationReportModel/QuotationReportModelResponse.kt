package com.unitedsoftek.accountbook.Models.getQuotationReportModel

data class QuotationReportModelResponse(
    var id: String,
    var customer_id: String,
    var user_id: String,
    var quotation_no: String,
    var total_amount: String,
    var quotation_pdf: String,
    var quotation_date: String,
    var selected: Boolean=false
)
