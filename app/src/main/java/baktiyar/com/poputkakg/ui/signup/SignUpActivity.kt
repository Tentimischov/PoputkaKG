package baktiyar.com.poputkakg.ui.signup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.StartApplication.Companion.INSTANCE
import baktiyar.com.poputkakg.model.City
import baktiyar.com.poputkakg.model.ProfileInfo
import baktiyar.com.poputkakg.model.User
import baktiyar.com.poputkakg.ui.checksms.CheckSmsCodeActivity
import baktiyar.com.poputkakg.ui.signup.adapter.CitiesSpinnerAdapter
import baktiyar.com.poputkakg.util.ConnectionsManager
import baktiyar.com.poputkakg.util.Const
import kotlinx.android.synthetic.main.activity_sign_up.*
import android.widget.CompoundButton
class SignUpActivity : AppCompatActivity(), View.OnClickListener, SignUpContract.View {

    private var mIsDriver = true
    private var mIsAgreed = false
    private var mUser: User = User()
    private var mProfileInfo: ProfileInfo = ProfileInfo()
    private lateinit var mCities: MutableList<City>
    private lateinit var mPresenter: SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        init()
    }

    private fun init() {
        chkBoxAgreedSignUp.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            mIsAgreed = isChecked
        })
        btnIsDriverSignUp.setOnClickListener(this)
        btnIsRiderSignUp.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
        if (ConnectionsManager.isNetworkOnline()) {
            initPresenter()
        } else {
            Const.makeToast(this, resources.getString(R.string.turn_on_internet))
        }
    }

    private fun initPresenter() {
        mPresenter = SignUpPresenter(this, INSTANCE.service)
        mPresenter.getCities()
    }

    private fun initSpinner() {
        spinnerSitiesSignUp.adapter = CitiesSpinnerAdapter(this, mCities)
    }

    private fun onUserButtonClick(isDriver: Boolean) {
        if (isDriver) {
            mIsDriver = true
            btnIsDriverSignUp.setBackgroundResource(R.drawable.bg_round)
            btnIsRiderSignUp.setBackgroundResource(R.drawable.bg_round_gray)
        } else {
            mIsDriver = false
            btnIsDriverSignUp.setBackgroundResource(R.drawable.bg_round_gray)
            btnIsRiderSignUp.setBackgroundResource(R.drawable.bg_round)
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            btnIsRiderSignUp -> {
                onUserButtonClick(false)
            }
            btnIsDriverSignUp -> {
                onUserButtonClick(true)
            }

            btnSignUp -> {
                createUser()
            }
        }
    }

    @SuppressLint("WorldReadableFiles")
    private fun createUser() {

        if (filledFields()) {

            if (checkFor9Signs()) {
                mUser.phone = etPhoneSignUp.text.toString()
            } else {
                Const.makeToast(this, resources.getString(R.string.error_phone_length))
            }
            val preference = this.getSharedPreferences(Const.PREFS_FILENAME,android.content.Context.MODE_PRIVATE).edit()
            preference.putString("name",etNameSignUp    .text.toString())
            preference.apply()
            mUser.firstName = etNameSignUp.text.toString()
            mUser.password = etPasswordSignUp.text.toString()
            mUser.passwordRepeat = etPasswordSignUp.text.toString()
            mUser.isDriver = mIsDriver
            if (mCities.size > 0) {
                mUser.cityId = mCities[spinnerSitiesSignUp.selectedItemPosition].id
            } else {
                mUser.cityId = 1
            }
            if (mIsAgreed) {
                mPresenter.signUp(mUser)
            } else {
                Const.makeToast(this, getString(R.string.read_license))
            }
        } else {
            Const.makeToast(this, resources.getString(R.string.fill_fields))
        }
    }

    private fun checkFor9Signs(): Boolean {
        return (etPhoneSignUp.text.toString().length == 9)

    }

    private fun filledFields(): Boolean {
        return !(etPasswordSignUp.text.toString() == "" || etPhoneSignUp.text.toString() == ""
                || etNameSignUp.text.toString() == "")
    }

    override fun onSuccessGetCities(cities: MutableList<City>) {
        mCities = cities
        initSpinner()
    }

    override fun onSuccessSignUp(profileInfo: ProfileInfo) {
        mProfileInfo = profileInfo
        Const.makeToast(this, "Success create")
        var intent = Intent(this, CheckSmsCodeActivity::class.java)
        intent.putExtra(Const.INTENT_PROFILE_INFO, profileInfo)
        startActivity(intent)
    }


    override fun onError(message: String) {
        Const.makeToast(this, message)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

}
