package baktiyar.com.poputkakg.ui.user_short_dialog

import android.content.Context
import android.telecom.Call
import baktiyar.com.poputkakg.StartApplication
import baktiyar.com.poputkakg.model.DealBody
import baktiyar.com.poputkakg.ui.offer.create_offer.NewOfferContract
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.ForumService
import baktiyar.com.poputkakg.util.Permissions
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback
import retrofit2.Response
import java.security.AccessControlContext

class MarkerClickPresenter(var mView:MarkerClickContract.View)
    :MarkerClickContract.Presenter{


    override fun offerDeal(routId: Int?,context: Context) {
        val token = Const.TOKEN_PREFIX+context.getSharedPreferences(Const.PREFS_FILENAME,0).getString(Const.PREFS_CHECK_TOKEN,"null")
        StartApplication.INSTANCE.service.offerDeal(token,routId!!,2).enqueue(
                object : Callback<DealBody> {
                    override fun onFailure(call: retrofit2.Call<DealBody>?, t: Throwable?) {
                        mView.onError(t!!.message!!)
                    }

                    override fun onResponse(call: retrofit2.Call<DealBody>?, response: Response<DealBody>?) {
                        mView.OnSuccessOfferDeal(response!!.body()!!)

                    }

                }
        )
    }

}