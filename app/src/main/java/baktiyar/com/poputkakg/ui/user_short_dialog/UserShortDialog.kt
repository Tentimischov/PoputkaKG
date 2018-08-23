package baktiyar.com.poputkakg.ui.user_short_dialog

import android.app.DialogFragment
import android.os.Bundle
import android.support.design.internal.BottomNavigationPresenter
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.view.Window
import baktiyar.com.poputkakg.R

class UserShortDialog : DialogFragment(){
    private lateinit var mPresenter : UserShortDialogContract.Presenter
    companion object {
        fun newInstance():UserShortDialog{
            return UserShortDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if(dialog != null && dialog.window != null){
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.dialog_user_short_information,container,false)

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


