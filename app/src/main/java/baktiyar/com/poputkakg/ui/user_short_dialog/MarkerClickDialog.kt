package baktiyar.com.poputkakg.ui.user_short_dialog

import android.annotation.SuppressLint
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.Rout
import kotlinx.android.synthetic.main.dialog_marker_click.*
import kotlinx.android.synthetic.main.dialog_marker_click.view.*

@SuppressLint("ValidFragment")
class MarkerClickDialog(currentMarkerRout: Rout) : DialogFragment() {

    private lateinit var mPresenter : MarkerClickContract.Presenter
    private val mRout = currentMarkerRout
    private fun setFillMarkerDialogInformation(view: View,currentMarkerRout: Rout) {
            view.name.text = currentMarkerRout.description
            view.type.text  = when(currentMarkerRout.isDriver){
                true-> "Водитель"
                else-> "Пассажир"
            }
            view.startAddress.text = currentMarkerRout.startAddress
            view.targetAddress.text = currentMarkerRout.endAddress
            view.time.text = currentMarkerRout.startTime.toString()
        }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if(dialog != null && dialog.window != null){
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.dialog_marker_click,container,false)

        setFillMarkerDialogInformation(view,mRout)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init(){
        val window = dialog.window
        window.requestFeature(Window.FEATURE_NO_TITLE)
    }
    }


