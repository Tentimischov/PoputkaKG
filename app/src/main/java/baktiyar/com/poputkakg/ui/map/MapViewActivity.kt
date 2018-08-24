package baktiyar.com.poputkakg.ui.map

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.ui.BaseActivity
import baktiyar.com.poputkakg.ui.user_short_dialog.DownloadAsyncTask
import baktiyar.com.poputkakg.ui.user_short_dialog.MarkerClickDialog
import baktiyar.com.poputkakg.util.Permissions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

open class MapViewActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {


    private var mMap: GoogleMap? = null

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        init()
    }

    private fun init() {
        initGoogleMap()
    }

    private fun initGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("ResourceType")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL

        val mapFragment :SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val zoomControls : View = mapFragment.view!!.findViewById(0x1)
        if(zoomControls != null) {
            val params: RelativeLayout.LayoutParams = zoomControls.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            val margin :Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10f,resources.displayMetrics).toInt()
            params.setMargins(margin,80,margin,margin)

        }
        // По умолчанию Ориентир Бишкек
        val startLatLng = LatLng(42.8746, 74.5698)
        val camPos = CameraPosition.Builder().target(startLatLng).zoom(13f).build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(camPos))
        mMap!!.setOnMyLocationButtonClickListener(this)
        if (Permissions.iPermissionLocation(this))
            setMyLocationEnable()
    }

    override fun onMyLocationButtonClick(): Boolean = false


    @SuppressLint("MissingPermission")
    private fun setMyLocationEnable() {
        mMap!!.isMyLocationEnabled = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permissions.Request.ACCESS_FINE_LOCATION &&
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setMyLocationEnable()
        } else {
            Permissions.iPermissionLocation(this)
        }
    }

    protected fun drawList(list: MutableList<Rout>) {
        if (mMap != null) {
            list.forEach { data ->
                if (data.startLatitude != null && data.startLongitude != null) {
                    val latLng = LatLng(java.lang.Double.parseDouble(data.startLatitude!!), java.lang.Double.parseDouble(data.startLongitude!!))
                    mMap!!.addMarker(MarkerOptions()
                            .title(data.owner)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.driver_marker))
                            .anchor(0.0f, 1.0f)
                            .position(latLng)).tag = data
                }

                if (data.endLatitude!= null && data.endLongitude!= null) {
                    val latLng = LatLng(java.lang.Double.parseDouble(data.endLatitude!!), java.lang.Double.parseDouble(data.endLongitude!!))
                    mMap!!.addMarker(MarkerOptions()
                            .title(data.owner)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.rider_marker))
                            .anchor(0.0f, 1.0f)
                            .position(latLng)).tag = data
                }
            }
            setOnMarkerClickListeners(list)
        }
    }

    private fun setOnMarkerClickListeners(list:MutableList<Rout>) {
        mMap!!.setOnMarkerClickListener {
            openCurrentRiderInformationDialogFragment(it,list)
        }
    }

    private fun openCurrentRiderInformationDialogFragment(it: Marker?, list: MutableList<Rout>): Boolean {
        var currentMarkerRout = Rout()
        for(i in 0..list.size-1){
            if(it!!.title == list.get(i).owner){
                currentMarkerRout = list.get(i)
                 val url = getUrlToTheGoogleDirectionsApi(currentMarkerRout.startLatitude,
                         currentMarkerRout.startLongitude,currentMarkerRout.endLatitude,
                         currentMarkerRout.endLongitude)
                val downloadTask:DownloadAsyncTask= DownloadAsyncTask(mMap)
                downloadTask.execute(url)
                Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()

            }
        }

        //val dialog = MarkerClickDialog(currentMarkerRout)
        //dialog.show(fragmentManager,"dialog")
        return true
    }

    private fun getUrlToTheGoogleDirectionsApi(startLatitude: String?, startLongitude: String?,
                                               endLatitude: String?, endLongitude: String?): String? {
        val strOrigin = "origin="+startLatitude+","+startLongitude
        val strDest  ="destination="+endLatitude+","+endLongitude
        val sensor = "sensoe=false"
        val mode ="mode=driving"
        val parameters = strOrigin+"&"+strDest+"&"+sensor+"&"+mode
        val output = "json"
        val url:String? = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters
        return url

    }


}