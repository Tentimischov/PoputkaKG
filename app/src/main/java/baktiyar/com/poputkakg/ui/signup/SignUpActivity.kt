package baktiyar.com.poputkakg.ui.signup

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
import baktiyar.com.poputkakg.util.Const
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener, SignUpContract.View {


    private var mIsDriver: Boolean = true

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
        btnIsDriverSignUp.setOnClickListener(this)
        btnIsRiderSignUp.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)
        initPresenter()
    }

    private fun initPresenter() {
        mPresenter = SignUpPresenter(this, INSTANCE.service)
        mPresenter.getCities()
    }

    private fun initSpinner() {

        spinnerSitiesSignUp.adapter = CitiesSpinnerAdapter(this, mCities)

        spinnerSitiesSignUp.setSelection(1)
    }

    private fun onUserButtonClick(isDriver: Boolean) {
        if (isDriver) {

        } else {

        }

    }

    override fun onClick(v: View?) {
        when (v) {
            btnIsRiderSignUp -> {

            }
            btnIsDriverSignUp -> {

            }

            btnSignUp -> {
                createUser()
            }
        }
    }

    private fun createUser() {

        if (filledFields()) {

            mUser.phone = etPhoneSignUp.text.toString()
            mUser.password = etPasswordSignUp.text.toString()
            mUser.passwordRepeat = etPasswordSignUp.text.toString()
            mUser.isDriver = mIsDriver
            mUser.cityId = mCities[spinnerSitiesSignUp.selectedItemPosition].id
//            mUser.cityId = 1
            mPresenter.signUp(mUser)
        } else {
            Const.makeToast(this, R.string.fill_fields.toString())
        }
    }

    private fun filledFields(): Boolean {
        return !(etPasswordSignUp.text.toString() == "" || etPhoneSignUp.text.toString() == "")
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