package com.SmartTech.teasyNew.api_new.appmanager;

import com.SmartTech.teasyNew.api_new.appmanager.response_model.AEDCGetMeterInfoResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AEDCPurchaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AIRSBillStatusResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AIRSOutstandingPaymentsResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AIRSPayerValidationResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AIRSPaymentResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AIRSTaxListResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.AuthResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BankAccountValidationResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BankTransferRespondsModel;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.BaseResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.CardChargeAmountValidationResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.CheckStatusModel;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.FRSCPaymentResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.FRSCValidateAppResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetBalanceV2Response;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetInterswitchBillersResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetInterswitchPaymentItemsResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetNotificationsResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.GetUserDataResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.IOPCheckStatusResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.IOPMakePaymentResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.InterswitchPaymentResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.KIRSCheckStatusResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.KIRSPaymentAdditionalResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.KIRSPaymentResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.NISValidationResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.PADataValidationResponse;
import com.SmartTech.teasyNew.api_new.appmanager.response_model.TransactionHistoryResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppMananagerAPI {

    @GET("api/iopCheckStatus?responseFormat=JSON")
    Call<IOPCheckStatusResponse> iopCheckStatusRequest(
            @Query("txnRef") String txnRef,
            @Query("senderCountry") String senderCountry
    );

    @FormUrlEncoded
    @POST("api/iopUploadKYC?responseFormat=JSON")
    Call<BaseResponse> iopUploadKYCRequest(
            @Field("sessionID") String sessionID,
            @Field("txnRef") String txnRef,
            @Field("idProofType") String idProofType,
            @Field("idProofNo") String idProofNo,
            @Field("idProofImageData") String idProofImageData
    );

    @FormUrlEncoded
    @POST("api/iopUploadPhoto?responseFormat=JSON")
    Call<BaseResponse> iopUploadPhotoRequest(
            @Field("sessionID") String sessionID,
            @Field("txnRef") String txnRef,
            @Field("userImageData") String userImageData
    );

    @GET("api/iopMakePayment?responseFormat=JSON")
    Call<IOPMakePaymentResponse> iopMakePayment(
            @Query("walletID") String walletID,
            @Query("pin") String pin,
            @Query("sessionID") String sessionID,
            @Query("txnRef") String txnRef,
            @Query("senderCountry") String senderCountry
    );

    @FormUrlEncoded
    @POST("api/authV2?responseFormat=JSON")
    Call<AuthResponse> auth(
            @Field("walletId") String walletId,
            @Field("pin") String pin,
            @Field("imei") String imei,
            @Field("androidID") String androidID,
            @Field("simNumber") String simNumber,
            @Field("location") String location,
            @Field("softwareVersion") String softwareVersion,
            @Field("softwareVersionNumber") long softwareVersionNumber
    );

    @GET("api/purchaseAirtime?responseFormat=JSON")
    Call<BaseResponse> airtimePurchaseRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("destNumber") String destNumber,
            @Query("amount") long amount,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/getAEDCMeterInfo?responseFormat=JSON")
    Call<AEDCGetMeterInfoResponse> aedcGetMeterInfoRequest(
            @Query("meterNumber") String meterNumber
    );


    @GET("api/bankTransactionStatus?responseFormat=JSON")
    Call<CheckStatusModel> getBankStatus(
            @Query("sessionID") String sessionID,
            @Query("sourceCode") String bankCode
    );

    @GET("api/purchaseAEDC?responseFormat=JSON")
    Call<AEDCPurchaseResponse> aedcPurchaseRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("meterNumber") String meterNumber,
            @Query("amountInCents") long amountInCents,
            @Query("userType") String userType,
            @Query("agentShortCode") String agentShortCode,
            @Query("receiverPhoneNumber") String receiverPhoneNumber,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/validateFRSCApplication?responseFormat=JSON")
    Call<FRSCValidateAppResponse> frscValidateAppRequest(
            @Query("appID") String appID
    );

    @GET("api/makePaymentFRSC?responseFormat=JSON")
    Call<FRSCPaymentResponse> frscPaymentRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("appID") String appID,
            @Query("agentShortCode") String agentShortCode,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/kirsCheckPaymentStatus?responseFormat=JSON")
    Call<KIRSCheckStatusResponse> kirsCheckPaymentStatusRequest(
            @Query("txnID") String txnID
    );

    @GET("api/makePaymentKIRS?responseFormat=JSON")
    Call<KIRSPaymentResponse> kirsPaymentRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("agentShortCode") String agentShortCode,
            @Query("businessLevel") String businessLevel,
            @Query("businessType") String businessType,
            @Query("amountInCents") long amountInCents,
            @Query("ito") String ito,
            @Query("smsLang") String smsLang,
            @Query("paymentPeriod") String paymentPeriod,
            @Query("payerName") String payerName,
            @Query("payerSurname") String payerSurname,
            @Query("payerMobileNumber") String payerMobileNumber,
            @Query("payerAddress") String payerAddress,
            @Query("paymentType") String paymentType,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/makePaymentKIRSAdditional?responseFormat=JSON")
    Call<KIRSPaymentAdditionalResponse> kirsPaymentAdditionalRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("agentShortCode") String agentShortCode,
            @Query("amountInCents") long amountInCents,
            @Query("transactionID") String transactionID,
            @Query("payerMobileNumber") String payerMobileNumber,
            @Query("smsLang") String smsLang,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/getBalanceV2?responseFormat=JSON")
    Call<GetBalanceV2Response> getBalanceV2Request(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("userType") String userType,
            @Query("agentShortCode") String agentShortCode
    );

    @GET("api/getUserData?responseFormat=JSON")
    Call<GetUserDataResponse> getUserDataRequest(
            @Query("walletId") String walletId
    );

    @GET("api/validateBankAccount?responseFormat=JSON")
    Call<BankAccountValidationResponse> bankAccountValidationRequest(
            @Query("bankID") String bankID,
            @Query("accountNumber") String accountNumber
    );

    @GET("api/sendMoney?responseFormat=JSON")
    Call<BaseResponse> walletTransferRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("receiverNumber") String receiverNumber,
            @Query("amount") long amount,
            @Query("userType") String  userType,
            @Query("agentShortCode") String agentShortCode,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/bankTransfer?responseFormat=JSON")
    Call<BankTransferRespondsModel> bankTransferRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("bankID") String bankID,
            @Query("bankAccountId") String bankAccountId,
            @Query("amount") long amount,
            @Query("userType") String userType,
            @Query("agentShortCode") String agentShortCode,
            @Query("accountType") String accountType,
            @Query("saveBeneficiary") boolean saveBeneficiary,
            @Query("narration") String narration,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @FormUrlEncoded
    @POST("api/registerCustomer?responseFormat=JSON")
    Call<BaseResponse> registerCustomerRequest(
            @Field("walletId") String walletId,
            @Field("pin") String pin,
            @Field("firstname") String firstname,
            @Field("middlename") String middlename,
            @Field("lastname") String lastname,
            @Field("photo") String photo
    );

    @GET("api/getInterswitchPaymentItems?responseFormat=JSON")
    Call<GetInterswitchPaymentItemsResponse> getInterswitchPaymentItems(
            @Query("billerId") String billerId
    );

    @GET("api/getInterswitchBillers?responseFormat=JSON")
    Call<GetInterswitchBillersResponse> getInterswitchBillers(
            @Query("category") String category
    );

    @GET("api/payInterswitchBiller?responseFormat=JSON")
    Call<InterswitchPaymentResponse> payInterswitchBiller(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("billerId") String billerId,
            @Query("customerId") String customerId,
            @Query("amountInKobo") long amountInKobo,
            @Query("paymentCode") String paymentCode,
            @Query("agentShortCode") String agentShortCode,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/getNotifications?responseFormat=JSON")
    Call<GetNotificationsResponse> getNotificationsRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("userType") String userType,
            @Query("agentShortCode") String agentShortCode
    );

    @GET("api/makePayment?responseFormat=JSON")
    Call<BaseResponse> makePaymentRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("amount") long amount,
            @Query("billReference") String billReference,
            @Query("userType") String userType,
            @Query("agentShortCode") String agentShortCode,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/makePaymentHospital?responseFormat=JSON")
    Call<BaseResponse> hospitalPaymentRequest(
        @Query("walletId") String walletId,
        @Query("pin") String pin,
        @Query("amountInKobo") long amountInKobo,
        @Query("hospitalShortCode") String hospitalShortCode,
        @Query("billReferenceNumber") String billReferenceNumber,
        @Query("payerName") String payerName,
        @Query("remarks") String remarks,
        @Query("userType") String userType,
        @Query("agentShortCode") String agentShortCode,
        @Query("imei") String imei,
        @Query("androidID") String androidID,
        @Query("simNumber") String simNumber,
        @Query("location") String location
    );

    @GET("api/validateNISApplication?responseFormat=JSON")
    Call<NISValidationResponse> nisValidationRequest(
            @Query("appID") String appID
    );

    @GET("api/makePaymentNIS?responseFormat=JSON")
    Call<BaseResponse> nisPaymentRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("appID") String appID,
            @Query("userType") String userType,
            @Query("agentShortCode") String agentShortCode,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/changePin?responseFormat=JSON")
    Call<BaseResponse> pinChangeRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("newPin") String newPin
    );

    @GET("api/useVoucher?responseFormat=JSON")
    Call<BaseResponse> useTeasyVoucherRequest(
            @Query("sourceWallet") String sourceWallet,
            @Query("voucherPin") String voucherPin,
            @Query("destWallet") String destWallet,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/transactionHistory?responseFormat=JSON")
    Call<TransactionHistoryResponse> transactionHistoryRequest(
        @Query("walletId") String walletId,
        @Query("pin") String pin
    );

    @GET("api/makePaymentAIRS?responseFormat=JSON")
    Call<AIRSPaymentResponse> airsPaymentRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("agentShortCode") String agentShortCode,
            @Query("userType") String userType,
            @Query("payerId") String payerId,
            @Query("taxId") long taxId,
            @Query("taxCode") String taxCode,
            @Query("quantity") long quantity,
            @Query("amount") long amount,
            @Query("payerFirstname") String payerFirstname,
            @Query("payerLastname") String payerLastname,
            @Query("payerAddress") String payerAddress,
            @Query("payerMobileNumber") String payerMobileNumber,
            @Query("payerLGA") int payerLGA,
            @Query("paymentPeriod") String paymentPeriod,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/airsQuickPayRequest?responseFormat=JSON")
    Call<AIRSPaymentResponse> airsQuickPayRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("agentShortCode") String agentShortCode,
            @Query("userType") String userType,
            @Query("taxId") long taxId,
            @Query("taxCode") String taxCode,
            @Query("quantity") long quantity,
            @Query("amount") long amount,
            @Query("paymentPeriod") String paymentPeriod,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/makePaymentAIRSAdditional?responseFormat=JSON")
    Call<AIRSPaymentResponse> airsAdditionalPaymentRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("agentShortCode") String agentShortCode,
            @Query("userType") String userType,
            @Query("billRef") String billRef,
            @Query("payerMobileNumber") String payerMobileNumber,
            @Query("amount") long amount,
            @Query("taxCode") String taxCode,
            @Query("imei") String imei,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/airsBillStatus?responseFormat=JSON")
    Call<AIRSBillStatusResponse> airsBillStatusRequest(
            @Query("billRef") String billRef
    );

    @GET("api/airsTaxList?responseFormat=JSON")
    Call<AIRSTaxListResponse> airsTaxListRequest(
            @Query("collection") String collection
    );

    @GET("api/airsPayerValidation?responseFormat=JSON")
    Call<AIRSPayerValidationResponse> airsPayerValidation(
            @Query("payerId") String payerId
    );

    @GET("api/airsOutstandingPaymentsRequest?responseFormat=JSON")
    Call<AIRSOutstandingPaymentsResponse> airsOutstandingPaymentsRequest(
            @Query("payerId") String payerId
    );

    @GET("api/paDataValidationRequest?responseFormat=JSON")
    Call<PADataValidationResponse> paDataValidationRequest(
            @Query("msisdn") String msisdn
    );

    @GET("api/paDataPurchaseRequest?responseFormat=JSON")
    Call<BaseResponse> paDataPurchaseRequest(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("msisdn") String msisdn,
            @Query("productID") String productID,
            @Query("denomination") Long denomination,
            @Query("faceValue") String faceValue,
            @Query("androidID") String androidID,
            @Query("simNumber") String simNumber,
            @Query("location") String location
    );

    @GET("api/cardChargeAmountValidation?responseFormat=JSON")
    Call<CardChargeAmountValidationResponse> cardChargeAmountValidation(
            @Query("walletId") String walletId,
            @Query("amount") long amount
    );

    @GET("api/transactionHistoryV2?responseFormat=JSON")
    Call<TransactionHistoryResponse> transactionHistoryV2(
            @Query("walletId") String walletId,
            @Query("pin") String pin,
            @Query("dateFrom")String dateFrom,
            @Query("dateTo") String dateTo,
            @Query("limit") long limit
    );
}
