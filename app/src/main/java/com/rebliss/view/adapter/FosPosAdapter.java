package com.rebliss.view.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rebliss.utils.App;
import com.rebliss.R;
import com.rebliss.data.perf.MySingleton;
import com.rebliss.domain.constant.Constant;
import com.rebliss.domain.model.ErrorBody;
import com.rebliss.domain.model.approvedecline.ApproveRequest;
import com.rebliss.domain.model.approvedecline.ApproveResponce;
import com.rebliss.domain.model.fospostest.Data;
import com.rebliss.presenter.helper.DisplaySnackBar;
import com.rebliss.presenter.helper.Logout;
import com.rebliss.presenter.helper.Network;
import com.rebliss.presenter.helper.ShowHintOrText;
import com.rebliss.presenter.helper.TextUtil;
import com.rebliss.presenter.retrofit.ApiClient;
import com.rebliss.presenter.retrofit.ApiInterface;
import com.rebliss.view.activity.ActivityCP;
import com.rebliss.view.activity.ActivityFOS;
import com.rebliss.view.activity.ActivityPOS;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FosPosAdapter extends BaseAdapter {

    private Context context;
    private List<Data> allGroupsList;
    private KProgressHUD kProgressHUD;
    private MySingleton mySingleton;
    private DisplaySnackBar displaySnackBar;
    String type;
    private AlertDialog dialog;
    private EditText forgotemail;
    private TextView send, cancel, title;
    private Network network;
    public FosPosAdapter(Context context, List<com.rebliss.domain.model.fospostest.Data> allGroupsList, String type) {
        this.context = context;
        this.allGroupsList = allGroupsList;
        this.type = type;
        mySingleton = new MySingleton(context);
        network = new Network();
        displaySnackBar = new DisplaySnackBar(context);
    }


    @Override
    public int getCount() {
        return allGroupsList.size();
    }

    @Override
    public Object getItem(int position) {
        return allGroupsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.get_user_challange_row, null);
            holder = new ViewHolder();
            holder.profile_image = convertView.findViewById(R.id.profile_image);
            //holder.imgCall = convertView.findViewById(R.id.imgCall);
            //holder.imgEmail = convertView.findViewById(R.id.imgEmail);
            holder.textName = convertView.findViewById(R.id.textName);
            holder.textPhone = convertView.findViewById(R.id.textPhone);
            holder.textEmail = convertView.findViewById(R.id.textEmail);
            holder.textFirmName = convertView.findViewById(R.id.textFirmName);
            holder.textStatus = convertView.findViewById(R.id.textStatus);
            holder.new_wrigl = convertView.findViewById(R.id.new_wrigl);
            holder.text_accept = convertView.findViewById(R.id.text_accept);
            holder.text_decline = convertView.findViewById(R.id.text_decline);
            holder.imgStatus = convertView.findViewById(R.id.imgStatus);
            holder.imgCall = convertView.findViewById(R.id.imgCall);
            holder.imgSms = convertView.findViewById(R.id.imgSms);
            holder.imgMail = convertView.findViewById(R.id.imgMail);
            holder.textDeclineMessage = convertView.findViewById(R.id.textDeclineMessage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textName.setTypeface(App.LATO_REGULAR);
        holder.textPhone.setTypeface(App.LATO_REGULAR);
        holder.textEmail.setTypeface(App.LATO_REGULAR);
        holder.textFirmName.setTypeface(App.LATO_REGULAR);
        holder.textStatus.setTypeface(App.LATO_REGULAR);
        holder.text_decline.setTypeface(App.LATO_REGULAR);
        holder.text_accept.setTypeface(App.LATO_REGULAR);
        holder.textDeclineMessage.setTypeface(App.LATO_REGULAR);

        if (allGroupsList.get(position) != null) {
            if (allGroupsList.get(position).getProfileVerified().equalsIgnoreCase("0")) {
                holder.text_accept.setVisibility(View.GONE);
                holder.text_decline.setVisibility(View.GONE);
                if ((!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getDeclineMessage()))) {
                    holder.textStatus.setText("Status : Profile Decline");
                    holder.imgStatus.setImageResource(R.drawable.multiply);
                    holder.textDeclineMessage.setVisibility(View.VISIBLE);
                    holder.textDeclineMessage.setText("Remark:" +ShowHintOrText.GetExpendString(allGroupsList.get(position).getDeclineMessage()));
                }
                else {
                    holder.textStatus.setText("Status : Profile Pending");
                    holder.textDeclineMessage.setVisibility(View.GONE);
                    holder.imgStatus.setImageResource(R.drawable.exclamation_mark);
                }
            }
            else if (allGroupsList.get(position).getProfileVerified().equalsIgnoreCase("1")) {
                holder.textStatus.setText("Status : Profile Approved");
                holder.imgStatus.setImageResource(R.drawable.checked);
                holder.text_accept.setVisibility(View.GONE);
                holder.textDeclineMessage.setVisibility(View.GONE);

                holder.text_decline.setVisibility(View.GONE);

            }
            else if (allGroupsList.get(position).getProfileVerified().equalsIgnoreCase("2"))
            {

                    holder.imgStatus.setImageResource(R.drawable.exclamation_mark);
                    holder.text_accept.setVisibility(View.GONE);
                    holder.text_decline.setVisibility(View.GONE);
                    holder.textStatus.setText("Status : Profile Pending");
            }
            else
                {
                    Log.e("TAG", "getView: else" );
                }

            Log.i("myyyyyyy", allGroupsList+"");
            holder.textName.setText(((!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getFirstName()) ? allGroupsList.get(position).getFirstName() : "")) + " " +
                    ((!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getLastName()) ? allGroupsList.get(position).getLastName() : "")));
            holder.textPhone.setText("Phone :" + (!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getPhoneNumber()) ? allGroupsList.get(position).getPhoneNumber() : ""));
            if (!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getUserDetail().getPersonalEmailId())) {
                holder.textEmail.setText("Email :" + allGroupsList.get(position).getUserDetail().getPersonalEmailId());
            }
            if (!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getUserDetail().getCpFirmName())) {
                holder.textFirmName.setText("Firm Name :" + allGroupsList.get(position).getUserDetail().getCpFirmName());
            }
            if (!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getImgPath())) {

            }

            holder.text_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getId())) {
                    }
                }
            });

            holder.text_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getId())) {
                    }
                }
            });
            holder.textDeclineMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtil.isStringNullOrBlank(allGroupsList.get(position).getId())) {
                        showErrorSimpleAlertDialog(Constant.TITLE, allGroupsList.get(position).getDeclineMessage());

                    }
                }
            });

            holder.imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isCallPermissionGranted() && allGroupsList.get(position).getPhoneNumber() != null){
                        actionCall(allGroupsList.get(position).getPhoneNumber());
                    }
                }
            });
            holder.imgSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isSMSPermissionGranted() && allGroupsList.get(position).getPhoneNumber() != null){
                        sendSms(allGroupsList.get(position).getPhoneNumber(),"");
                    }
                }
            });
            holder.imgMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(allGroupsList.get(position).getUserDetail().getOfficialEmailId() != null){
                        sendMail(allGroupsList.get(position).getUserDetail().getOfficialEmailId());
                    }
                }
            });

        }
        return convertView;
    }


    private class ViewHolder {

        ImageView profile_image, imgEmail, imgStatus, imgCall, imgSms, imgMail;
        private TextView textName, textPhone, textEmail, textStatus, text_decline, text_accept, textDeclineMessage, textFirmName;
        private RelativeLayout new_wrigl;
    }

    private void showSimpleAlertDialog(String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setTitleText(Constant.TITLE)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if (type.equalsIgnoreCase("FOS")) {
                            ((ActivityFOS)context).reLoadData();
                        } else if (type.equalsIgnoreCase("POS")) {
                            ((ActivityPOS)context).reLoadData();
                        } else if (type.equalsIgnoreCase("CP")) {
                            ((ActivityCP)context).reLoadData();
                        }
                    }
                })
                .show();
    }

    private void showWarningSimpleAlertDialog(String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                    }
                })
                .show();
    }
    private void showErrorSimpleAlertDialog(String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                    }
                })
                .show();
    }

    public void callAcceptAccount(String userId) {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(context.getResources().getColor(R.color.progressbar_color))
                .show();


        ApproveRequest approveRequest = new ApproveRequest();
        approveRequest.setUser_id(userId);
        Gson gson = new Gson();
        String json = gson.toJson(approveRequest, ApproveRequest.class);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ApproveResponce> call = apiService.postAccountApprove(mySingleton.getData(Constant.TOKEN_BASE_64), userId);
        call.enqueue(new Callback<ApproveResponce>() {
            @Override
            public void onResponse(Call<ApproveResponce> call, Response<ApproveResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), ApproveResponce.class);
                                    Log.i("json", "json " + json);
                                    showSimpleAlertDialog(response.body().getData().getMessage());
                                } else {
                                    showWarningSimpleAlertDialog(response.body().getData().getMessage(), "Data not found");

                                }
                            } else if (response.body().getStatus() == 0) {
                                showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getMessage());
                            }
                            callDisplayErrorCode(response.code(), "");
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);

                        // Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getMessage().contains("invalid")) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ApproveResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }
    public void callDeclineAccount(String userId, String Message) {

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setWindowColor(context.getResources().getColor(R.color.progressbar_color))
                .show();


        ApproveRequest approveRequest = new ApproveRequest();
        approveRequest.setUser_id(userId);
        approveRequest.setDecline_message(Message);
        Gson gson = new Gson();
        String json = gson.toJson(approveRequest, ApproveRequest.class);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<ApproveResponce> call = apiService.postAccountDecline(mySingleton.getData(Constant.TOKEN_BASE_64), approveRequest);
        call.enqueue(new Callback<ApproveResponce>() {
            @Override
            public void onResponse(Call<ApproveResponce> call, Response<ApproveResponce> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (response.code() >= 200 && response.code() < 700) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                if (response.body().getData() != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(response.body(), ApproveResponce.class);
                                    Log.i("json", "json " + json);
                                    showSimpleAlertDialog(response.body().getData().getMessage());
                                } else {
                                    showWarningSimpleAlertDialog(response.body().getData().getMessage(), "Data not found");

                                }
                            } else if (response.body().getStatus() == 0) {
                                showWarningSimpleAlertDialog(response.body().getData().getMessage(), response.body().getData().getMessage());
                            }
                            callDisplayErrorCode(response.code(), "");
                        }
                    } else {
                        displaySnackBar.DisplaySnackBar(Constant.UNEXPECTED, Constant.TYPE_ERROR);

                    }
                } else {

                    try {
                        ErrorBody errorBody = new ErrorBody();
                        Gson gson = new Gson();
                        errorBody = gson.fromJson(response.errorBody().string(), ErrorBody.class);
                        callDisplayErrorCode(Integer.parseInt(errorBody.getStatus()), errorBody.getMessage());
                        displaySnackBar.DisplaySnackBar(errorBody.getMessage(), Constant.TYPE_ERROR);

                        // Toast.makeText(context, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        if (errorBody.getName().equalsIgnoreCase(context.getString(R.string.unAuthorisedUser))) {
                            Logout.Login(context);
                        }
                    } catch (Exception e) {
                        displaySnackBar.DisplaySnackBar(Constant.ERROR_INVALID_JSON, Constant.TYPE_ERROR);

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ApproveResponce> call, Throwable t) {
                kProgressHUD.dismiss();
                if (t != null && (t instanceof IOException || t instanceof SocketTimeoutException
                        || t instanceof ConnectException || t instanceof NoRouteToHostException
                        || t instanceof SecurityException)) {
                    displaySnackBar.DisplaySnackBar(Constant.NETWIRK_ERROR, Constant.TYPE_ERROR);

                }
            }
        });
    }

    private void callDisplayErrorCode(int statusCode, String message) {
        String Mmessage = message;
        if (statusCode == 400) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_400;
            }
        }
        if (statusCode == 401) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_401;
            }
        }
        if (statusCode == 403) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_403;
            }
        }
        if (statusCode == 404) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_404;
            }
        }
        if (statusCode == 405) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_405;
            }
        }
        if (statusCode == 500) {
            if (message.length() <= 0) {
                Mmessage = Mmessage + Constant.ERROR_500;
            }
        }
        Log.i("", "response " + statusCode + " " + Mmessage);
    }

    private void actionCall(String caontactNumber){
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+caontactNumber));
                context.startActivity(callIntent);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+caontactNumber));
            context.startActivity(callIntent);
        }
    }

    private void sendSms(String numberToSend, String message){
        String smsNumber = numberToSend;
        String smsText = message;

        Uri uri = Uri.parse("smsto:" + smsNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", smsText);
        context.startActivity(intent);
    }


    private void sendMail(String email){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/html");
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches) {
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
                best = info;
                break;
            }
        }
        if (best != null) {
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        }
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Team reBLISS");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        context.startActivity(intent);
    }
    public boolean isCallPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted");
                return true;
            } else {
                Log.v("permission", "Permission is revoked");
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.CALL_PHONE
                }, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted");
            return true;
        }
    }
    public boolean isSMSPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission", "Permission is granted");
                return true;
            } else {
                Log.v("permission", "Permission is revoked");
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.SEND_SMS
                }, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission", "Permission is granted");
            return true;
        }
    }
}
