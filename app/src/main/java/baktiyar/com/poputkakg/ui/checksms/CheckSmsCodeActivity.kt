package baktiyar.com.poputkakg.ui.checksms

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.StartApplication.Companion.INSTANCE
import baktiyar.com.poputkakg.model.Token
import baktiyar.com.poputkakg.model.ProfileInfo
import baktiyar.com.poputkakg.model.SmsCode
import baktiyar.com.poputkakg.ui.main.MainActivity
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.Const.Companion.PREFS_FILENAME
import kotlinx.android.synthetic.main.activity_check_sms_code.*

class CheckSmsCodeActivity : AppCompatActivity(), CheckSmsCodeContract.View {

    private var mSmsCode: SmsCode = SmsCode()
    private var mProfileInfo: ProfileInfo = ProfileInfo()
    private var mToken: Token = Token()


    private lateinit var mPrefs: SharedPreferences

    private lateinit var mPresenter: CheckSmsCodePresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_sms_code)

        mPrefs = this.getSharedPreferences(PREFS_FILENAME, 0)

        init()
    }

    private fun init() {
        initView()
        initPresenter()
    }

    private fun initView() {
        btnCheckSms.setOnClickListener { checkSms() }
    }

    private fun initPresenter() {
        mPresenter = CheckSmsCodePresenter(this, INSTANCE.service)
    }

    private fun checkSms() {
        if (etCheckSms.text.toString() != "") {
            mSmsCode.smsCode = Integer.parseInt(etCheckSms.text.toString())
            mSmsCode.userId = mProfileInfo.userId
            mPresenter.checkSmsCode(mSmsCode)
        } else {
            Const.makeToast(this, R.string.fill_fields.toString())
        }

    }

    override fun onSuccessCheckSmsCode(token: Token) {
        mToken = token

        val editor = mPrefs.edit()
        editor.putString(Const.PREFS_CHECK_TOKEN, mToken.token)
        editor.putInt(Const.PREFS_CHECK_USER_ID, mToken.userId!!)

        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))

    }

    override fun onError(message: String) {
        Const.makeToast(this, message)
    }

}
