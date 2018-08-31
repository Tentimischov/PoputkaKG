package baktiyar.com.poputkakg.ui.profile

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.StartApplication.Companion.INSTANCE
import baktiyar.com.poputkakg.model.History
import baktiyar.com.poputkakg.model.ProfileInfo
import baktiyar.com.poputkakg.ui.profile.adapter.HistoryAdapter
import baktiyar.com.poputkakg.util.ConnectionsManager
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.FileLog
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.partial_toolbar.*
import kotlin.properties.Delegates

class ProfileActivity : AppCompatActivity(), ProfileContract.View, HistoryAdapter.OnHistoryItemClickListener {

    private lateinit var mPresenter: ProfilePresenter
    private lateinit var mHistoryAdapter: HistoryAdapter

    private lateinit var mHistoryList: MutableList<History>
    private lateinit var mProfileInfo: ProfileInfo


    private lateinit var mPrefs: SharedPreferences


    private var mUserId : Int by Delegates.notNull()
    private lateinit var mToken : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        mPrefs = this.getSharedPreferences(Const.PREFS_FILENAME, 0)
        mUserId = mPrefs.getInt(Const.PREFS_CHECK_USER_ID, 0)


        mToken = mPrefs.getString(Const.PREFS_CHECK_TOKEN, "")
        FileLog.e("Token "+ mToken)
        init()
    }


    private fun init() {
        initToolbar()
        initPresenter()
    }

    private fun initToolbar() {

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)))

        // set up button color
        toolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        ivToolbarImage.setBackgroundResource(R.drawable.ic_logo_white)
    }

    private fun initPresenter() {
        mPresenter = ProfilePresenter(this, INSTANCE.service)
        if (ConnectionsManager.isNetworkOnline()) {
            mPresenter.getProfileInfo(mUserId, mToken)
        } else {
            Const.makeToast(this, resources.getString(R.string.turn_on_internet))
        }
    }


    private fun initView(profileInfo: ProfileInfo) {
        if (profileInfo.photo != "") {

            Glide.with(this)
                    .load(profileInfo.photo)
                    .into(ivProfileImage)
        }else{
            ivProfileImage.visibility = View.GONE
        }
        tvProfileName.text = profileInfo.firstName
        tvProfileCity.text = profileInfo.city
        tvProfilePhoneNumber.text = profileInfo.phone
        if (profileInfo.dealsCount != null) {
            tvProfileDealsCount.text = profileInfo.dealsCount.toString()
        } else {
            tvProfileDealsCount.text = "0"
        }
        if(profileInfo.rating!=null){
            tvProfileRating.text = profileInfo.rating.toString()
        }else{
            tvProfileRating.text = "0"
        }
        setUserStatus(profileInfo.isDriver)

    }

    private fun initRecyclerView() {
        mHistoryAdapter = HistoryAdapter(mHistoryList, this)
        rvHistories.layoutManager = LinearLayoutManager(this)
        rvHistories.adapter = mHistoryAdapter
        rvHistories.isNestedScrollingEnabled = true

    }

    override fun onSuccessGetProfileInfo(profileInfo: ProfileInfo) {
        mProfileInfo = profileInfo
        mHistoryList = profileInfo.history as MutableList<History>
        initView(mProfileInfo)
        initRecyclerView()
    }

    override fun onError(message: String) {
        Const.makeToast(this, message)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setUserStatus(isDriver: Boolean?) {
        if (isDriver != null) {
            if (isDriver) {
                btnIsRiderSignUp.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightGray))
            } else {
                btnIsDriverSignUp.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightGray))
            }
        }
    }
}
