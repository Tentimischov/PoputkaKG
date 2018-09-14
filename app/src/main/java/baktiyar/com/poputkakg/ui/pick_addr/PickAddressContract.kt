package baktiyar.com.poputkakg.ui.pick_addr

import com.google.android.gms.maps.model.LatLng


interface PickAddressContract {
    interface View{
        fun moveMap(latLng: LatLng?)
        fun setAddress(address: String)
    }

    interface Presenter{
        fun getAddress(latLng: LatLng?)
    }
}