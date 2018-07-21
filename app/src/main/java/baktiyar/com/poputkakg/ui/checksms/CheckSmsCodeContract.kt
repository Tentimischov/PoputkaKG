package baktiyar.com.poputkakg.ui.checksms

import baktiyar.com.poputkakg.model.Token
import baktiyar.com.poputkakg.model.SmsCode

interface CheckSmsCodeContract {
    interface View {
        fun onSuccessCheckSmsCode(token: Token)
        fun onError(message: String)
    }

    interface Presenter {
        fun checkSmsCode(smsCode: SmsCode)
    }
}