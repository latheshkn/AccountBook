package com.unitedsoftek.accountbook.Models.getQuotationReportModel

import com.unitedsoftek.accountbook.Models.getInvoiceReportModel.InvoiceReportModelResponse

data class QutatiionReportModel(
    var status:String,
    var message:String,
    var quotation_list:ArrayList<QuotationReportModelResponse>
)
