package com.rebliss.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.rebliss.R
import com.rebliss.domain.model.*
import com.rebliss.domain.model.assignment_review.AssignmentModel
import com.rebliss.view.adapter.EditRbmAdapter
import com.rebliss.view.adapter.OpportunityDetailAdapter
import kotlinx.android.synthetic.main.activity_edit_rbm_detail.*
import kotlinx.android.synthetic.main.activity_opportunity_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditRbmDetailActivity : BaseActivity() {
    private var categoryid: Int = 0
    private var type_of_doc: String = ""
    private var type_of_doc_work: String = ""
    private var sathi_code: String? = ""
    private var activity_id: String? = ""
    private var activity_id_s: String? = ""
    var allCategoryList: List<AssignmentModel.Data.All_groups>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(R.layout.activity_edit_rbm_detail)

        call_rbMDetail()
        call_assignmentDetail()
        initAdapter()

        icBack.setOnClickListener { onBackPressed() }

        btnEditRbm.setOnClickListener {
            startActivity(Intent(this,
                MerchantActivity::class.java).putExtra("rbm_sathhii", sathi_code)
                .putExtra("comingfrom", "editRbm"))
        }
        btnEditSubmit.setOnClickListener {
            startActivity(Intent(this, SearchReblissMerchantActivity::class.java))
            finish()
        }
    }

    private fun initAdapter() {
        rvAssignmentDetail.adapter = EditRbmAdapter(this)
    }

    private fun apiIntegrationforUpdateStatus() {
        kProgressHUD.show()
        val updateStatusRbmlistRequest = update_status_rbmlist_request("1", sathi_code!!)
        val call = apiService.updateStatus(updateStatusRbmlistRequest)
        call.enqueue(object : Callback<update_status_rbmlist_response> {
            override fun onResponse(
                call: Call<update_status_rbmlist_response>,
                response: Response<update_status_rbmlist_response>,
            ) {
                Log.e("TAG", "onResponse: " + response.body())
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                Toast.makeText(this@EditRbmDetailActivity,
                                    "Form submitted successfully",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<update_status_rbmlist_response>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }

    private fun getRefrenceData() {
        kProgressHUD.show()
        val call = apiService.getRefData(sathi_code)
        call.enqueue(object : Callback<getRefrenceDataResponse> {
            override fun onResponse(
                call: Call<getRefrenceDataResponse>,
                response: Response<getRefrenceDataResponse>,
            ) {
                Log.e("TAG", "onResponse: " + response.body())
                kProgressHUD.dismiss()
                if (response.isSuccessful) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body()!!.status == 1) {
                                Log.e("TAG", "onResponse: status 1")
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<getRefrenceDataResponse>, t: Throwable) {
                kProgressHUD.dismiss()
            }
        })
    }

    private fun initView() {
        val bundle = intent?.extras
        sathi_code = bundle?.getString("rbm_sathii").toString()
        activity_id = bundle?.getString("activity_id")
        Log.e("TAG", "initView: " + activity_id)

    }

    private fun call_rbMDetail() {
        val call = apiService.getrBMDetail(sathi_code)
        call.enqueue(object : Callback<rBMDetailResponse> {
            override fun onResponse(
                call: Call<rBMDetailResponse>,
                response: Response<rBMDetailResponse>,
            ) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {
                    if (response.body()?.status == 1 && response.body()?.data != null) {
                        response.body()?.data?.all_groups?.apply {
                            txtEditName.setText(first_name.plus("\t").plus(last_name))
                            if (userDetail.gender == "M") {
                                txtEditGender.setText("Male")
                            } else if (userDetail.gender == "F") {
                                txtEditGender.setText("Female")
                            } else {
                                txtEditGender.setText("Other")
                            }
                            txtEditMobile.setText(phone_number)
                            if (userDetail.dob == null) {
                                userDetail.dob = ""
                            }

                            txtEditAgeLimit.setText(age_range)
                            txtEditEducation.setText(userDetail.education)
                            txtEditOccupation.setText(occupation)
                            txtEditStoreName.setText(fos_shop_name)
                            userDetail.apply {
                                txtEditInsidePhoto.setText(shop_inside_photo)
                                txtEditOutsidePhoto.setText(shop_photo)
                                if (userDetail.address != null) {
                                    txtEditState.setText(address.state)
                                    txtPincodeArea.setText(address.zipcode)
                                    txtEditCity.setText(address.city)
                                    txtEditShopName.setText(address.address1)
                                    txtEditArea.setText(address.address2)
                                    txtEditLandmark.setText(address.land_mark)
                                }
                                if (userDetail.current_service != null) {
                                    txtEditCurrentServices.setText(userDetail.current_service)
                                }
                                if (userDetail.interested_service != null) {
                                    if (userDetail.interested_service == "select interested services") {
                                        llEditServices.visibility = View.GONE
                                    } else {
                                        txtEditInterestedServices.setText(userDetail.interested_service)
                                    }
                                }
                                if (userDetail.personal_email_id != null) {
                                    txtEditKycMail.setText(userDetail.personal_email_id)
                                }

                            }
                            if (userDetail.upload_type_option.equals("0")) {
                                type_of_doc = "aadhar"
                                txtEditSelfDocType.setText(type_of_doc)
                            } else if (userDetail.upload_type_option.equals("1")) {
                                type_of_doc = "Driving License"
                                txtEditSelfDocType.setText(type_of_doc)
                            } else {
                                type_of_doc = "Voter id"
                                txtEditSelfDocType.setText(type_of_doc)
                            }

                            txtEditSelfDocNumber.setText(userDetail.aadhar_no)
                            txtSelfKycImage.setText(userDetail.cp_adhar_proof)
                            if (userDetail.cp_firm_name != null) {
                                if (userDetail.cp_firm_name.equals("0")) {
                                    type_of_doc_work = "aadhar"
                                    txtEditWorkDocType.setText(type_of_doc)
                                } else if (userDetail.cp_firm_name.equals("1")) {
                                    type_of_doc_work = "Driving License"
                                    txtEditWorkDocType.setText(type_of_doc)
                                } else {
                                    type_of_doc_work = "Voter id"
                                    txtEditWorkDocType.setText(type_of_doc)
                                }
                            }
                            if (userDetail.cp_adhar_proof != null) {
                                txtEditWorkDocNumber.setText(userDetail.pan_no)
                            }
                            if (userDetail.firm_pan_proof != null) {
                                txtWorkKycImage.setText(userDetail.id_proof)
                            }

                            if (img_path != null) {
                                txtEditSelfImage.setText(img_path)
                            }
                        }

                    }
                }
            }

            override fun onFailure(call: Call<rBMDetailResponse>, t: Throwable) {
                kProgressHUD.dismiss();
                t.printStackTrace()
            }
        })
    }

    private fun call_assignmentDetail() {
        Log.e("TAG", "call_assignmentDetail: >>>>>" + activity_id)
        val call = apiService.getAssignment(activity_id)

        call.enqueue(object : Callback<AssignmentModel> {
            override fun onResponse(
                call: Call<AssignmentModel>,
                response: Response<AssignmentModel>,
            ) {
                kProgressHUD.dismiss();
                if (response.isSuccessful && response.code() == 200) {

                    if (response.body()?.data != null) {
                        (rvAssignmentDetail.adapter as? EditRbmAdapter)?.updateAdapter(response.body()?.data?.all_groups)
                    }
                } else {
                    Log.e("TAG", "onResponse: elseeeeeeeeeeeee")
                }
            }

            override fun onFailure(call: Call<AssignmentModel>, t: Throwable) {
                kProgressHUD.dismiss();
                Log.e("TAG", "onFailure:LLLLLLL " + t.message)
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MyTaskActivity::class.java))
        finish()
    }
}