package baktiyar.com.poputkakg.ui.signup

import android.content.Context
import baktiyar.com.poputkakg.StartApplication
import baktiyar.com.poputkakg.model.City
import baktiyar.com.poputkakg.model.ProfileInfo
import baktiyar.com.poputkakg.model.TokenInfo
import baktiyar.com.poputkakg.model.User
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.DeviceNameTools
import baktiyar.com.poputkakg.util.FileLog
import baktiyar.com.poputkakg.util.ForumService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpPresenter(var mView: SignUpContract.View, var mService: ForumService) : SignUpContract.Presenter {





    override fun signUp(user: User) {

        mService.createUser(user).enqueue(object : Callback<ProfileInfo> {
            override fun onFailure(call: Call<ProfileInfo>?, t: Throwable?) {
                mView.onError(t!!.message!!.toString())
                FileLog.e(t.message!!.toString(), t)
            }

            override fun onResponse(call: Call<ProfileInfo>?, response: Response<ProfileInfo>?) {
                if (response!!.isSuccessful) {
                    mView.onSuccessSignUp(response.body()!!)
                } else {
                    mView.onError(response.message())
                    FileLog.e(response.message())
                }
            }
        })

    }

    override fun getCities() {
        mService.getCities().enqueue(object : Callback<List<City>> {
            override fun onFailure(call: Call<List<City>>?, t: Throwable?) {
                mView.onError(t!!.message.toString())
                FileLog.e(t.message!!.toString(), t)
            }

            override fun onResponse(call: Call<List<City>>?, response: Response<List<City>>?) {
                if (response!!.isSuccessful) {
                    mView.onSuccessGetCities(response.body() as MutableList<City>)
                } else {
                    mView.onError(response.message())
                    FileLog.e(response.message())
                }

            }
        })

    }
}