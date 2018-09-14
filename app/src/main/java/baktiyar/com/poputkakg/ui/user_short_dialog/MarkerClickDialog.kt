package baktiyar.com.poputkakg.ui.user_short_dialog

import android.annotation.SuppressLint
import android.app.DialogFragment
import android.content.ComponentCallbacks2
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.DealBody
import baktiyar.com.poputkakg.model.Point
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.ui.suggestions.SuggestionActivity
import baktiyar.com.poputkakg.ui.suggestions.SuggestionAdapter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.dialog_marker_click.*
import kotlinx.android.synthetic.main.dialog_marker_click.view.*
import okhttp3.ResponseBody

@SuppressLint("ValidFragment")
class MarkerClickDialog(currentMarkerRout: Rout, mMap: GoogleMap?) : DialogFragment(),MarkerClickContract.View {

    private lateinit var mPresenter : MarkerClickContract.Presenter
    private val mRout = currentMarkerRout
    private val googleMap = mMap

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.dialog_marker_click,container,false)
        setFillMarkerDialogInformation(view,mRout)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
      override fun onStart() {
        super.onStart()
        val dialog = dialog
        if(dialog != null && dialog.window != null){
            dialog.window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window.setBackgroundDrawableResource(R.color.prozrachniy)
            dialog.window.setDimAmount(0f)

        }
        mPresenter = MarkerClickPresenter(this)
    }

    private fun setFillMarkerDialogInformation(view: View,currentMarkerRout: Rout) {
        if(currentMarkerRout != null){
            val name = currentMarkerRout.name!!.split(" ")

            view.tvName.text = name[0]
            view.tvType.text  = when(currentMarkerRout.isDriver){
                true-> "Водитель"
                else-> "Пассажир"
            }
            view.tvFrom.text = currentMarkerRout.startAddress
            view.tvTo.text = currentMarkerRout.endAddress
            view.tvTime.text = currentMarkerRout.startTime.toString()
            view.btnDeal.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    mPresenter.offerDeal(currentMarkerRout.id,context)
                }
            }
        }
    }

    private fun init(){
        val window = dialog.window
        window.requestFeature(Window.FEATURE_NO_TITLE)
        btnShowPath.setOnClickListener {
            getUrlAddress(mRout.points!!,mRout.startLatitude,mRout.startLongitude,mRout.endLatitude,mRout.endLongitude)
            for (i  in 0 until mRout.points!!.size){
                val bitmap = getBitmap(R.mipmap.to)
                googleMap!!.addMarker(MarkerOptions()
                        .title("")
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .anchor(0.5f,0.5f)
                        .position(LatLng(mRout.points!![i].lat!!.toDouble(),mRout.points!![i].lon!!.toDouble())))
            }
            dialog.dismiss()
            }
        btnCall.setOnClickListener {
            val intent = Intent(activity,SuggestionActivity::class.java)
            startActivity(intent)
        }
        }
    private fun getBitmap(from: Int): Bitmap {
        val height = 30
        val width = 30
        val bitmapDraw: BitmapDrawable = resources.getDrawable(from) as BitmapDrawable
        val bitmap = bitmapDraw.bitmap
        return Bitmap.createScaledBitmap(bitmap,width,height,false)


    }
    private fun getUrlAddress(points :ArrayList<Point>, startLatitude: String?, startLongitude: String?, lat: String?, lon: String?){
         mPresenter.getUrlToTheGoogleDirectionsApi(points,startLatitude,startLongitude,lat,lon)

    }
    override fun onSuccessGetGoogleUrl(url: String) {
        val fetchUrl = FetchUrl(googleMap)
        fetchUrl.execute(url)
    }
    override fun onFailureOfferDeal(body: ResponseBody?) {
    }
    override fun onSuccessOfferDeal(dealUser: DealBody?) {
        view.btnDeal.isClickable = false
        view.btnDeal.setBackgroundResource(R.drawable.bg_round_gray)
    }
    override fun onError(message: String) {
    }



}



