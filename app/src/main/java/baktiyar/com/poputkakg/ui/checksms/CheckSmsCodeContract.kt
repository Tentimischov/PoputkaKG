package baktiyar.com.poputkakg.ui.checksms

import baktiyar.com.poputkakg.model.Token
import baktiyar.com.poputkakg.model.SmsCode
import baktiyar.com.poputkakg.util.IProgressBar

interface CheckSmsCodeContract {
    interface View : IProgressBar{
        fun onSuccessCheckSmsCode(token: Token)
        fun onError(message: String)
    }

    interface Presenter {
        fun checkSmsCode(smsCode: SmsCode)
    }
}