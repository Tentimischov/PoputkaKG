package baktiyar.com.poputkakg.ui.profile

import baktiyar.com.poputkakg.model.ProfileInfo
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.FileLog
import baktiyar.com.poputkakg.util.ForumService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(var mView: ProfileContract.View, var mService: ForumService) : ProfileContract.Presenter {
    override fun getProfileInfo(userId: Int, token: String) {
        var fullToken = Const.TOKEN_PREFIX+token

        mService.getProfileInfo(userId, fullToken).enqueue(object : Callback<ProfileInfo>{
            override fun onFailure(call: Call<ProfileInfo>?, t: Throwable?) {
                mView.onError(t!!.message!!.toString())
                FileLog.e(t!!.message!!.toString(), t)
            }

            override fun onResponse(call: Call<ProfileInfo>?, response: Response<ProfileInfo>?) {
                if (response!!.isSuccessful){
                    mView.onSuccessGetProfileInfo(response.body()!!)
                }else{
                    mView.onError(response.message())
                    FileLog.e(response.message())

                }
            }
        })
    }
}