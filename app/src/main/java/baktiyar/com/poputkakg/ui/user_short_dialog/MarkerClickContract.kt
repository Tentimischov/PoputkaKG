package baktiyar.com.poputkakg.ui.user_short_dialog

import android.content.Context
import baktiyar.com.poputkakg.model.DealBody

interface MarkerClickContract{
    interface View{
        fun OnSuccessOfferDeal(dealUser: DealBody)
        fun onError(message:String)
    }
    interface Presenter{
        fun offerDeal(routId:Int?,context: Context)
    }
}