package baktiyar.com.poputkakg.ui.map

import baktiyar.com.poputkakg.model.Rout
import com.google.android.gms.maps.GoogleMap

interface MapContract {
    interface View{
        fun onSuccessDrawList(list:MutableList<Rout>)
        fun onSuccessMapReady(map:GoogleMap)
    }
    interface Presenter{
        fun initMap()
        fun configurateUiMap()
        fun showDialogAlert()
        fun drawList(list:MutableList<Rout>)
        fun getMyLocation()
    }
}