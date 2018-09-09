package baktiyar.com.poputkakg.ui.user_short_dialog

import android.annotation.SuppressLint
import android.app.DialogFragment
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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.dialog_marker_click.*
import kotlinx.android.synthetic.main.dialog_marker_click.view.*
import okhttp3.ResponseBody

@SuppressLint("ValidFragment")
class MarkerClickDialog(currentMarkerRout: Rout, mMap: GoogleMap?) : DialogFragment(),MarkerClickContract.View {
    override fun onFailureOfferDeal(body: ResponseBody?) {
    }


    override fun onSuccessOfferDeal(dealUser: DealBody?) {

        view.btnDeal.isClickable = false
        view.btnDeal.setBackgroundResource(R.drawable.bg_round_gray)

    }

    override fun onError(message: String) {
    }


    private lateinit var mPresenter : MarkerClickContract.Presenter
    private val mRout = currentMarkerRout
    private val googleMap = mMap
    private fun setFillMarkerDialogInformation(view: View,currentMarkerRout: Rout) {
        if(currentMarkerRout != null){
        val name = currentMarkerRout.name!!.split(" ")

        view.name.text = name[0]
            view.type.text  = when(currentMarkerRout.isDriver){
                true-> "Водитель"
                else-> "Пассажир"
            }
            view.startAddress.text = currentMarkerRout.startAddress
            view.targetAddress.text = currentMarkerRout.endAddress
            view.time.text = currentMarkerRout.startTime.toString()
            view.btnDeal.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    mPresenter.offerDeal(currentMarkerRout.id,context)
                }
            }
            }
        }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if(dialog != null && dialog.window != null){
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        }
        mPresenter = MarkerClickPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.dialog_marker_click,container,false)


        setFillMarkerDialogInformation(view,mRout)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init(){
        val window = dialog.window
        window.requestFeature(Window.FEATURE_NO_TITLE)
        btnShowPath.setOnClickListener {
            val url = getUrlAddress(mRout.points!!,mRout.startLatitude,mRout.startLongitude,mRout.endLatitude,mRout.endLongitude)
            val parser = DownloadAsyncTask(googleMap)
            for (i  in 0 until mRout.points!!.size){
                googleMap!!.addMarker(MarkerOptions()
                        .title("")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.to))
                        .anchor(0.0f,1.0f)
                        .position(LatLng(mRout.points!![i].lat!!.toDouble(),mRout.points!![i].lon!!.toDouble())))
            }
            parser.execute(url)
            dialog.dismiss()
            }
        }
    private fun getUrlAddress(points :ArrayList<Point>, startLatitude: String?, startLongitude: String?, lat: String?, lon: String?): Any? {
        return getUrlToTheGoogleDirectionsApi(points,startLatitude,startLongitude,lat,lon)
    }

    private fun getUrlToTheGoogleDirectionsApi(points:ArrayList<Point>,startLatitude: String?, startLongitude: String?,
                                               endLatitude: String?, endLongitude: String?): String? {
        val strOrigin = "origin="+startLatitude+","+startLongitude
        val strDest  ="destination="+endLatitude+","+endLongitude
        val sensor = "sensor=false"
        val mode ="mode=driving"
        var  waypoints ="waypoints=optimize:true|"+points.get(0).lat+","+points.get(0).lon+"|"+
                    points.get(1).lat+","+points.get(1).lon
        if(points.size == 3)
            waypoints = waypoints +"|"+points.get(2).lat+","+points.get(2).lon
        val parameters = strOrigin+"&"+strDest+"&"+sensor+"&"+waypoints+"&"+mode
        val output = "json"
        val url:String? = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters
        Log.d("LINK: ",url)

        return url

    }
    }



