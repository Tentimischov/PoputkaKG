package baktiyar.com.poputkakg.ui.main

import android.os.Bundle
import android.support.v7.app.AlertDialog
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.StartApplication.Companion.INSTANCE
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.ui.BaseActivity
import baktiyar.com.poputkakg.ui.map.MapViewActivity
import baktiyar.com.poputkakg.ui.offer.create_offer.NewOfferDialog
import baktiyar.com.poputkakg.util.Const
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

    private fun init() {
        ivCreateSuggestion.setOnClickListener { showNewOfferDialog() }
        ivFilterSettings.setOnClickListener { showFilterDialog() }

        initPresenter()
    }

    private fun initPresenter() {
        mPresenter = MainPresenter(this, INSTANCE.service)
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
