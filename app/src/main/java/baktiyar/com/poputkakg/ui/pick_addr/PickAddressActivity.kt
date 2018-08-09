package baktiyar.com.poputkakg.ui.pick_addr

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.util.Const.Companion.MAP_LOCATION
import baktiyar.com.poputkakg.util.Const.Companion.MAP_RESULT
import baktiyar.com.poputkakg.util.LocationsManager
import baktiyar.com.poputkakg.util.Permissions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_pick_addr.*
import java.util.*


class PickAddressActivity : AppCompatActivity(), PickAddressContract.View, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener {

    private var mMap: GoogleMap? = null

    private lateinit var mPresenter: PickAddressPresenter

    private var mAdapter: PlaceAutoCompleteAdapter? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private var mDefaultLocation = LatLng(42.8746, 74.5698)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_addr)

        init()
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()
    }

    override fun onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected)
            mGoogleApiClient!!.disconnect()
        super.onStop()
    }

    private fun init() {
        initGoogleMap()
        initView()
        initApiClient()
        initPresenter()
        initAutoComplete()
    }

    private fun initGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    private fun initApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build()
    }

    private fun initAutoComplete() {
        if (mGoogleApiClient != null) {
            tvAutoCompleteAddress.setOnItemClickListener { parent, view, position, id ->
                mPresenter.getPlaces(mAdapter?.getItem(position)!!, mGoogleApiClient!!)
            }
            mAdapter = PlaceAutoCompleteAdapter(this, mGoogleApiClient, null, null)
            tvAutoCompleteAddress.setAdapter(mAdapter)
        }
    }

    private fun initView() {
        btnSetAddress.setOnClickListener {
            if (!TextUtils.isEmpty(tvAutoCompleteAddress.text)) {
                setPickedAddress((tvAutoCompleteAddress.text.toString()))
            } else {
                showWarningMessage("Type your address")
            }
        }
        btnClearText.setOnClickListener {
            tvAutoCompleteAddress.setText("")
        }
    }

    private fun initPresenter() {
        mPresenter = PickAddressPresenter(this, Geocoder(this, Locale.getDefault()))
    }


    @SuppressLint("MissingPermission")
    private fun setMyLocationEnable() {
        mMap!!.isMyLocationEnabled = true
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        // По умолчанию Ориентир Бишкек
        val startLatLng = LatLng(42.8746, 74.5698)
        val camPos = CameraPosition.Builder().target(startLatLng).zoom(13f).build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(camPos))
        mMap!!.setOnMyLocationButtonClickListener(this)
        if (Permissions.iPermissionLocation(this))
            setMyLocationEnable()
        mMap!!.setOnMapClickListener(this)

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


    override fun onMapClick(latLng: LatLng?) {
        Log.d("TEST_LATLNG", latLng!!.latitude.toString() + "" + latLng.longitude.toString())
        moveMap(latLng)
        mPresenter.getAddress(latLng)
    }


    override fun moveMap(latLng: LatLng?) {
        if (mMap != null && latLng != null) {
            mDefaultLocation = latLng
            mMap!!.clear()
            mMap!!.addMarker(MarkerOptions().icon(
                    BitmapDescriptorFactory
                            .fromResource(R.mipmap.ic_launcher))
                    .anchor(0.0f, 1.0f)
                    .position(mDefaultLocation))
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder()
                    .target(mDefaultLocation).zoom(15f).build()))
        }
    }


    override fun setAddress(address: String) {
        tvAutoCompleteAddress.setText(address)
    }

    private fun setPickedAddress(address: String) {
        setResult(Activity.RESULT_OK, Intent()
                .putExtra(MAP_RESULT, address)
                .putExtra(MAP_LOCATION, mDefaultLocation))
        finish()
    }


    override fun onMyLocationButtonClick(): Boolean {

        if (LocationsManager.isLocationEnabled(this)) {
            getLocation()
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

        return false


    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        // instantiate the location manager, note you will need to request permissions in your manifest
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // get the last know location from your location manager.

        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        // now get the lat/lon from the location and do something with it.
        val latlng = LatLng(location.latitude, location.longitude)
        moveMap(latlng)
        mPresenter.getAddress(latlng)

    }

    protected fun showWarningMessage(message: String) {
        val builder = android.support.v7.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.error)).setMessage(message)
                .setPositiveButton(android.R.string.ok, { v: DialogInterface, _: Int ->
                    v.dismiss()
                }).show()
    }
}