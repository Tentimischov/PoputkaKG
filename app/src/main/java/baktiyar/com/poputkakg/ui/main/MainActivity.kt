package baktiyar.com.poputkakg.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.view.View
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.StartApplication.Companion.INSTANCE
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.model.TokenInfo
import baktiyar.com.poputkakg.ui.map.MapViewActivity
import baktiyar.com.poputkakg.ui.offer.create_offer.NewOfferDialog
import baktiyar.com.poputkakg.ui.suggestions.SuggestionActivity
import baktiyar.com.poputkakg.util.Const
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MapViewActivity(), MainContract.View,NavigationView.OnNavigationItemSelectedListener {



    private lateinit var mPresenter:MainPresenter
    private lateinit var mToken: String
    private var mRoutList: MutableList<Rout> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToken = mPrefs.getString(Const.PREFS_CHECK_TOKEN, "")



        initPresenter()
    }

    private fun init() {
        ivCreateSuggestion.setOnClickListener { showNewOfferDialog()
        }
        ivFilterSettings.setOnClickListener { showFilterDialog() }

        imgZoomPlus.setOnClickListener { mMap.animateCamera(CameraUpdateFactory.zoomIn()) }
        imgZoomMinus.setOnClickListener { mMap.animateCamera(CameraUpdateFactory.zoomOut()) }
        tvAll.setOnClickListener {
            mPresenter.getRoutes(mToken)
            mainFilter.visibility = View.GONE
        }
        tvDrivers.setOnClickListener {
            mPresenter.getDriversRouts(mToken)
            mainFilter.visibility = View.GONE
        }
        tvSystemSuggested.setOnClickListener {
            mPresenter.getSystemSuggestionRoutes(mToken)
            mainFilter.visibility = View.GONE
        }

    }
    override fun onSuccessSendToken(tokenInfo: TokenInfo) {

    }
    override fun onSuccessGetSuggestionRoutes(routs: List<Rout>) {
        mRoutList = routs as MutableList<Rout>
        drawList(mRoutList)
        hideProgress()
    }

    override fun onSuccessDriverRoutes(routs: List<Rout>) {
        mRoutList = routs as MutableList<Rout>
        drawList(mRoutList)
        hideProgress()
    }

    private fun initPresenter() {
        mToken = Const.TOKEN_PREFIX+this.getSharedPreferences(Const.PREFS_FILENAME,0).getString(Const.PREFS_CHECK_TOKEN,"null")
        val name = this.getSharedPreferences(Const.PREFS_FILENAME,android.content.Context.MODE_PRIVATE).getString("name","null")
        mPresenter = MainPresenter(this, INSTANCE.service)
        mPresenter.sendToken(this,this,name,mToken)
        mPresenter.getRoutes(mToken)
        init()

    }

    private fun showNewOfferDialog() {
        val dialog = NewOfferDialog.newInstance()
        dialog.show(fragmentManager, "dialog")
    }

    private fun showFilterDialog() {
        if(mainFilter.visibility == View.VISIBLE)
            mainFilter.visibility = View.GONE
        else {
            mainFilter.visibility = View.VISIBLE
            mainFilter.animate()
        }
    }
    override fun onSuccessGetRoutes(routs: List<Rout>) {
        mRoutList = routs as MutableList<Rout>
        drawList(mRoutList)
        hideProgress()
    }

    override fun onError(message: String) {
    }

    override fun showProgress() {
        Const.progressIsShowing(true, this)
    }

    override fun hideProgress() {
        Const.progressIsShowing(false, this)
    }
}
