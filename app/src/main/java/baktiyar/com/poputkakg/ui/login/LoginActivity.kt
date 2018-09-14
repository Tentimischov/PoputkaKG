package baktiyar.com.poputkakg.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.StartApplication.Companion.INSTANCE
import baktiyar.com.poputkakg.model.Login
import baktiyar.com.poputkakg.model.Token
import baktiyar.com.poputkakg.ui.main.MainActivity
import baktiyar.com.poputkakg.ui.signup.SignUpActivity
import baktiyar.com.poputkakg.util.Const
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginContract.View, View.OnClickListener {

    private var mLogin: Login = Login()


    private lateinit var mPrefs: SharedPreferences
    private lateinit var mPresenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mPrefs = this.getSharedPreferences(Const.PREFS_FILENAME, 0)

        init()

    }

    private fun init() {
        checkPrefs()
        initView()
        initPresenter()
    }

    private fun checkPrefs() {
        if (mPrefs.contains(Const.PREFS_CHECK_TOKEN)) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun initPresenter() {
        mPresenter = LoginPresenter(this, INSTANCE.service)
    }

    private fun initView() {
        btnEnterLogin.setOnClickListener(this)
        btnGoToSignUp.setOnClickListener(this)
    }

    private fun login() {
        if (etPassWordLogin.text.toString() != "" &&
                etPhoneLogin.text.toString() != "") {

            mLogin.phone = etPhoneLogin.text.toString()
            mLogin.password = etPassWordLogin.text.toString()
            mPresenter.login(mLogin)
        } else {
            Const.makeToast(this, resources.getString(R.string.fill_fields))
        }
    }

    override fun onError(message: String) {
        Const.makeSnack(resources.getString(R.string.error_login), findViewById<View>(android.R.id.content))
    }

    override fun onSuccessLogin(token: Token) {
        Const.makeSnack(resources.getString(R.string.success_login), findViewById<View>(android.R.id.content))

        val editor = mPrefs.edit()
        editor.putString(Const.PREFS_CHECK_TOKEN, token.token)
        editor.putInt(Const.PREFS_CHECK_USER_ID, token.userId!!)

        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onClick(v: View?) {
        when (v) {
            btnEnterLogin -> {
                login()
            }
            btnGoToSignUp -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }
    }
}
