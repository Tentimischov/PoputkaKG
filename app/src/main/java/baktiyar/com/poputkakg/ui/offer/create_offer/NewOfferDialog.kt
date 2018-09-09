package baktiyar.com.poputkakg.ui.offer.create_offer

import android.app.DialogFragment
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.StartApplication.Companion.INSTANCE
import baktiyar.com.poputkakg.model.Rout
import kotlinx.android.synthetic.main.dialog_new_offer.*
import android.widget.CompoundButton
import android.widget.Toast
import baktiyar.com.poputkakg.model.Point
import baktiyar.com.poputkakg.ui.pick_addr.PickAddressActivity
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.Const.Companion.MAP_ADDITIONAL_POINTS
import baktiyar.com.poputkakg.util.Const.Companion.MAP_END
import baktiyar.com.poputkakg.util.Const.Companion.MAP_POINTS_ADDRESS
import baktiyar.com.poputkakg.util.Const.Companion.MAP_POINT_RESULT
import baktiyar.com.poputkakg.util.Const.Companion.MAP_START
import baktiyar.com.poputkakg.util.FileLog
import com.google.android.gms.maps.model.LatLng
import java.sql.Timestamp
import kotlin.collections.ArrayList



class NewOfferDialog : DialogFragment(), NewOfferContract.View, View.OnClickListener {


    private lateinit var mPresenter: NewOfferContract.Presenter
    private var mRout: Rout = Rout()

    private lateinit var mPrefs: SharedPreferences
    private lateinit var mToken: String
    private var mIsDriver = true
    private var mIsBag = false
    private var addressList = ArrayList<String>()
    private var mPoints = ArrayList<Point>()
    private var mStartLocation: LatLng? = null
    private var mEndLocation: LatLng? = null


    companion object {
        fun newInstance(): NewOfferDialog {
            return NewOfferDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null && dialog.window != null) {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.dialog_new_offer, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mPrefs = this.activity.getSharedPreferences(Const.PREFS_FILENAME, 0)
        mToken = mPrefs.getString(Const.PREFS_CHECK_TOKEN, "")
        mIsDriver = mPrefs.getBoolean(Const.PREFS_CHECK_IS_DRIVER, true)

        FileLog.e("Token $mToken")

        initPresenter()

        initView(mIsDriver)

        val window = dialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window.requestFeature(Window.FEATURE_NO_TITLE)

    }

    private fun initView(isDriver: Boolean) {
        if (isDriver) {
            tvSetEndPoint.text = getString(R.string.driver_endAdress_text)
            layoutDriverPart.visibility = View.VISIBLE
            layoutRiderPart.visibility = View.GONE
        } else {
            layoutDriverPart.visibility = View.GONE
            layoutRiderPart.visibility = View.VISIBLE
        }
        switchRout.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            mRout.isLocal = !isChecked
        })

        checkboxIsBag.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> setRoutGoal(isChecked) })
        checkboxIsMan.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> setRoutGoal(isChecked) })
        ivStartAddressPoint.setOnClickListener(this)
        ivEndAddressPoint.setOnClickListener(this)
        ivAdditionalAddressPoint.setOnClickListener(this)
        btnSendOffer.setOnClickListener(this)
    }

    private fun initPresenter() {
        mPresenter = NewOfferPresenter(this, INSTANCE.service)
    }

    override fun onClick(v: View?) {
        when (v) {
            ivStartAddressPoint -> goToMap(tvStartAddressPoint.text.toString(), MAP_START, mStartLocation)
            ivEndAddressPoint -> goToMap(tvEndAddressPoint.text.toString(), MAP_END, mEndLocation)
            ivAdditionalAddressPoint -> goToMap(tvAdditionalAddressPoint.text.toString(), MAP_ADDITIONAL_POINTS, LatLng(42.3, 42.3))
            btnSendOffer -> sendOffer()
        }


    }


    private fun setRoutGoal(isBag: Boolean) {

        mIsBag = isBag
        if (isBag) {
            checkboxIsBag.isChecked = true
            checkboxIsMan.isChecked = false
        } else {
            checkboxIsBag.isChecked = false
            checkboxIsMan.isChecked = true
        }
    }

    private fun sendOffer() {
        mRout.availableSeats = Integer.valueOf(etSpaceAmount.text.toString())
        mRout.isDriver = mIsDriver
        mRout.isBag = mIsBag
        mRout.description = etRoutDescription.text.toString()
        mRout.startTime = Timestamp(System.currentTimeMillis()).nanos
        mRout.startLatitude = mStartLocation!!.latitude.toString()
        mRout.startLongitude = mStartLocation!!.longitude.toString()

        mRout.endLatitude = mEndLocation!!.latitude.toString()
        mRout.endLongitude = mEndLocation!!.longitude.toString()
        mRout.startAddress = tvStartAddressPoint.text.toString()
        mRout.endAddress = tvEndAddressPoint.text.toString()

        mPresenter.sendOffer(mRout, Const.TOKEN_PREFIX + mToken)



    }


    override fun onSuccessSendOffer(rout: Rout) {
        Const.makeToast(activity, resources.getString(R.string.success))
    }

    override fun onError(message: String) {
        Const.makeToast(activity, resources.getString(R.string.error))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                MAP_START -> {
                    tvStartAddressPoint.text = data?.getStringExtra(Const.MAP_RESULT)
                    mStartLocation = data?.extras?.get(Const.MAP_LOCATION) as LatLng
                }
                MAP_END -> {
                    tvEndAddressPoint.text = data?.getStringExtra(Const.MAP_RESULT)
                    mEndLocation = data?.extras?.get(Const.MAP_LOCATION) as LatLng
                }
                MAP_ADDITIONAL_POINTS->{
                    mRout.points = data?.extras?.get(MAP_POINT_RESULT) as ArrayList<Point>
                    addressList = data?.extras?.get(MAP_POINTS_ADDRESS) as ArrayList<String>
                    setAdditionalPointsText(addressList)

                }
            }
        }
    }

    private fun setAdditionalPointsText(addressList: java.util.ArrayList<String>?) {
        var buffer = StringBuffer()
        for (i in 0..addressList!!.size-1){
            buffer.append(addressList[i])
            buffer.append(",")
        }
        tvAdditionalAddressPoint.text = buffer.toString()
    }

    fun goToMap(address: String, requestCode: Int, selectedLocation: LatLng?) {
        val intent = Intent(activity, PickAddressActivity::class.java)
        if(requestCode == MAP_ADDITIONAL_POINTS)
            intent.putExtra("isAdditional",true)
        intent.putExtra("name", address)
        intent.putExtra("location", selectedLocation)
        startActivityForResult(intent, requestCode)
    }
}