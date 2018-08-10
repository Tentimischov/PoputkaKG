package baktiyar.com.poputkakg.ui.main

import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.FileLog
import baktiyar.com.poputkakg.util.ForumService
import retrofit2.Call
import retrofit2.Response

class MainPresenter(var mView: MainContract.View, var mService: ForumService) : MainContract.Presenter {

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