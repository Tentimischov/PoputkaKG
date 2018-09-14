package baktiyar.com.poputkakg.ui.suggestions

import android.content.Context
import android.widget.Toast
import baktiyar.com.poputkakg.StartApplication
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.model.suggestion.Suggestion
import baktiyar.com.poputkakg.model.suggestion.SuggestionList
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.IProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuggestionPresenter(val view:SuggestionContract.View):SuggestionContract.Presenter{

    override fun getOwnRoutes(context: Context, token: String?) {
        if(isViewAttached()){
            view?.showProgress()
            StartApplication.INSTANCE.service.getOwnRoutes(token!!).enqueue(
                    object :Callback<ArrayList<Rout>>{
                        override fun onResponse(call: Call<ArrayList<Rout>>?, response: Response<ArrayList<Rout>>?) {
                            if(isViewAttached()){
                                if(response!!.isSuccessful && response!!.body() != null)
                                    view.onSuccessGetOwnRoutes(createSuggestionListFromDeals(response.body()!!))
                            }
                        }

                        override fun onFailure(call: Call<ArrayList<Rout>>?, t: Throwable?) {
                        }

                    }
            )
        }
    }

    override fun getDealsWithClosestDepartureTime(context: Context, token: String?) {
        if(isViewAttached()){
            view?.showProgress()
            StartApplication.INSTANCE.service.getAllRoutesWithDepartureTime(token!!).enqueue(
                    object:Callback<ArrayList<Rout>>{
                        override fun onResponse(call: Call<ArrayList<Rout>>?, response: Response<ArrayList<Rout>>?) {
                            if(isViewAttached()){
                                if(response!!.isSuccessful && response.body() != null){
                                  view.onSuccessGetDealsWithClosestDepartureTime(
                                          createSuggestionListFromDeals(response.body()!!))
                                }
                            }
                        }

                        override fun onFailure(call: Call<ArrayList<Rout>>?, t: Throwable?) {
                        }

                    }
            )
        }
    }

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

    private fun createSuggestionListFromDeals(body: ArrayList<Rout>): ArrayList<Suggestion> {
        val listOfSuggestions = ArrayList<Suggestion>()
        for (i in 0 until body.size){
            val suggestion = Suggestion()
            suggestion.id = body[i].id
            suggestion.route = body[i].startAddress+"-->"+body.get(i).endAddress
            suggestion.seats = "2"
            suggestion.created = body[i].startTime.toString()
            suggestion.status = "1"
            suggestion.name = body[i].name
            listOfSuggestions.add(suggestion)
        }
        return listOfSuggestions

    }

    private fun isViewAttached(): Boolean = view != null


}




