package com.rebliss.presenter.helper;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

public class ShowHintOrText {
    public static SpannableStringBuilder GetMandatory(String message) {
        String simple = message;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static String GetOptional(String message) {
        String simple = message;
        String colored = " (Optional)";
        return simple+colored;
    }
    public static String GetExpendString(String message) {
        if(message.length()<=10)
            return message;
        String upToNCharacters = message.substring(0, Math.min(message.length(), 10));
        String simple = upToNCharacters;
        String colored = " More...";
        return simple+colored;
    }

}
