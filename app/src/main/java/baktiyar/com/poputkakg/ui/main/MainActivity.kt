package baktiyar.com.poputkakg.ui.main

import android.os.Bundle
import android.support.v7.app.AlertDialog
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.StartApplication.Companion.INSTANCE
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.model.TokenInfo
import baktiyar.com.poputkakg.ui.BaseActivity
import baktiyar.com.poputkakg.ui.map.MapViewActivity
import baktiyar.com.poputkakg.ui.offer.create_offer.NewOfferDialog
import baktiyar.com.poputkakg.util.Const
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_new_offer.*

class MainActivity : MapViewActivity(), MainContract.View {


    private lateinit var mPresenter: MainContract.Presenter
    private lateinit var mToken: String
    private var mRoutList: MutableList<Rout> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToken = mPrefs.getString(Const.PREFS_CHECK_TOKEN, "")


        init()

    }

    override fun onStart() {
        super.onStart()
    }

    private fun init() {
        ivCreateSuggestion.setOnClickListener { showNewOfferDialog() }
        ivFilterSettings.setOnClickListener { showFilterDialog() }

        initPresenter()
    }
    override fun onSuccessSendToken(tokenInfo: TokenInfo) {

    }

    private fun initPresenter() {
        val token = Const.TOKEN_PREFIX+this.getSharedPreferences(Const.PREFS_FILENAME,0).getString(Const.PREFS_CHECK_TOKEN,"null")
        val name = this.getSharedPreferences(Const.PREFS_FILENAME,android.content.Context.MODE_PRIVATE).getString("name","null")
        mPresenter = MainPresenter(this, INSTANCE.service)
        mPresenter.sendToken(this,this,name,token)
        mPresenter.getRoutes(mToken)
    }

    private fun showNewOfferDialog() {
        val dialog = NewOfferDialog.newInstance()
        dialog.show(fragmentManager, "dialog")
    }

    private fun showFilterDialog() {
        val args = arrayOf<String>(
                getString(R.string.filter_from_all),
                getString(R.string.filter_from_drivers),
                getString(R.string.filter_from_riders),
                getString(R.string.filter_from_suggestions))
        AlertDialog.Builder(this).setTitle(getString(R.string.filter)).setItems(args) { dialog, w ->
            when (w) {
                0 -> {
                    getRoutes()
                }
                1 -> {
                    //TODO make filter for drivers
                    getRoutes()
                }
                2 -> {
                    //TODO make filter for riders
                    getRoutes()
                }
                3 -> {
                    //TODO make filter for suggestions
                    getRoutes()
                }
            }
            dialog.dismiss()
        }.show()
    }


    private fun getRoutes() {
        mPresenter.getRoutes(mToken)
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
