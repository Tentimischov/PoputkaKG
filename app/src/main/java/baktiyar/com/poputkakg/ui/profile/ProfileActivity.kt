package baktiyar.com.poputkakg.ui.profile

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.StartApplication.Companion.INSTANCE
import baktiyar.com.poputkakg.model.History
import baktiyar.com.poputkakg.model.ProfileInfo
import baktiyar.com.poputkakg.ui.profile.adapter.HistoryAdapter
import baktiyar.com.poputkakg.util.ConnectionsManager
import baktiyar.com.poputkakg.util.Const
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.partial_toolbar.*

class ProfileActivity : AppCompatActivity(), ProfileContract.View {

    private lateinit var mPresenter: ProfilePresenter
    private lateinit var mHistoryAdapter: HistoryAdapter

    private lateinit var mHistoryList: MutableList<History>
    private lateinit var mProfileInfo: ProfileInfo


    private lateinit var mPrefs: SharedPreferences

    private var mUserId: Int = 8
    private var mToken = "65bfea2900e71cae4c3ee8086a9df1bb3feffb92"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        mPrefs = this.getSharedPreferences(Const.PREFS_FILENAME, 0)
        mUserId = mPrefs.getInt(Const.PREFS_CHECK_USER_ID, 0)
        mToken = mPrefs.getString(Const.PREFS_CHECK_TOKEN, "")

        init()
    }


    private fun init(){
        initToolbar()
        initPresenter()
        initRecyclerView()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)))
    }

    private fun initPresenter(){
        mPresenter = ProfilePresenter(this, INSTANCE.service)
        if (ConnectionsManager.isNetworkOnline()){
            mPresenter.getProfileInfo(mUserId, mToken)
        }else{
            Const.makeToast(this, resources.getString(R.string.turn_on_internet))
        }
    }


    private fun initView(profileInfo: ProfileInfo){
        Glide.with(this)
                .load(profileInfo.photo)
                .centerCrop()
                .into(ivProfileImage)

        tvProfileName.text = profileInfo.firstName +" "+ profileInfo.lastName
        tvProfileCity.text = profileInfo.city
        tvProfilePhoneNumber.text = profileInfo.phone
        tvProfileRating.text = profileInfo.rating.toString()

    }

    private fun initRecyclerView(){

    }

    override fun onSuccessGetProfileInfo(profileInfo: ProfileInfo) {
        mProfileInfo = profileInfo
        initView(mProfileInfo)

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
}
