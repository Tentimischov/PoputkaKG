package baktiyar.com.poputkakg.ui.user_short_dialog

import android.content.Context
import baktiyar.com.poputkakg.model.DealBody
import baktiyar.com.poputkakg.model.Point
import com.google.android.gms.maps.model.LatLng
import okhttp3.ResponseBody

interface MarkerClickContract{
    interface View{
        fun onSuccessOfferDeal(dealUser: DealBody?)
        fun onError(message:String)
        fun onFailureOfferDeal(body:ResponseBody?)
        fun onSuccessGetGoogleUrl(url:String)
    }
    interface Presenter{
        fun offerDeal(routId:Int?,context: Context)
        fun getUrlToTheGoogleDirectionsApi(points:ArrayList<Point>, startLatitude: String?, startLongitude: String?,
                                           endLatitude: String?, endLongitude: String?)
    }
}