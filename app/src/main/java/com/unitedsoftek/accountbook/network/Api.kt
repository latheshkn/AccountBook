package com.unitedsoftek.accountbook.network

import com.unitedsoftek.accountbook.Models.*
import com.unitedsoftek.accountbook.Models.getCustomerList.GetCustomerListModel
import com.unitedsoftek.accountbook.Models.getInvoiceReportModel.invoiceReportModel
import com.unitedsoftek.accountbook.Models.getQuotationReportModel.QutatiionReportModel
import retrofit2.Response
import retrofit2.http.*

interface Api {
    @FormUrlEncoded
    @POST("saveMobileNumber")
    suspend fun saveMobile(
        @Field("mobile") mobile: String
    ): Response<StatusMessageModel>


    @FormUrlEncoded
    @POST("login")
    suspend fun lolgin(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String
    ): Response<LoginResposeModel>

    @FormUrlEncoded
    @POST("register")
    suspend fun registerCompany(
        @Field("mobile") mobile: String,
        @Field("username") username: String,
        @Field("business_name") business_name: String,
        @Field("gst") gst: String,
    ): Response<LoginResposeModel>

    @FormUrlEncoded
    @POST("updateBusinessName")
    suspend fun updateBname(
        @Field("mobile") mobile: String,
        @Field("business_name") business_name: String,
    ): Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("updateMobileNumber")
    suspend fun updatePhone(
        @Field("mobile") mobile: String,
        @Field("secondary_number") secondary_number: String,
    ): Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("updateGst")
    suspend fun updateGst(
        @Field("mobile") mobile: String,
        @Field("gst") gst: String,
    ): Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("updateUpiId")
    suspend fun updateUpi(
        @Field("mobile") mobile: String,
        @Field("upiId") upiId: String,
    ): Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("updateBankDetail")
    suspend fun updateBankDetail(
        @Field("mobile") mobile: String,
        @Field("account_no") account_no: String,
        @Field("ifsc_code") ifsc_code: String,
        @Field("bank_name") bank_name: String,
        @Field("holder_name") holder_name: String,
    ): Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("updateBusinessAddress")
    suspend fun updateBisinessAddress(
        @Field("mobile") mobile: String,
        @Field("business_address") business_address: String
    ): Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("addParties")
    suspend fun addParties(
        @Field("user_mobile") mobile: String,
        @Field("customer_name") party_name: String,
        @Field("customer_contact_no") contact_no: String,
        @Field("customer_gst_no") customer_gst_no: String
    ): Response<StatusMessageModel>

    @POST("addSale")
    suspend fun AddSales(
        @Body addSalesModel: AddSalesModel
    ): Response<AddSalesModel>

    @POST("addQuotation")
    suspend fun AddQuotation(
        @Body addQuotation: AddQuotionModel
    ): Response<AddQuotionModel>

    @GET("getInvoiceReport")
    suspend fun getInvoicereport(
        @Query("user_mobile") user_mobile: String
    ): Response<invoiceReportModel>

    @GET("getQuotationReport")
    suspend fun getQuotationreport(
        @Query("user_mobile") user_mobile: String
    ): Response<QutatiionReportModel>

     @GET("getUserDetails")
    suspend fun getUserDetails(
        @Query("mobile") mobile: String
    ): Response<GetUserDetailModel>

@GET("getCustomerList")
    suspend fun getCustomerList(
        @Query("mobile") mobile: String
    ): Response<GetCustomerListModel>

    @FormUrlEncoded
    @POST("deleteParties")
    suspend fun deleteParty(
        @Field("user_mobile") user_mobile:String,
        @Field("customer_contact_no") customer_contact_no:String,
    ): Response<StatusMessageModel>


    @FormUrlEncoded
    @POST("deleteSale")
    suspend fun deleteSale(
        @Field("invoice_no") invoice_no: String,
    ): Response<StatusMessageModel>

    @FormUrlEncoded
    @POST("deleteQuotation")
    suspend fun deleteQuotation(
        @Field("quotation_no") quotation_no: String,
    ): Response<StatusMessageModel>

    @GET("getBalanceAmount")
    suspend fun getBalanceAmount(
        @Query("mobile") mobile: String
    ): Response<GetBalanceModel>

}
