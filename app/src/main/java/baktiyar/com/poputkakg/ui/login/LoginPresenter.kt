package baktiyar.com.poputkakg.ui.login

import android.content.Context
import baktiyar.com.poputkakg.model.Login
import baktiyar.com.poputkakg.model.Token
import baktiyar.com.poputkakg.util.FileLog
import baktiyar.com.poputkakg.util.ForumService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart

class LoginPresenter(var mView: LoginContract.View,
                     var mService: ForumService) : LoginContract.Presenter {



    override fun login(login: Login) {
        mService.login(login).enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>?, t: Throwable?) {
                FileLog.e(t!!.message!!.toString(), t)
                mView.onError(t.message!!.toString())
            }

            override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                if (response!!.isSuccessful) {
                    mView.onSuccessLogin(response.body()!!)
                } else {
                    FileLog.e(response.message())
                    mView.onError(response.message())
                }
            }
        })
    }
}