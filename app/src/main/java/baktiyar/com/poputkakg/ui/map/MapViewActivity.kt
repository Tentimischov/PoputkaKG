package baktiyar.com.poputkakg.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.ui.BaseActivity
import baktiyar.com.poputkakg.ui.user_short_dialog.MarkerClickDialog
import baktiyar.com.poputkakg.util.LocationsManager
import baktiyar.com.poputkakg.util.Permissions
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_suggestion.*
import java.util.*

@Suppress("DEPRECATION")
open class MapViewActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,GoogleMap.OnMarkerClickListener {

    private var mapCircle : Circle? = null
    private var mapMarker :Marker?
            = null
    protected var mMap: GoogleMap? = null

    @SuppressLint("ObsoleteSdkInt")
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


    override fun onMarkerClick(p0: Marker?): Boolean {
        return true
    }


    @SuppressLint("ResourceType")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap
        configurateGoogleMapUi()

        val mapFragment :SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val zoomControls : View = mapFragment.view!!.findViewById(0x1)
        if(zoomControls != null) {
            moveZoomButtonsToTop(zoomControls)
            val success = googleMap!!.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json))
            if(!success){
                Log.d("STYLE: ","Style parsing failed")
            }
        }


        // По умолчанию Ориентир Бишкек
        val startLatLng = LatLng(42.8746, 74.5698)
        val camPos = CameraPosition.Builder().target(startLatLng).zoom(16f).build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(camPos))
        mMap!!.setOnMyLocationButtonClickListener(this)
        if (Permissions.iPermissionLocation(this))
            setMyLocationEnable()
    }

    private fun configurateGoogleMapUi() {
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.setOnMarkerClickListener(this)

        val languageToLoad = "ru"
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            config.setLocale(locale)
            applicationContext.createConfigurationContext(config)
        } else {

            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
        }
        ivMyLocation.setOnClickListener {
            if (LocationsManager.isLocationEnabled(this)) {
                getMyLocation()
            } else {

                val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            LocationsManager.turnOnGPSLOcation(this)
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                            dialog.dismiss()
                        }
                    }
                }

                val builder = AlertDialog.Builder(this)
                builder.setMessage(resources.getString(R.string.turn_on_gps)).setPositiveButton(resources.getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(resources.getString(R.string.no), dialogClickListener).show()
            }


        }
    }

    private fun getMyLocation() {
        if(mMap!!.myLocation != null){
        val latLng = LatLng(mMap!!.myLocation.latitude,mMap!!.myLocation.longitude)
        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,16f)
        mMap!!.animateCamera(cameraUpdate)
            if(mapCircle != null){
                mapCircle!!.remove()
            }
            if(mapMarker != null){

                mapMarker!!.remove()
            }
            mapMarker = mMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.my_location))
                    .title("my Location").anchor(0f,0f).position(LatLng(mMap!!.myLocation.latitude,mMap!!.myLocation.longitude)))
            mapCircle = mMap!!.addCircle(getCircle(LatLng(mMap!!.myLocation.latitude,mMap!!.myLocation.longitude)))

        }
    }

    private fun getCircle(latLng: LatLng): CircleOptions {
        val circle = CircleOptions().center(latLng)
                .radius(500.0)
                .fillColor(Color.argb(20,41,104,226))
                .strokeColor(Color.BLUE)
                .strokeWidth(0.2f)
        return circle
    }


    private fun moveZoomButtonsToTop(zoomControls: View) {
        val params: RelativeLayout.LayoutParams = zoomControls.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        val margin :Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10f,resources.displayMetrics).toInt()
        params.setMargins(margin,80,margin,margin)
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

    protected  fun drawList(list: MutableList<Rout>) {
        if (mMap != null) {
            list.forEach { data ->

                if (data.startLatitude != null && data.startLongitude != null) {
                    val latLng = LatLng(java.lang.Double.parseDouble(data.startLatitude!!), java.lang.Double.parseDouble(data.startLongitude!!))
                    val bitmap = getBitmap(R.mipmap.ic_launcher)
                    mMap!!.addMarker(MarkerOptions()
                            .title(data.owner)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap as Bitmap?))
                            .anchor(0.0f, 1.0f)
                            .position(latLng)).tag = data
                }

            }


            setOnMarkerClickListeners(list)
        }
    }

    private fun getBitmap(from: Int): Any {
        val height = 40
        val width = 40
        val bitmapDraw:BitmapDrawable = resources.getDrawable(from) as BitmapDrawable
        val bitmap = bitmapDraw.bitmap
        return Bitmap.createScaledBitmap(bitmap,width,height,false)


    }

    private fun setOnMarkerClickListeners(list:MutableList<Rout>) {
        mMap!!.setOnMarkerClickListener {
            openCurrentRiderInformationDialogFragment(it,list)
        }
    }

    private fun placeMarkerOnMap(location: LatLng){

        val markerOptions = MarkerOptions().position(location)

        mMap!!.addMarker(markerOptions)
    }


    private  fun openCurrentRiderInformationDialogFragment(it: Marker?, list: MutableList<Rout>): Boolean {
        var currentMarkerRout = Rout()
        for(i in 0..list.size-1){
            if(it!!.title == list.get(i).owner){
                currentMarkerRout = list.get(i)

            }
        }

        val dialog = MarkerClickDialog(currentMarkerRout,mMap)
        dialog.show(fragmentManager,"dialog")
        return true
    }








    }




