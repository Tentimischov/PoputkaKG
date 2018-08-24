package baktiyar.com.poputkakg.ui.profile.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.History
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter(var mList: MutableList<History>, var mListener: OnHistoryItemClickListener) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCard(mList[position], mListener)
    }

    interface OnHistoryItemClickListener {

    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bindCard(history: History, listener: OnHistoryItemClickListener) {
            itemView.tvHistoryUserName.text = history.grader
            itemView.tvHistoryComment.text = history.comment
            itemView.tvHistoryTime.text = history.dealCreated

            itemView.rbHistoryRating.rating = (history.grade!! / 2).toFloat()

            itemView.setOnClickListener {

            }
        }
    }

}