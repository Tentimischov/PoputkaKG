package baktiyar.com.poputkakg.ui.suggestions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.suggestionModels.Suggestion
import baktiyar.com.poputkakg.model.suggestionModels.SuggestionsList
import kotlinx.android.synthetic.main.item_suggestion.view.*

class SuggestionAdapter(private var listOfSuggestions:List<Suggestion>,var listener:Listener):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_suggestion
                ,parent,false)
        return RecyclerView.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        listOfSuggestions.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(position)
    }

    inner class ViewHolder(itemView: View?):RecyclerView.ViewHolder(itemView){
        fun bind(position:Int?){
            itemView.tvName.text = listOfSuggestions[position!!].name
            val list = listOfSuggestions[position].route!!.split("-->")
            itemView.tvFrom.text = list[0]
            itemView.tvTime.text = listOfSuggestions[position].created
            itemView.tvTo.text = list[1]

            itemView.btnSuggestion.tag = position

            itemView.btnSuggestion.setOnClickListener{
                val index = it.tag as Int?
                listener.onSuggestion
        }
    }



    }

}
