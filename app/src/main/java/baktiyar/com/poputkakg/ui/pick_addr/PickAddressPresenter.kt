package baktiyar.com.poputkakg.ui.pick_addr

import android.location.Geocoder
import android.text.TextUtils
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import java.io.IOException


class PickAddressPresenter(private val mView: PickAddressContract.View?,
                           private val mGeoCoder: Geocoder) : PickAddressContract.Presenter {

    override fun getAddress(latLng: LatLng?) {
        try {
            val data = mGeoCoder.getFromLocation(latLng!!.latitude, latLng.longitude, 1)
            var currentAddress = ""
            if (data.size > 0) {
                for (i in 0 until data[0].maxAddressLineIndex) {
                    currentAddress += data[0].getAddressLine(i) + if (i == 0) "\n" else " "
                }
                if (TextUtils.isEmpty(currentAddress) && data[0].getAddressLine(0) != null)
                    currentAddress = data[0].getAddressLine(0)
            }
            if (isViewAttached()) mView!!.setAddress(currentAddress)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun getPlaces(places: AutocompletePrediction, mGoogleApiClient: GoogleApiClient) {
        val placeId = places.placeId
        val placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId)
        placeResult.setResultCallback { place ->
            if (!place.status.isSuccess) {
                place.release()
                return@setResultCallback
            }
            if (isViewAttached()) mView!!.moveMap(place.get(0).latLng)
            place.release()
        }
    }

    private fun isViewAttached() = mView != null
}