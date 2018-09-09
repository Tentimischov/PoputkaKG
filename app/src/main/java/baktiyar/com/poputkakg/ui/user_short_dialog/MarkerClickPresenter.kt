package baktiyar.com.poputkakg.ui.user_short_dialog

import android.content.Context
import baktiyar.com.poputkakg.StartApplication
import baktiyar.com.poputkakg.model.DealBody
import baktiyar.com.poputkakg.util.Const
import retrofit2.Callback
import retrofit2.Response

class MarkerClickPresenter(var mView:MarkerClickContract.View)
    :MarkerClickContract.Presenter{



    override fun offerDeal(routId: Int?,context: Context) {
        val token = Const.TOKEN_PREFIX+context.getSharedPreferences(Const.PREFS_FILENAME,0).getString(Const.PREFS_CHECK_TOKEN,"null")
        val map = HashMap<String,Int>()
        map.put("route",routId!!)
        StartApplication.INSTANCE.service.offerDeal(token, map).enqueue(
                object : Callback<DealBody> {
                    override fun onFailure(call: retrofit2.Call<DealBody>?, t: Throwable?) {
                    }

                    override fun onResponse(call: retrofit2.Call<DealBody>?, response: Response<DealBody>?) {
                        if(response!!.code() == 406)
                            mView.onFailureOfferDeal(response.errorBody()!!)




                        else
                            mView.onSuccessOfferDeal(response.body()!!)

                        }



                }
        )
    }

}