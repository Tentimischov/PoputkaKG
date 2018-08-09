package baktiyar.com.poputkakg.ui.offer.create_offer

import android.app.DialogFragment
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import baktiyar.com.poputkakg.ui.pick_addr.PickAddressActivity
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.Const.Companion.MAP_END
import baktiyar.com.poputkakg.util.Const.Companion.MAP_START
import baktiyar.com.poputkakg.util.FileLog
import com.google.android.gms.maps.model.LatLng


class NewOfferDialog : DialogFragment(), NewOfferContract.View, View.OnClickListener {


    private lateinit var mPresenter: NewOfferContract.Presenter
    private var mRout: Rout = Rout()

    private lateinit var mPrefs: SharedPreferences
    private lateinit var mToken: String

    private var mStartLocation: LatLng? = null
    private var mEndLocation: LatLng? = null

    private var mIsDriver: Boolean = true


    companion object {

        fun newInstance(): NewOfferDialog {
            var dialogFragment = NewOfferDialog()
            return dialogFragment
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
            layoutDriverPart.visibility = View.VISIBLE
            layoutRiderPart.visibility = View.GONE
        } else {
            layoutDriverPart.visibility = View.GONE
            layoutRiderPart.visibility = View.VISIBLE
        }
        switchRout.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            mRout.isLocal = !isChecked
        })

        ivStartAddressPoint.setOnClickListener(this)
        ivEndAddressPoint.setOnClickListener(this)
        btnSendOffer.setOnClickListener(this)
    }

    private fun initPresenter() {
        mPresenter = NewOfferPresenter(this, INSTANCE.service)
    }

    override fun onClick(v: View?) {
        when (v) {
            ivStartAddressPoint -> goToMap(tvStartAddressPoint.text.toString(), MAP_START, mStartLocation)
            ivEndAddressPoint -> goToMap(tvEndAddressPoint.text.toString(), MAP_END, mEndLocation)
            btnSendOffer -> sendOffer()
        }


    }


    private fun sendOffer() {
        mRout.availableSeats = Integer.valueOf(etSpaceAmount.text.toString())
        mRout.isDriver = true
        mRout.description = etRoutDescription.text.toString()

        mRout.startLatitude = mStartLocation!!.latitude.toString()
        mRout.startLongitude = mStartLocation!!.longitude.toString()

        mRout.endLatitude = mEndLocation!!.latitude.toString()
        mRout.endLongitude= mEndLocation!!.longitude.toString()


        mPresenter.sendOffer(mRout, mToken)
    }


    override fun onSuccessSendOffer(rout: Rout) {
        Const.makeToast(activity, resources.getString(R.string.success))
    }

    override fun onError(message: String) {
        Const.makeToast(activity, resources.getString(R.string.error))
    }

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
            }
        }
    }

    fun goToMap(address: String, requestCode: Int, selectedLocation: LatLng?) {
        val intent = Intent(activity, PickAddressActivity::class.java)
        intent.putExtra("name", address)
        intent.putExtra("location", selectedLocation)
        startActivityForResult(intent, requestCode)
    }
}