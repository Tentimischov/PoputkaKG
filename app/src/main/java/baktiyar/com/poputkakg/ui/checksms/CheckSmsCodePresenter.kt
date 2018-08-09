package baktiyar.com.poputkakg.ui.checksms

import baktiyar.com.poputkakg.model.Token
import baktiyar.com.poputkakg.model.SmsCode
import baktiyar.com.poputkakg.util.FileLog
import baktiyar.com.poputkakg.util.ForumService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckSmsCodePresenter(var mView: CheckSmsCodeContract.View, var mService: ForumService) : CheckSmsCodeContract.Presenter {
    override fun checkSmsCode(smsCode: SmsCode) {
        mView.showProgress()
        mService.checkSmsCode(smsCode).enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>?, t: Throwable?) {
                mView.hideProgress()
                mView.onError(t!!.message!!.toString())
                FileLog.e(t.message!!.toString(), t)
            }

            override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                if (response!!.isSuccessful) {
                    mView.hideProgress()
                    mView.onSuccessCheckSmsCode(response.body()!!)
                } else {
                    mView.hideProgress()
                    mView.onError(response.message())
                    FileLog.e(response.message())
                }
            }

        })
    }
}