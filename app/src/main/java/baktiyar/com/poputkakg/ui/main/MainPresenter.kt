package baktiyar.com.poputkakg.ui.main

import android.content.Context
import baktiyar.com.poputkakg.StartApplication
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.model.TokenInfo
import baktiyar.com.poputkakg.ui.signup.SignUpActivity
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.DeviceNameTools
import baktiyar.com.poputkakg.util.FileLog
import baktiyar.com.poputkakg.util.ForumService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(var mView: MainContract.View, var mService: ForumService) : MainContract.Presenter {

    override fun sendToken(context: Context, activity: MainActivity,
                           firstName: String?, token: String) {
        var bodyBuilder = fillTokenRequest(context,firstName)
        bodyBuilder.setType(MultipartBody.FORM)

        StartApplication.INSTANCE.service.sendFirebaseToken(token,bodyBuilder.build())
                .enqueue(object : Callback<TokenInfo> {
                    override fun onFailure(call: Call<TokenInfo>?, t: Throwable?) {
                        mView.onError(t!!.message!!.toString())
                        FileLog.e(t.message!!.toString(), t)
                    }

                    override fun onResponse(call: Call<TokenInfo>?, response: Response<TokenInfo>?) {
                        if (response!!.isSuccessful) {
                            mView.onSuccessSendToken(response.body()!!)
                        } else {
                            mView.onError(response.message())
                            FileLog.e(response.message())
                        }
                    }

                }
                )

    }

    private fun fillTokenRequest(context: Context, firstName: String?): MultipartBody.Builder {
        val body = MultipartBody.Builder()
        val type = "android"
        val name = DeviceNameTools.getDeviceName()
        val device_id =firstName
        val token = context.getSharedPreferences(Const.PREFS_FILENAME,0).getString(Const.FIREBASE_TOKEN,"null")
        body.addFormDataPart("registration_id",token)
        body.addFormDataPart("type",type)
        body.addFormDataPart("name",name)
        body.addFormDataPart("device_id",device_id)

        return body

    }

    override fun getRoutes(token: String) {
        val tokenPrefix = Const.TOKEN_PREFIX + token
        mService.getRoutes(tokenPrefix).enqueue(object : retrofit2.Callback<List<Rout>> {
            override fun onFailure(call: Call<List<Rout>>?, t: Throwable?) {
                mView.onError(t!!.message!!.toString())
                FileLog.e(t.message!!.toString(), t)
            }

            override fun onResponse(call: Call<List<Rout>>?, response: Response<List<Rout>>?) {
                mView.hideProgress()
                if (response!!.isSuccessful) {
                    mView.onSuccessGetRoutes(response.body()!!)
                } else {
                    FileLog.e(response.message())
                    mView.onError(response.message())
                }
            }
        })
    }
}