package com.example.open_uni_providers.utils;
import android.util.Patterns;

import androidx.annotation.Nullable;
public class Validator {
    /// Check if the email is valid
    /// @param email email to validate
    /// @return true if the email is valid, false otherwise
    /// @see Patterns#EMAIL_ADDRESS
    public static boolean isEmailValid(@Nullable String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /// Check if the password is valid
    /// @param password password to validate
    /// @return true if the password is valid, false otherwise
    public static boolean isPasswordValid(@Nullable String password) {
        return password != null && password.length() >= 6;
    }
    public static boolean isIDValid(@Nullable String ID){
        return ID.length() == 9;
    }

    /// Check if the name is valid
    /// @param name name to validate
    /// @return true if the name is valid, false otherwise
    public static boolean isNameValid(@Nullable String name) {
        return name != null && name.length() >= 3;
    }
    public static boolean isWinnerValid(@Nullable String wName) {
        return (wName != null && wName.length() >= 3) || wName.length()==0;
    }
    public static boolean isSubjectValid(@Nullable String sub) {
        return sub != null && sub.length() > 0;
    }
    public static boolean isDateValid(@Nullable String dateP, @Nullable String dateE) {
        if (dateP == null || dateE == null) return false;

        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
            sdf.setLenient(false);

            java.util.Date dExp = sdf.parse(dateE);
            java.util.Date dPub = sdf.parse(dateP);

            if (dExp.before(dPub)) {
                return false;
            }

        } catch (java.text.ParseException e) {
            return false;
        }
        return true;
    }
    public static boolean isContentValid(@Nullable String cont) {
        return cont != null && cont.length() >=20;
    }


}
