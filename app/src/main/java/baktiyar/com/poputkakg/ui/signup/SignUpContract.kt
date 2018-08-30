package baktiyar.com.poputkakg.ui.signup

import android.content.Context
import baktiyar.com.poputkakg.model.City
import baktiyar.com.poputkakg.model.ProfileInfo
import baktiyar.com.poputkakg.model.TokenInfo
import baktiyar.com.poputkakg.model.User
import baktiyar.com.poputkakg.util.IProgressBar

interface SignUpContract{

    interface Presenter{
        fun signUp(user: User)

        fun getCities()
    }

    interface View: IProgressBar {
        fun onSuccessGetCities(cities: MutableList<City>)
        fun onSuccessSignUp(profileInfo: ProfileInfo)


        fun onError(message: String)
    }


}