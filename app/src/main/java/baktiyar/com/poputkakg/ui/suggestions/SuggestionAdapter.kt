package baktiyar.com.poputkakg.ui.suggestions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.Rout
import baktiyar.com.poputkakg.model.suggestion.Suggestion
import kotlinx.android.synthetic.main.item_suggestion.view.*

class SuggestionAdapter( private var listOfSuggestion:ArrayList<Suggestion>, var listener : Listener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int {
       return listOfSuggestion.size
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.item_suggestion,parent,false)
        return ViewHolder(view)
    }
    inner class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView){
        fun bind(position:Int){
            val routes = listOfSuggestion.get(position).route!!.split("-->")
            val name = listOfSuggestion.get(position).name!!.split(" ")

            itemView.tvName.text = name[0]
            itemView.tvFrom.text = routes[0]
            itemView.tvTime.text =listOfSuggestion.get(position).created
            itemView.tvTo.text = routes[1]

            itemView.btnDeal.setOnClickListener {
                val index = it.tag as Int
                listener.onDealClicked(listOfSuggestion[position].id)
            }
            itemView.btnCall.setOnClickListener {  }

        }

    }
    interface Listener{
        fun onDealClicked(routeId:Int?)
    }
}