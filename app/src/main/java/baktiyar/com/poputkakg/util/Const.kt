package baktiyar.com.poputkakg.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.support.design.widget.Snackbar
import baktiyar.com.poputkakg.R


class Const {
    companion object {
        const val URL = "http://46.101.236.211:3333/api/v1/"

        const val INTENT_PROFILE_INFO = "com.poputka.profileinfo"


        const val MAP_START = 0
        const val MAP_END = 1
        const val PREFS_FILENAME = "baktiyar.com.poputka_prefs"

        const val PREFS_CHECK_TOKEN = "baktiyar.com.poputka_token_prefs"
        const val PREFS_CHECK_USER_ID = "baktiyar.com.poputka_user_id_prefs"
        const val PREFS_CHECK_IS_DRIVER = "baktiyar.com.poputka_is_driver_prefs"
        const val TOKEN_PREFIX: String = "Token "
        const val MAP_ADDITIONAL_POINTS = 2



        const val MAP_RESULT = "map_result"
        const val MAP_LOCATION = "location"
        const val MAP_POINT_RESULT = "points"
        const val MAP_POINTS_ADDRESS = "pointAddressses"
        const val FIREBASE_TOKEN = "FirebaseToken"

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

        fun progressIsShowing(isShowing: Boolean, activity: Activity) {
            var progressBar: ProgressDialog? = null
            if (isShowing) {
                if (progressBar == null) {
                    progressBar = ProgressDialog(activity)
                    progressBar.setTitle(R.string.loading)
                    progressBar.show()
                }
            } else {
                if (progressBar != null && progressBar.isShowing) progressBar!!.dismiss()
                progressBar = null
            }

        }
    }
}