package baktiyar.com.poputkakg.ui.main

import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.util.IProgressBar

interface MainContract{
    interface View: IProgressBar{
        fun onSuccessGetRoutes(routs : List<Rout>)
        fun onError(message: String)
    }

    interface Presenter{
        fun getRoutes(token: String)
    }
}