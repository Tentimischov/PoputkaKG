package baktiyar.com.poputkakg.ui.offer.create_offer

import baktiyar.com.poputkakg.model.Rout

interface NewOfferContract{
    interface View{
        fun onSuccessSendOffer(rout: Rout)
        fun onError(message: String)
    }

    interface Presenter{
        fun sendOffer(rout: Rout, token: String)
    }
}