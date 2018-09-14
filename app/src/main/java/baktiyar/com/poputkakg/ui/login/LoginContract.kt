package baktiyar.com.poputkakg.ui.login

import android.content.Context
import baktiyar.com.poputkakg.model.Login
import baktiyar.com.poputkakg.model.Token

interface LoginContract {
    interface View {
        fun onError(message: String)
        fun onSuccessLogin(token: Token)
    }

    interface Presenter {
        fun login(login: Login)
    }
}