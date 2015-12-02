package com.studioidan.turaco.utils;

import android.widget.EditText;

/**
 * Created by PopApp_laptop on 02/12/2015.
 */
public class TextUtils {

    public static boolean isEditTextEmpty(EditText et) {
        return et.getText().toString().trim().length() == 0;
    }

    public static String getEditTextContent(EditText et) {
        return et.getText().toString().trim();
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
