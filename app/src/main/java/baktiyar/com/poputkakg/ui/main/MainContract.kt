package baktiyar.com.poputkakg.ui.main

import android.content.Context
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.model.TokenInfo
import baktiyar.com.poputkakg.ui.signup.SignUpActivity
import baktiyar.com.poputkakg.util.IProgressBar

interface MainContract{
    interface View: IProgressBar{

        fun onSuccessGetRoutes(routs : List<Rout>)
        fun onError(message: String)
        fun onSuccessSendToken(tokenInfo: TokenInfo)
    }

    interface Presenter{
        fun getRoutes(token: String)
        fun sendToken(context: Context, activity: MainActivity,
                      firstName: String?, token: String)
    }
}