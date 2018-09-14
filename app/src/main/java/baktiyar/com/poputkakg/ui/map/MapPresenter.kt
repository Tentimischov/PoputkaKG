package baktiyar.com.poputkakg.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.R.id.ivMyLocation
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.util.LocationsManager
import baktiyar.com.poputkakg.util.Permissions
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MapPresenter (val view:MapContract.View,context:
Context,activity: AppCompatActivity,resources: Resources,
                    supportFragmentManager: FragmentManager):MapContract.Presenter,
        OnMapReadyCallback,GoogleMap.OnMarkerClickListener,LocationListener ,GoogleMap.OnMyLocationClickListener{


    private lateinit var location:Location
    private var mContext = context
    private var mGoogleMap :GoogleMap?= null
    private var mActivity = activity
    private var mResources = resources
    private var mSupportMapFragment = supportFragmentManager
    private var mapCircle:Circle? = null
    private var mapMarker:Marker? = null



    override fun showDialogAlert() {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    LocationsManager.turnOnGPSLOcation(mActivity)
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.dismiss()
                }
            }
        }

        val builder = AlertDialog.Builder(mContext)
        builder.setMessage(mResources.getString(R.string.turn_on_gps)).setPositiveButton(mResources.getString(R.string.yes), dialogClickListener)
                .setNegativeButton(mResources.getString(R.string.no), dialogClickListener).show()
    }

    override fun onMyLocationClick(p0: Location) {
        location = p0

    }
    override fun onLocationChanged(location: Location?) {
        this.location = location!!
        val center = CameraUpdateFactory.newLatLng(LatLng(location.latitude,location.longitude))
        mGoogleMap!!.moveCamera(center)
        val zoom = CameraUpdateFactory.zoomTo(16f)
        mGoogleMap!!.animateCamera(zoom)
        getMyLocation()
    }
    override fun getMyLocation() {
        if(mapCircle != null){
            mapCircle!!.remove()
        }
        if(mapMarker != null){

            mapMarker!!.remove()
        }
        val latlng = LatLng(mGoogleMap!!.myLocation.latitude,mGoogleMap!!.myLocation.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng,18f)
        mapMarker = mGoogleMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.my_location))
                .title("my Location").anchor(0.5f,0.5f).position(latlng))
        mapCircle = mGoogleMap!!.addCircle(getCircle(latlng))
        mGoogleMap!!.animateCamera(cameraUpdate)

    }
    private fun getCircle(latLng: LatLng): CircleOptions {
        val circle = CircleOptions().center(latLng)
                .radius(500.0)
                .fillColor(Color.argb(20,41,104,226))
                .strokeColor(Color.BLUE)
                .strokeWidth(0.2f)
        return circle
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0!!
        if(Permissions.iPermissionLocation(mActivity)) {
            initializeMapSettings(mSupportMapFragment,mResources,mContext,mGoogleMap!!)
        }
        view.onSuccessMapReady(mGoogleMap!!)
        configurateUiMap()

        // По умолчанию Ориентир Бишкек
        val startLatLng = LatLng(42.8746, 74.5698)
        val camPos = CameraPosition.Builder().target(startLatLng).zoom(16f).build()
        mGoogleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(camPos))
        mGoogleMap!!.isMyLocationEnabled = true
        mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = false


    }
    @SuppressLint("MissingPermission")
    override fun configurateUiMap() {
        mGoogleMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mGoogleMap!!.setOnMarkerClickListener(this)
        val languageToLoad = "ru"
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            config.setLocale(locale)
            mContext.createConfigurationContext(config)
        } else {

            config.locale = locale
            mResources.updateConfiguration(config, mResources.displayMetrics)
            }
        }

    override fun initMap() {
        val fragment = mSupportMapFragment.findFragmentById(R.id.map) as SupportMapFragment
        fragment.getMapAsync(this)

    }

    @SuppressLint("ResourceType")
    fun initializeMapSettings(supportFragmentManager: android.support.v4.app.FragmentManager, resources: Resources,
                              context: Context, googleMap: GoogleMap) {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val zoomControls: View = mapFragment.view!!.findViewById(0x1)
        if (zoomControls != null) {
            moveZoomButtonsToTop(zoomControls, resources)
            val success = googleMap!!.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(context, R.raw.style_json))
            if (!success) {
                Log.d("STYLE: ", "Style parsing failed")
            }
        }
    }
    override fun onMarkerClick(p0: Marker?): Boolean {
        return true
    }


    private fun moveZoomButtonsToTop(zoomControls: View, resources: Resources) {
        val params: RelativeLayout.LayoutParams = zoomControls.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        val margin: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
        params.setMargins(margin, 80, margin, margin)

    }

  override  fun drawList(list: MutableList<Rout>) {
            mGoogleMap!!.clear()
            list.forEach { data ->

                if (data.startLatitude != null && data.startLongitude != null) {
                    val bitmap = getBitmap(R.mipmap.ic_driver_marker)
                    val latLng = LatLng(java.lang.Double.parseDouble(data.startLatitude!!), java.lang.Double.parseDouble(data.startLongitude!!))
                    mGoogleMap!!.addMarker(MarkerOptions()
                            .title(data.owner)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .anchor(0.0f, 1.0f)
                            .position(latLng)).tag = data
                }

            }
      view.onSuccessDrawList(list)

        }
    private fun getBitmap(from: Int): Bitmap {
        val height = 80
        val width = 80
        val bitmapDraw: BitmapDrawable = mResources.getDrawable(from) as BitmapDrawable
        val bitmap = bitmapDraw.bitmap
        return Bitmap.createScaledBitmap(bitmap,width,height,false)


    }
    }

