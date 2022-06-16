package com.rebliss.presenter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Pattern;

/**
 * Created by swarajpal on 19-04-2016.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;
    public Pattern p = Pattern.compile("(|^)\\d{6}");
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();
        String str="";
        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.
            String messageBody = smsMessage.getMessageBody();
            //Pass on the text to our listener.
            // to check way to sms IM-WAYSMS
            if((sender.contains("reBLISS") || sender.contains("-reBLIS"))&& messageBody.contains("Your OTP for reBliss")){
                //String hardcoded = "One Time OTP  414161  to verify your mobile.";
//                String[] split = hardcoded.split("One Time OTP  ");
                String[] split = messageBody.split("Your OTP for reBliss is ");
                Log.i("smsData",  split[1]);
               // String[] split2 = split[1].split("  ");

//                String str = messageBody.replaceAll("\\D+","");
               // String str = split2[0];
                String mstr =  split[1].trim();
                Log.i("smsData",  mstr);
                mListener.messageReceived(mstr);
            } else {
                Log.i("messageOtp", sender+"-"+messageBody);
            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
