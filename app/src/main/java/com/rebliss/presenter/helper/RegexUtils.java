package com.rebliss.presenter.helper;

import java.util.regex.Pattern;

public class RegexUtils {
    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public static boolean isValidIPan(String name) {
        String regexIfsc = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        return Pattern.matches(regexIfsc, name);
    }
}
