package baktiyar.com.poputkakg.ui.main

import android.os.Bundle
import android.support.v7.app.AlertDialog
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.ui.BaseActivity
import baktiyar.com.poputkakg.ui.map.MapViewActivity
import baktiyar.com.poputkakg.ui.offer.create_offer.NewOfferDialog
import baktiyar.com.poputkakg.util.Const
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MapViewActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivCreateSuggestion.setOnClickListener { showNewOfferDialog() }
        ivFilterSettings.setOnClickListener { showFilterDialog() }

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
                    Const.makeToast(this, getString(R.string.test))
                }
                1 -> {
                    Const.makeToast(this, getString(R.string.test))
                }
                2 -> {
                    Const.makeToast(this, getString(R.string.test))
                }
                3 -> {
                    Const.makeToast(this, getString(R.string.test))
                }
            }/*
            dialog.dismiss()*/
        }.show()
    }

}
