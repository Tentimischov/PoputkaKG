package baktiyar.com.poputkakg.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.support.design.widget.Snackbar


class Const {
    companion object {
        const val URL = "http://165.227.147.84:3333/api/v1/"

        const val INTENT_PROFILE_INFO = "com.poputka.profileinfo"


        const val PREFS_FILENAME = "baktiyar.com.poputka_prefs"

        const val PREFS_CHECK_TOKEN = "baktiyar.com.poputka_token_prefs"
        const val PREFS_CHECK_USER_ID = "baktiyar.com.poputka_user_id_prefs"
        const val TOKEN_PREFIX: String = "Token "

        fun hideKeyboard(activity: Activity) {
            val view: View? = activity.window.currentFocus
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
        }

        fun makeToast(activity: Activity, message: String) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }

        fun makeSnack(message: String, view: View) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show()
        }
    }
}