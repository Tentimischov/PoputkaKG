package baktiyar.com.poputkakg.ui.profile

import baktiyar.com.poputkakg.model.ProfileInfo

interface ProfileContract {
    interface View {
        fun onSuccessGetProfileInfo(profileInfo: ProfileInfo)
        fun onError(message: String)
    }

    interface Presenter {
        fun getProfileInfo(userId: Int, token: String)
    }
}