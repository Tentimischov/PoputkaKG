package baktiyar.com.poputkakg.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.ui.BaseActivity
import baktiyar.com.poputkakg.ui.user_short_dialog.MarkerClickDialog
import baktiyar.com.poputkakg.util.Permissions
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_toolbar_poputka.*

@Suppress("DEPRECATION")
open class MapViewActivity : BaseActivity(),MapContract.View{

    protected lateinit var mMap:GoogleMap
    private lateinit var mPresenter:MapPresenter

    @SuppressLint("ObsoleteSdkInt")
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
            initGoogleMap()

    }

    private fun initGoogleMap() {
        mPresenter = MapPresenter(this,this,this,resources,supportFragmentManager)
        mPresenter.initMap()
    }

    override fun onSuccessMapReady(map: GoogleMap) {
        mMap = map
        configurateGoogleMapUi(map)

    }
    @SuppressLint("MissingPermission")
    private fun configurateGoogleMapUi(map: GoogleMap) {
        ivMyLocation.setOnClickListener {
            if (ifGpsEnabled())
                mPresenter.getMyLocation()
            else
                mPresenter.showDialogAlert()
        }
    }
    private fun ifGpsEnabled(): Boolean {
        val service = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permissions.Request.ACCESS_FINE_LOCATION &&
                requestCode == Permissions.Request.ACCESS_COARSE_LOCATION &&
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPresenter.getMyLocation()
        } else {
            Permissions.iPermissionLocation(this)

        }
    }

    protected  fun drawList(list: MutableList<Rout>) {
        mPresenter.drawList(list)
    }
    override fun onSuccessDrawList(list: MutableList<Rout>) {
        setOnMarkerClickListeners(list)
    }

    private fun setOnMarkerClickListeners(list:MutableList<Rout>) {
        mMap.setOnMarkerClickListener {
            openCurrentRiderInformationDialogFragment(it,list)
        }
    }

    private  fun openCurrentRiderInformationDialogFragment(it: Marker?, list: MutableList<Rout>): Boolean {
        var currentMarkerRout = Rout()
        for(i in 0 until list.size){
            if(it!!.title == list[i].owner){
                currentMarkerRout = list[i]

            }
        }
        val dialog = MarkerClickDialog(currentMarkerRout,mMap)
        dialog.show(fragmentManager,"dialog")
        return true
    }








    }




