package baktiyar.com.poputkakg.ui.offer.create_offer

import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.util.FileLog
import baktiyar.com.poputkakg.util.ForumService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewOfferPresenter(var mView: NewOfferContract.View, var mService: ForumService) : NewOfferContract.Presenter {
    override fun sendOffer(rout: Rout, token: String) {
        mService.createRout(rout, token).enqueue(object : Callback<Rout>{
            override fun onFailure(call: Call<Rout>?, t: Throwable?) {
                mView.onError(t!!.message!!.toString())
                FileLog.e(t.message!!.toString(), t)
            }

            override fun onResponse(call: Call<Rout>?, response: Response<Rout>?) {
                if (response!!.isSuccessful){
                    mView.onSuccessSendOffer(response.body()!!)
                }else{
                    mView.onError(response.message())
                    FileLog.e(response.message())
                }
            }
        })
    }
}