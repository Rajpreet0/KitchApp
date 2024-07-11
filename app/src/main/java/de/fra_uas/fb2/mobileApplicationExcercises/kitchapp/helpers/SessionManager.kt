package de.fra_uas.fb2.mobileApplicationExcercises.kitchapp.helpers

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

// BEGIN: Raj
class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    companion object {
        private const val PREFS_NAME = "kitchApp_prefs_userData"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USERNAME = "username"
        private const val KEY_LANGUAGE = "language" // Daria
        private const val KEY_SESSION_EXPIRY = "session_expiry"
        private const val SESSION_DURATION = 24*60*60*1000;
    }

    fun createLoginSession(userData: JSONObject) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_ID, userData.getString("_id"))
        editor.putString(KEY_USERNAME, userData.getString("username"))
        editor.putString(KEY_USER_EMAIL, userData.getString("email"))
        editor.putString(KEY_LANGUAGE, userData.getString("language")) // Daria
        editor.putLong(KEY_SESSION_EXPIRY, System.currentTimeMillis() + SESSION_DURATION)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val sessionExpiry = prefs.getLong(KEY_SESSION_EXPIRY, 0)
        if (isLoggedIn && System.currentTimeMillis() < sessionExpiry) {
            return true
        }
        logout()
        return false
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }
    // END: Raj

    // BEGIN: Daria
    fun getLanguage(): String? {
        return prefs.getString(KEY_LANGUAGE, null)
    }

    fun setLanguage(language: String) {
        val editor = prefs.edit()
        editor.putString(KEY_LANGUAGE, language)
        editor.apply()
    }

    fun setUserName(username: String) {
        val editor = prefs.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }
    // END: Daria

    // BEGIN: Raj
    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    // END: Raj
}