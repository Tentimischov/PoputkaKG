package baktiyar.com.poputkakg.ui.user_short_dialog

import android.content.Context
import android.util.Log
import baktiyar.com.poputkakg.StartApplication
import baktiyar.com.poputkakg.model.DealBody
import baktiyar.com.poputkakg.model.Point
import baktiyar.com.poputkakg.model.polylines.DirectionResult
import baktiyar.com.poputkakg.util.Const
import retrofit2.Call
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




    override fun getUrlToTheGoogleDirectionsApi(points:ArrayList<Point>, startLatitude: String?, startLongitude: String?,
                                               endLatitude: String?, endLongitude: String?){
        val strOrigin = "origin="+startLatitude+","+startLongitude
        val strDest  ="destination="+endLatitude+","+endLongitude
        val sensor = "sensor=false"
        val mode ="mode=driving"
        var  waypoints ="waypoints=optimize:true|"+points.get(0).lat+","+points.get(0).lon+"|"+
                points.get(1).lat+","+points.get(1).lon
        if(points.size == 3)
            waypoints = waypoints +"|"+points.get(2).lat+","+points.get(2).lon
        val parameters = strOrigin+"&"+strDest+"&"+sensor+"&"+waypoints+"&"+mode+"&key=AIzaSyBHZx8zTVBcv_shWAYBPYIpzsRyNenZRVU"
        val output = "json"
        val url:String? = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters
        Log.d("LINK: ",url)
        mView.onSuccessGetGoogleUrl(url!!)

    }

}