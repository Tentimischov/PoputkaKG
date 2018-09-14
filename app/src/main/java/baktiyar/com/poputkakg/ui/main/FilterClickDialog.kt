package baktiyar.com.poputkakg.ui.main

import android.annotation.SuppressLint
import android.app.DialogFragment
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import baktiyar.com.poputkakg.R

@SuppressLint("ValidFragment")
class FilterClickDialog(presenter:MainPresenter,token:String):DialogFragment() {
    val mPresenter = presenter
    val mToken = token

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {



        return view
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog.window != null) {
            dialog.window.attributes.horizontalMargin = -0.180f
            dialog.window.attributes.verticalMargin = 0.200f
            dialog.window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window.setBackgroundDrawableResource(R.color.prozrachniy)
            dialog.window.setDimAmount(0f)
        }


    }



}