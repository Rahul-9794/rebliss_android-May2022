package com.rebliss.presenter.retrofit;


import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ActivityTaskResponse;
import com.rebliss.domain.model.AssignmentStatusCheckResponse;
import com.rebliss.domain.model.CarouselResponse;
import com.rebliss.domain.model.Category_Ref_Response;
import com.rebliss.domain.model.RbmAddressRequest;
import com.rebliss.domain.model.RefrenceRequest;
import com.rebliss.domain.model.RefrenceResponse;
import com.rebliss.domain.model.ResonseforActivity;
import com.rebliss.domain.model.SeachMerchantData;
import com.rebliss.domain.model.CheckCPOrderModel;
import com.rebliss.domain.model.CommonResponse;
import com.rebliss.domain.model.CpPartnerSelectionModel;
import com.rebliss.domain.model.CudelResponse;
import com.rebliss.domain.model.EarningSummaryResponse;
import com.rebliss.domain.model.EducationResponse;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.InsuranceAmount.InsuranceAmountResponse;
import com.rebliss.domain.model.KycResponse;
import com.rebliss.domain.model.MyEarningResponse;
import com.rebliss.domain.model.Occupation.OccupationResponse;
import com.rebliss.domain.model.OpportunityDetailResponse;
import com.rebliss.domain.model.PartnerSelectionModel;
import com.rebliss.domain.model.PineWebViewResponse.PinewebviewResponse;
import com.rebliss.domain.model.SearchSathiRecordResponse;
import com.rebliss.domain.model.SelfAssessmentModel;
import com.rebliss.domain.model.Service_Response;
import com.rebliss.domain.model.ShopImageUploadRequest;
import com.rebliss.domain.model.ShopUploadResponse;
import com.rebliss.domain.model.TermsModel;
import com.rebliss.domain.model.TrainingResponse;
import com.rebliss.domain.model.ViewInvoiceResponse;
import com.rebliss.domain.model.activity_detail.ActivityDetailResponse;
import com.rebliss.domain.model.agegroup.AgeGroupResponse;
import com.rebliss.domain.model.approvedecline.ApproveRequest;
import com.rebliss.domain.model.approvedecline.ApproveResponce;
import com.rebliss.domain.model.appversion.AppVersionResponce;
import com.rebliss.domain.model.assignment_review.AssignmentModel;
import com.rebliss.domain.model.bankit.BankItBody;
import com.rebliss.domain.model.bankit.BankItResponse;
import com.rebliss.domain.model.categoryresponse.CategoryResponse;
import com.rebliss.domain.model.changepassword.ChangePasswordRequest;
import com.rebliss.domain.model.changepassword.ChangePasswordResponce;
import com.rebliss.domain.model.city.CityResponce;
import com.rebliss.domain.model.demandpartner.DemandPartnerNameResponse;
import com.rebliss.domain.model.documentByUser.DocumentByUserModel;
import com.rebliss.domain.model.editprofile.EditProfileRequest;
import com.rebliss.domain.model.editprofile.EditProfileResponce;
import com.rebliss.domain.model.employee.EmployeeFilter;
import com.rebliss.domain.model.fileupload.FileUploadResponce;
import com.rebliss.domain.model.fileupload.UploadRequest;
import com.rebliss.domain.model.forgotpassword.ForgotPasswordRequest;
import com.rebliss.domain.model.forgotpassword.ForgotResponce;
import com.rebliss.domain.model.fosattendancedetail.FosAttendanceStatus;
import com.rebliss.domain.model.fospos.GetFosPos;
import com.rebliss.domain.model.fospostest.GetFosPosTest;
import com.rebliss.domain.model.getRefrenceDataResponse;
import com.rebliss.domain.model.group.GroupResponce;
import com.rebliss.domain.model.industrytype.IndustryTypeResponse;
import com.rebliss.domain.model.login.AppBannerResponse;
import com.rebliss.domain.model.login.Log_inRequest;
import com.rebliss.domain.model.login.LoginResponce;
import com.rebliss.domain.model.logout.LogoutResponce;
import com.rebliss.domain.model.myactivity.MyActivityDashboardResponse;
import com.rebliss.domain.model.notificationlist.NotificationListResponse;
import com.rebliss.domain.model.opportunitylist.OpportunityListResponse;
import com.rebliss.domain.model.otp.OtpRequest;
import com.rebliss.domain.model.otp.OtpResponce;
import com.rebliss.domain.model.otp.ResendOTPResponce;
import com.rebliss.domain.model.passwordsetup.PasswordSetUpRequest;
import com.rebliss.domain.model.passwordsetup.PasswordSetupResponse;
import com.rebliss.domain.model.payment.AddressDetailResponse;
import com.rebliss.domain.model.payment.InvoiceListResponse;
import com.rebliss.domain.model.payment.OrderIdRazorPayResponse;
import com.rebliss.domain.model.payment.PaymentDetailResponse;
import com.rebliss.domain.model.payment.PincodeDetailResponse;
import com.rebliss.domain.model.payment.SuccessResponse;
import com.rebliss.domain.model.personal_data_request;
import com.rebliss.domain.model.placedorderstatus.AcceptOrderStatusRequest;
import com.rebliss.domain.model.placedorderstatus.AcceptOrderStatusResponse;
import com.rebliss.domain.model.profile.ProfileResponce;
import com.rebliss.domain.model.rBMDetailResponse;
import com.rebliss.domain.model.rbmAddressResponse;
import com.rebliss.domain.model.response_for_activity.ActivityResponse;
import com.rebliss.domain.model.searchstate.SearchStateResponce;
import com.rebliss.domain.model.selfassessmentquestions.SelfAssessmentResponse;
import com.rebliss.domain.model.service_output_response;
import com.rebliss.domain.model.servicesRequest;
import com.rebliss.domain.model.shopcategory.ShopCategoryResponse;
import com.rebliss.domain.model.signup.SignupRequest;
import com.rebliss.domain.model.signup.SignupResponce;
import com.rebliss.domain.model.state.StateResponce;
import com.rebliss.domain.model.termsandconditions.TermsAndConditionsResponse;
import com.rebliss.domain.model.opportunityvideo.OpportunityVideoRequest;
import com.rebliss.domain.model.opportunityvideo.OpportunityVideosResponse;
import com.rebliss.domain.model.update_status_rbmlist_request;
import com.rebliss.domain.model.update_status_rbmlist_response;
import com.rebliss.domain.personal_data_response;
import com.rebliss.view.activity.cpdashboardforfos.AcivityModel;
import com.rebliss.view.activity.cpdashboardforfos.dialogsmodel.CpListResponse;
import com.rebliss.view.activity.downloadviaLink.DemandPartnerAppLinkModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.jvm.JvmStatic;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {

    //get custom-privacy data response
    @GET("group/question")
    Call<SelfAssessmentResponse> getSelfAssessmentQuestionsData();

    //get youtube data response
    @POST("group/opportunity-video")
    @Headers({"Content-Type: application/json"})
    Call<OpportunityVideosResponse> getYoutubeDataApi(@Body OpportunityVideoRequest opportunityVideoRequest);

    @POST("group/answer-of-question")
    @Headers({"Content-Type: application/json"})
    Call<SuccessResponse> postSelfAssessment(@Body SelfAssessmentModel selfAssessmentModel);

    @POST("user/accept-terms-condition")
    @Headers({"Content-Type: application/json"})
    Call<SuccessResponse> postTermsCheck(@Body TermsModel termsModel);

    //get custom-privacy data response
    @GET("group/custom-privacy")
    Call<TermsAndConditionsResponse> getTermsAndConditionDATA();

    //login request*//*

    @POST(Constant.K_POST_SET_PASSWORD)
    @Headers({"Content-Type: application/json"})
    Call<PasswordSetupResponse> getPASSWORDSetup(@Body PasswordSetUpRequest passwordSetUpRequest);

    @POST(Constant.K_POST_GROUP_ID)
    @Headers({"Content-Type: application/json"})
    Call<SuccessResponse> updateGroupId(@Body PartnerSelectionModel partnerSelectionModel);

    @POST(Constant.K_POST_UPDATE_PARTNER_TYPE)
    @Headers({"Content-Type: application/json"})
    Call<SuccessResponse> updatePartnerType(@Body CpPartnerSelectionModel cpPartnerSelectionModel);

    //get occupation data response
    @GET("group/get-occupation")
    Call<OccupationResponse> getOccupationDATA(@Query("type") String type);

    //get age group data response
    @GET("group/get-age-range")
    Call<AgeGroupResponse> getAgeGroupDATA();

    @GET("group/get-education")
    Call<EducationResponse> getEducationData();


    //post group/accept-activity data
    @POST("group/accept-activity")
    @Headers({"Content-Type: application/json"})
    Call<AcceptOrderStatusResponse> postAcceptOrderStatusActivityApi(@Body AcceptOrderStatusRequest acceptOrderStatusRequest);

    @POST("group/accept-activity-clone")
    @Headers({"Content-Type: application/json"})
    Call<AcceptOrderStatusResponse> postAcceptOrderStatusActivityCloneApi(@Body AcceptOrderStatusRequest acceptOrderStatusRequest);


    /*login request*/
    @POST(Constant.K_POST_LOGIN)
    @Headers({"Content-Type: application/json"})
    Call<LoginResponce> postUserLogin(
            //@Header("Authorization") String authorization,
            @Body Log_inRequest lRequest
    );

    /*Account approve request*/
    @POST(Constant.K_POST_APPROVE)
    @Headers({"Content-Type: application/json"})
    Call<ApproveResponce> postAccountApprove(
            @Header("Authorization") String authorization,
            @Query("user_id") String approveRequest
    );

    /*Account Dedline request*/
    @POST(Constant.K_POST_DECLINE)
    @Headers({"Content-Type: application/json"})
    Call<ApproveResponce> postAccountDecline(
            @Header("Authorization") String authorization,
            @Body ApproveRequest approveRequest
    );

    /*login request*/
    @POST(Constant.K_POST_CHANGE_PASSWORD)
    @Headers({"Content-Type: application/json"})
    Call<ChangePasswordResponce> postUserChangePassword(
            @Header("Authorization") String authorization,
            @Body ChangePasswordRequest changePasswordRequest
    );

    /*Signup request*/
    @POST(Constant.K_POST_REGISTER)
    @Headers({"Content-Type: application/json"})
    Call<SignupResponce> postUserSignup(
            //@Header("Authorization") String authorization,
            @Body SignupRequest signupRequest
    );

    @POST(Constant.K_POST_REGISTER_RBS)
    @Headers({"Content-Type: application/json"})
    Call<SignupResponce> postUserSignupforRBS(
            //@Header("Authorization") String authorization,
            @Body SignupRequest signupRequest
    );

    @POST(Constant.K_POST_REGISTER_RBS_BY_RBS)
    @Headers({"Content-Type: application/json"})
    Call<SignupResponce> postUserSignupforRBS_BY_RBS(
            //@Header("Authorization") String authorization,
            @Body SignupRequest signupRequest
    );

    /*get StateCity*/
    @GET(Constant.K_GET_STATE_CITY)
    @Headers({"Content-Type: application/json"})
    Call<SearchStateResponce> getStateCity(
            @Header("Authorization") String authorization,
            @Query("zipcode") String zipcode);

    /*Signup POS request*/
    @POST(Constant.K_POST_FOS_REGISTER)
    @Headers({"Content-Type: application/json"})
    Call<SignupResponce> postFOSUserSignup(
            @Header("Authorization") String authorization,
            @Body SignupRequest signupRequest
    );


    @POST(Constant.K_POST_BANK_IT_SHOW_HIDE)
    @Headers({"Content-Type: application/json"})
    Call<BankItResponse> postBankItButton(
            @Header("Authorization") String authorization,
            @Body BankItBody bankItBody
    );


    /*get Group List*/
    @GET(Constant.K_GET_USER_LOGOUT)
    @Headers({"Content-Type: application/json"})
    Call<LogoutResponce> getUserLogout(@Header("Authorization") String authorization);

    @POST(Constant.K_CHECK_KYC)
    @FormUrlEncoded
    Call<KycResponse> checkKyc(@Field("fos_id") String fos_id);

    /*get Group List*/
    @GET
    @Headers({"Content-Type: application/json"})
    Call<ResendOTPResponce> getUserResendOTP(@Header("Authorization") String authorization, @Url String uurl);


    /*get Group List*/
    @GET(Constant.K_GET_FOS_DETAILs)
    @Headers({"Content-Type: application/json"})
    Call<LogoutResponce> getFosList(@Header("Authorization") String authorization);


    /*get Group List*/
    @GET(Constant.K_GET_GROUP)
    @Headers({"Content-Type: application/json"})
    Call<GroupResponce> getGrouplist();

    /*get Group List*/
    @GET(Constant.K_GET_USER_PROFILE)
    @Headers({"Content-Type: application/json"})
    Call<ProfileResponce> getProfile(@Header("Authorization") String authorization);

    /*get Group List*/
    @GET(Constant.K_GET_HOME_CAROUSEL)
    @Headers({"Content-Type: application/json"})
    Call<CarouselResponse> getCarousel(@Query("user_type") String user_type, @Query("position") String position);


    /*get Fos Pos profile*/
    @Multipart
    @POST(Constant.K_POST_USER_PROFILE_IMAGE)
    Call<ProfileResponce> postUserProfileImage(@Part MultipartBody.Part userProfileImage, @Part("user_id") RequestBody userId);

    @GET(Constant.K_GET_USER_PROFILE)
    @Headers({"Content-Type: application/json"})
    Call<ProfileResponce> getProfileUser(@Header("Authorization") String authorization, @Query("user_id") String userId);

    /*get Group List*/
    @GET(Constant.K_GET_STATE)
    @Headers({"Content-Type: application/json"})
    Call<StateResponce> getState(@Header("Authorization") String authorization);

    /*get Group List*/
    @GET(Constant.K_GET_City)
    @Headers({"Content-Type: application/json"})
    Call<CityResponce> getCity(@Header("Authorization") String authorization, @Query("stateId") String stateId);

    /*get Group List*/
    @GET(Constant.K_GET_CATEGORY)
    @Headers({"Content-Type: application/json"})
    Call<CategoryResponse> getCategory();

    /*get Group List*/
    @FormUrlEncoded
    @POST(Constant.K_GET_SUB_CATEGORY)
    Call<CategoryResponse> getSubCategory(@Field("category_id") int catId);

    /*get Group List*/
    @GET(Constant.K_GET_SHOP_CATEGORY)
    @Headers({"Content-Type: application/json"})
    Call<ShopCategoryResponse> getShopCategories();

    @FormUrlEncoded
    @POST(Constant.K_GET_GET_EMPLOYEE_FILTER)
    Call<EmployeeFilter> getemployeefilter(
            @Field("fos_id") int fos_id,
            @Field("employee_id") String employee_id,
            @Field("date") String date);

    @FormUrlEncoded
    @POST(Constant.K_POST_fosbyemplyee)
    Call<CpListResponse> fosbyempolyee
            (@Field("employee_id") String employee_id);

    /*get Group List*/
    @FormUrlEncoded
    @POST(Constant.K_GET_MOBILE_STATUS)
    Call<CategoryResponse> getMobileStatus(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(Constant.K_ACTIVITY_TASK)
    Call<ActivityTaskResponse> getActivityTask(@Field("user_id") String user_id, @Field("city") String city, @Field("state") String state);

    @FormUrlEncoded
    @POST(Constant.K_ACTIVITY_TASK_TEST)
    Call<ActivityTaskResponse> getActivityTaskTest(@Field("user_id") String user_id, @Field("city") String city, @Field("state") String state, @Field("pin_code") String pincode);

    @FormUrlEncoded
    @POST(Constant.K_DAILY_DEDUCTION)
    Call<SuccessResponse> getDailyDeduction(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(Constant.K_APP_VERSION)
    Call<SuccessResponse> getAppVersion(@Field("user_id") String user_id,
                                        @Field("app_version") String app_version);

    @FormUrlEncoded
    @POST(Constant.K_CHECK_TODAY_DEDUCTION)
    Call<SuccessResponse> getTodaysDeduction(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(Constant.K_EARNING_ON_TASK)
    Call<SuccessResponse> getEarningOnTask(@Field("user_id") String user_id,
                                           @Field("earning_task_id") String earning_task_id,
                                           @Field("activity_id") String activity_id,
                                           @Field("amount") String amount);

    @GET(Constant.K_GET_TRAINING)
    Call<TrainingResponse> getTraining(@Query("sub_category1_id") String sub_category1_id);

    @GET(Constant.K_GET_MY_EARNING)
    Call<MyEarningResponse> getMyEarning(@Query("user_id") String user_id, @Query("start_date") String start_date);

    @FormUrlEncoded
    @POST(Constant.K_POST_CHECK_VALIDATION)
    Call<CategoryResponse> checkDuplicasy(
            @Field("store_id") String store_id,
            @Field("phone") String phone,
            @Field("category_id") String category_id,
            @Field("sub_category1_id") String sub_category1_id,
            @Field("type") Integer type
    );

    @Multipart
    @POST(Constant.K_GET_SAVE_MY_ACTIVITY)
    Call<ErrorBody> saveMyActivity(
            @PartMap() Map<String, RequestBody> partMap,
            @PartMap HashMap<String, Integer> activityArray,
            @Part List<MultipartBody.Part> outSideFile,
            @Part List<MultipartBody.Part> activityFile,
            @Part List<MultipartBody.Part> insideFile,
            @Part MultipartBody.Part gstFile,
            @Part MultipartBody.Part panFile
    );

    @Multipart
    @POST(Constant.K_GET_SAVE_MY_ACTIVITY)
    Call<ErrorBody> saveMyActivityWithoutGst(
            @PartMap() Map<String, RequestBody> partMap,
            @PartMap HashMap<String, Integer> activityArray,
            @Part List<MultipartBody.Part> outSideFile,
            @Part List<MultipartBody.Part> activityFile,
            @Part List<MultipartBody.Part> insideFile,
            @Part MultipartBody.Part panFile
    );

    @Multipart
    @POST(Constant.K_GET_SAVE_UPDATE_ACTIVITY)
    Call<ErrorBody> updateMyActivity(
            @PartMap() Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> outSideFile,
            @Part List<MultipartBody.Part> activityFile,
            @Part List<MultipartBody.Part> insideFile,
            @Part MultipartBody.Part gstFile,
            @Part MultipartBody.Part panFile,
            @Part List<MultipartBody.Part> previosoutsideimage,
            @Part List<MultipartBody.Part> previousinsideimage,
            @Part List<MultipartBody.Part> previousactivityimage);

    @Multipart
    @POST(Constant.K_GET_SAVE_UPDATE_ACTIVITY)
    Call<ErrorBody> updateMyActivityWithoutGst(
            @PartMap() Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> outSideFile,
            @Part List<MultipartBody.Part> activityFile,
            @Part List<MultipartBody.Part> insideFile,
            @Part MultipartBody.Part panFile,
            @Part List<MultipartBody.Part> previosoutsideimage,
            @Part List<MultipartBody.Part> previousinsideimage,
            @Part List<MultipartBody.Part> previousactivityimage);

    /*get Group List*/
    @FormUrlEncoded
    @POST(Constant.K_GET_SAVE_ACTIVITY_DETAIL)
    Call<ActivityDetailResponse> getActivityDetail(@Field("activity_id") int activityId);

    @FormUrlEncoded
    @POST(Constant.K_POST_fosattendancestatus)
    Call<FosAttendanceStatus> getstatus(
            @Field("fos_id") String fos_id);

    @FormUrlEncoded
    @POST(Constant.K_POST_fosattendance)
    Call<CpListResponse> fosattendance(
            @Field("fos_id") String fos_id,
            @Field("status") String status);

    @FormUrlEncoded
    @POST(Constant.K_POST_opportunitylist)
    Call<OpportunityListResponse> getopportunity(@Field("fos_id") String userId);

    @GET("group/opportunity-media")
    Call<OpportunityDetailResponse> getopportunityDetails(
            @Query("opportunity_title") String opportunity_title);

    @FormUrlEncoded
    @POST(Constant.K_GET_NOTIFICATION_LIST)
    Call<NotificationListResponse> getNotificationList(@Field("id") String userId);

    @GET(Constant.K_GET_PAYMENT_DETAIL)
    Call<PaymentDetailResponse> getPaymentDetail(@Query("state_id") String state);

    @GET(Constant.K_GET_PAYMENT_DETAIL_TEST)
    Call<PaymentDetailResponse> getPaymentDetailTest(
            @Query("state_id") String state,
            @Query("user_id") String user_id);

    @GET(Constant.K_GET_INSURANCE_AMOUNT)
    Call<InsuranceAmountResponse> getInsuranceAmount();


    @POST(Constant.K_GET_ORDERS)
    Call<OrderIdRazorPayResponse> getRazorPayOrderId(
            @Header("Authorization") String authorization,
            @Body HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(Constant.K_GET_ADDRESS_DETAIL)
    Call<AddressDetailResponse> getAddressDetail(
            @Field("cp_id") String userId);


    @FormUrlEncoded
    @POST(Constant.K_UPDATE_ADDRESS_DETAIL)
    Call<SuccessResponse> updateAddressDetail(
            @Field("cp_id") String userId,
            @Field("address_line_1") String address_line_1,
            @Field("address_line_2") String address_line_2,
            @Field("city") String city,
            @Field("state") String state,
            @Field("zip") String zip,
            @Field("gst") String gst);

    @FormUrlEncoded
    @POST(Constant.K_SUBMIT_ORDEE)
    Call<SuccessResponse> submitOrder(
            @Field("cp_id") String userId,
            @Field("invoice_id") String invoice_id,
            @Field("total") String total,
            @Field("tax") Double tax);

    @FormUrlEncoded
    @POST(Constant.K_GET_INVOICE_LIST)
    Call<InvoiceListResponse> getInvoiceList(
            @Field("cp_id") String userId);

    @FormUrlEncoded
    @POST(Constant.K_GET_NOTIFICATION_UPDATE_STATUS)
    Call<CommonResponse> updateNotificationReadStatus(
            @Field("notification_id") int notificationId);

    @FormUrlEncoded
    @POST(Constant.K_POST_fosopportunity)
    Call<CpListResponse> fosopportunity(
            @Field("fos_id") String fos_id,
            @Field("opportunity_id") String opportunity_id);

    /*get Group List*/
    @FormUrlEncoded
    @POST(Constant.K_GET_SAVE_MY_ACTIVITY_LIST)
    Call<MyActivityDashboardResponse> getMyactivityList(@Field("fos_id") String fosId,
                                                        @Field("date") String date);

    @FormUrlEncoded
    @POST(Constant.K_GET_SAVE_MY_ACTIVITY_LIST_TEST)
    Call<MyActivityDashboardResponse> getMyactivityListTest(@Field("fos_id") String fosId,
                                                            @Field("date") String date,
                                                            @Field("sub_category1_id") String subCategoryId);

    @FormUrlEncoded
    @POST(Constant.K_GET_SAVE_MY_ACTIVITY_LIST_TEST_TWO)
    Call<MyActivityDashboardResponse> getMyactivityListTestTwo(@Field("fos_id") String fosId,
                                                               @Field("date") String date,
                                                               @Field("sub_category1_id") String subCategoryId);

    @FormUrlEncoded
    @POST(Constant.K_GET_SAVE_MY_DECLINED_ACTIVITY_LIST)
    Call<MyActivityDashboardResponse> getMyDeclineActivityList(@Field("fos_id") String fosId);


    @FormUrlEncoded
    @POST(Constant.K_GET_GET_CP_FILTER)
    Call<AcivityModel> getcpfiler(
            @Field("fos_id") int fos_id,
            @Field("cp_id") String cp_id,
            @Field("date") String date);

    @FormUrlEncoded
    @POST(Constant.K_POST_cplist)
    Call<CpListResponse> getcplist(
            @Field("cp_id") String cp_id);

    /*get FOSpOS List*/
    @GET(Constant.K_GET_FOS_POS)
    @Headers({"Content-Type: application/json"})
    Call<GetFosPos> getFosPos(@Header("Authorization") String authorization, @Query("groupID") String groupID);


    @GET(Constant.K_GET_FOS_POS_TEST)
    @Headers({"Content-Type: application/json"})
    Call<GetFosPosTest> getFosPosTest(@Header("Authorization") String authorization, @Query("groupID") String groupID);


    /*get Group List*/
    @POST(Constant.K_POST_EDIT_PROFILE)
    @Headers({"Content-Type: application/json"})
    Call<EditProfileResponce> postEditProfile(@Header("Authorization") String authorization, @Body EditProfileRequest editProfileRequest);

    /*get Group List*/
    @POST(Constant.K_POST_UPLOAD_FILE)
    @Headers({"Content-Type: application/json"})
    Call<FileUploadResponce> postUploadfile(@Body UploadRequest uploadRequest);


    @POST(Constant.K_POST_UPLOAD_ShOP_IMAGE)
    @Headers({"Content-Type: application/json"})
    Call<ShopUploadResponse> postUploadShopfile(@Part MultipartBody.Part userProfileImage, @Part("user_id") RequestBody userId);

    /*get Group List*/
    @POST(Constant.K_POST_UPLOAD_FILE)
    @Headers({"Content-Type: application/json"})
    Call<AppVersionResponce> postAppVersion(@Header("Authorization") String authorization, @Query("app_version") String appversion);

    /*Verify OTP request*/
    @POST(Constant.K_POST_VERIFY_OTP)
    @Headers({"Content-Type: application/json"})
    Call<OtpResponce> verifyOTP(
            @Header("Authorization") String authorization,
            @Body OtpRequest requestOTP);

    /*Verify OTP request*/
    @POST(Constant.K_POST_FORGOT_PASSWORD)
    @Headers({"Content-Type: application/json"})
    Call<ForgotResponce> postForgotPasswrod(
            @Header("Authorization") String authorization,
            @Body ForgotPasswordRequest gotPasswordRequest);

    @GET(Constant.K_APP_BANNER)
    Call<AppBannerResponse> getAppBanner();

    /*Get Industry Type Spinner*/
    @GET(Constant.K_GET_INDUSTRY_TYPE)
    @Headers({"Content-Type: application/json"})
    Call<IndustryTypeResponse> getIndustyType(
            @Header("Authorization") String authorization
            , @Query("type") String group_id);

    /*Get Industry Type Spinner*/
    @GET()
    Call<PincodeDetailResponse> getPincodeDetail(
            @Url String url);

    @POST(Constant.K_POST_CUDEL)
    @FormUrlEncoded
    Call<CudelResponse> postCudel(
            @Field("fos_id") String fos_id,
            @Field("type") String type,
            @Field("business_name") String business_name,
            @Field("shop_category") String shop_category,
            @Field("form_status") String form_status,
            @Field("category_id") String category_id,
            @Field("sub_category_id") String sub_category_id,
            @Field("sub_category1_id") String sub_category1_id,
            @Field("address") String address,
            @Field("city") String city,
            @Field("state") String state,
            @Field("pincode") String pincode,
            @Field("lat") double lat,
            @Field("long") double longitude,
            @Field("app_version") String app_version,
            @Field("mobile_number") String mobile_number);

    @POST(Constant.K_POST_Daily_DSR_Report)
    @FormUrlEncoded
    Call<CudelResponse> postDailyDSR(
            @Field("fos_id") String fos_id,
            @Field("demand_partner") String demand_partner,
            @Field("shop_name") String shop_name,
            @Field("shop_id") String shop_id,
            @Field("mobile_no") String mobile_no,
            @Field("shop_category") String shop_category,
            @Field("pincode") String pincode,
            @Field("state") String state,
            @Field("city") String city,
            @Field("address") String address,
            @Field("status") String status,
            @Field("remark1") String remark1,
            @Field("remark2") String remark2,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("hidden_address") String hidden_address);


    @GET(Constant.K_GET_DEMAND_PARTNER_NAME)
    Call<DemandPartnerNameResponse> getDemandPartnerName();

    @GET(Constant.K_GET_WEBVIEW_PINE_LABS)
    Call<PinewebviewResponse> getWebviewPineLabs();

    @GET(Constant.K_GET_EARNING_SUMMARY)
    Call<EarningSummaryResponse> getEariningSummary(@Query("user_id") String user_id, @Query("start_date") String start_date, @Query("earning_task_id") String earning_task_id);

    @GET(Constant.K_GET_WITHDRAW_TASK_AMOUNT)
    Call<SuccessResponse> getWithdrawTaskAmount(@Query("user_id") String user_id, @Query("start_date") String start_date, @Query("earning_task_id") String earning_task_id);

    @GET(Constant.K_GET_CHECK_PAYOUT)
    Call<EarningSummaryResponse> getCheckPayout(@Query("user_id") String user_id, @Query("start_date") String start_date);

    @GET(Constant.K_GET_CHECK_PAYOUT_CATEGORY)
    Call<ViewInvoiceResponse> getCheckPayoutbyCategory(@Query("user_id") String user_id, @Query("start_date") String start_date, @Query("sub_category") String sub_category);

    @POST(Constant.K_UPDATE_USER_PROFILE)
    @FormUrlEncoded
    Call<ProfileResponce> updateUserProfile(
            @Field("user_id") String user_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("fos_shop_name") String shop_name,
            @Field("occupation") String occupation,
            @Field("age_limit") String age_limit,
            @Field("gender") String gender,
            @Field("education") String education,
            @Field("personal_email_id") String email,
            @Field("dob") String dob,
            @Field("cp_adhar_proof") String adhar_proof,
            @Field("aadhar_no") String aadhar_no,
            @Field("upload_type_option") int upload_type_option);

    //check payment order done for CP
    @POST(Constant.K_CHECK_ORDER_CP)
    @FormUrlEncoded
    Call<CheckCPOrderModel> checkOrderforCP(
            @Field("cp_id") String userId);

    @POST(Constant.K_USER_CURRENT_LOCATION)
    @FormUrlEncoded
    Call<SuccessResponse> updateUserCurrentLocation(
            @Field("user_id") String userId,
            @Field("district") String district,
            @Field("state") String state,
            @Field("zip_code") String zipcode);

    // API for download link
    @GET(Constant.K_GET_DEMAND_PARTNER_APP_LINK)
    Call<DemandPartnerAppLinkModel> getDemandPartnerAppLink();

    @GET(Constant.K_SEARCH_RBM_RBS)
    Call<SeachMerchantData> getMerchantList(@Query("rbs_id") String rbs_id, @Query("date") String date, @Query("phone") String phone, @Query("status") String status);

    @GET(Constant.K_SEARCH_RBM_RBS_NEW)
    Call<SeachMerchantData> getMerchantListNew(@Query("id") String rbs_id, @Query("type") String type, @Query("date") String date, @Query("phone") String phone, @Query("status") String status);

    @GET(Constant.K_FIND_RBM_DETAIL)
    Call<rBMDetailResponse> getrBMDetail(@Query("rbm_id") String rbs_id);

    @GET(Constant.K_GET_ASSIGNMENT_PREVIEW)
    Call<AssignmentModel> getAssignment(@Query("activity_id") String activity_id);

    /*Get Industry Type Spinner*/
    @GET(Constant.K_GET_SERVICE)
    @Headers({"Content-Type: application/json"})
    Call<Service_Response> getServiceData(@Query("type") String type);

    //Category Data for refrences
    @GET(Constant.K_GET_CATEGORY)
    @Headers({"Content-Type: application/json"})
    Call<Category_Ref_Response> getCategoryData();

    //save refrence data
    @POST(Constant.K_POST_SAVE_REFRENCE)
    @Headers({"Content-Type: application/json"})
    Call<RefrenceResponse> saveRefData(@Body RefrenceRequest refrenceRequest);

    //save refrence data
    @POST(Constant.K_POST_UPDATE_RBM_ADDRESS)
    @Headers({"Content-Type: application/json"})
    Call<rbmAddressResponse> rbmAddress(@Body RbmAddressRequest rbmAddressRequest);

    //save sevices data
    @POST(Constant.K_POST_SAVE_SERVICES)
    @Headers({"Content-Type: application/json"})
    Call<service_output_response> saveServices(@Body servicesRequest serviceRequeat);

    @POST(Constant.K_POST_SAVE_PERSONAL_DETAILS)
    @Headers({"Content-Type: application/json"})
    Call<personal_data_response> savePersonalData(@Body personal_data_request personal_data_request);

    @GET(Constant.K_GET_REFRENCE_DATA)
    Call<getRefrenceDataResponse> getRefData(
            @Query("user_id") String user_id);

    @Multipart
    @POST(Constant.K_POST_WORK_PROFILE)
    Call<ErrorBody> saveWorkProfile(
            @PartMap() Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> outSideFile,
            @Part List<MultipartBody.Part> insideFile);

    @Multipart
    @POST(Constant.K_POST_SELF_KYC)
    Call<ErrorBody> saveSelfKYC(
            @PartMap() Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> self_kyc_image);

    @Multipart
    @POST(Constant.K_POST_WORK_KYC)
    Call<ErrorBody> saveworkKYC(
            @PartMap() Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> work_kyc_image);

    @Multipart
    @POST(Constant.K_POST_SAVE_ASSIGNMENT_DETAILS)
    Call<ErrorBody> saveAssignmentData(
            @PartMap() Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> inside_photo,
            @Part MultipartBody.Part gst_photo,
            @Part MultipartBody.Part pan_photo);

    @Multipart
    @POST(Constant.K_POST_SAVE_ASSIGNMENT_DETAILS_NEW)
    Call<ActivityResponse> saveAssignmentData_new(
            @PartMap() Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> inside_photo,
            @Part MultipartBody.Part gst_photo,
            @Part MultipartBody.Part pan_photo);

    @GET(Constant.K_FIND_SATHI_STATUS)
    Call<AssignmentStatusCheckResponse> getrBMStatus(@Query("rbm_id") String rbm_id);

    @GET(Constant.K_CHECK_DUPLICATE_REFERENCE)
    Call<AssignmentStatusCheckResponse> getCheckDuplicateRefrence(@Query("phone") String phone);

    @POST(Constant.K_POST_UPDATE_STATUS)
    @Headers({"Content-Type: application/json"})
    Call<update_status_rbmlist_response> updateStatus(@Body update_status_rbmlist_request updateResponse);

    @GET(Constant.K_SEARCH_SATHI_RECORDS)
    Call<SearchSathiRecordResponse> getSearchSathiRecords(@Query("id") String phone);

    @GET(Constant.K_GET_DEMAND_PARTNER_NAME_SATHI)
    Call<DemandPartnerNameResponse> getDemandPartnerNameforSathi(@Query("mobile") String phone_number);

    @GET(Constant.GET_DOCUMENT_BY_USER)
    Call<DocumentByUserModel> getDocbyUser(@Query("user_id") String user_id);
}