package baktiyar.com.poputkakg.ui.suggestions

import android.content.Context
import android.widget.Toast
import baktiyar.com.poputkakg.StartApplication
import baktiyar.com.poputkakg.model.suggestion.SuggestionList
import baktiyar.com.poputkakg.util.IProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuggestionPresenter(val view:SuggestionContract.View):SuggestionContract.Presenter{



    override fun getDeals(context: Context, token: String?) {
        if(isViewAttached()){
            view?.showProgress()
            StartApplication.INSTANCE.service.getOwnDeals(token!!).enqueue(
                    object :Callback<SuggestionList>{
                        override fun onFailure(call: Call<SuggestionList>?, t: Throwable?) {
                        }

                        override fun onResponse(call: Call<SuggestionList>?, response: Response<SuggestionList>?) {
                            if(isViewAttached()){
                                if(response!!.code() == 406){
                                    Toast.makeText(context,response.message().toString(),Toast.LENGTH_SHORT).show()
                                }
                                else if(response!!.isSuccessful && response != null){
                                    view.onSuccessGetDeals(response.body()!!)

                                }
                            }
                        }

                    }
            )
        }
    }
    private fun isViewAttached(): Boolean = view != null


}