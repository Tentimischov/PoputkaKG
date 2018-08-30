package baktiyar.com.poputkakg.ui.notificationUtils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import baktiyar.com.poputkakg.util.Const
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class FirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        val preferences = this.getSharedPreferences(Const.PREFS_FILENAME,android.content.Context.MODE_PRIVATE).edit()
        preferences.putString(Const.FIREBASE_TOKEN,refreshedToken)
        preferences.apply()
        preferences.commit()


        Log.d(TAG, "____Refreshed token: "+ refreshedToken!!)
    }
}