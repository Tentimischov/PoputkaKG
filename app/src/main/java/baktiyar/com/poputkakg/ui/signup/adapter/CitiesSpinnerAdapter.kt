package baktiyar.com.poputkakg.ui.signup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.City

class CitiesSpinnerAdapter(mContext: Context, var mCityList: List<City>)
    : ArrayAdapter<City>(mContext, 0, mCityList) {



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false)
        val textView = view?.findViewById<TextView>(R.id.tvCityItem)
        textView?.text = getItem(position)?.name

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val city = getItem(position)
        var view = convertView
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false)
        val textView = view?.findViewById<TextView>(R.id.tvCityItem)
        textView?.text = city?.name
        return view
    }
}
