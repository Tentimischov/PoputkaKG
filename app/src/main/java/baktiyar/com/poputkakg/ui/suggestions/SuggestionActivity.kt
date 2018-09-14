package baktiyar.com.poputkakg.ui.suggestions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.suggestion.Suggestion
import baktiyar.com.poputkakg.model.suggestion.SuggestionList
import baktiyar.com.poputkakg.util.Const
import kotlinx.android.synthetic.main.activity_suggestion.*

class SuggestionActivity :AppCompatActivity(),SuggestionContract.View ,SuggestionAdapter.Listener{

    lateinit var mToken:String
    lateinit var mPresenter : SuggestionPresenter
    lateinit var mDeals:SuggestionList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion)
        init()


    }
    override fun onSuccessGetOwnRoutes(list: ArrayList<Suggestion>) {
        recyclerView.adapter = SuggestionAdapter(list,this)
        recyclerView.visibility = View.VISIBLE
    }

    override fun onSuccessGetDealsWithClosestDepartureTime(list: ArrayList<Suggestion>) {

        recyclerView.adapter = SuggestionAdapter(list ,this)
        recyclerView.visibility = View.VISIBLE


    }
    override fun onSuccessGetDeals(list: SuggestionList) {
        mDeals = list
        val allDeals = list.incomingList
        allDeals!!.addAll(list.outgoingList!!)
        iwFilter.isClickable = true
        recyclerView.adapter = SuggestionAdapter(list.outgoingList!!,this)

    }

    private fun init() {
        iwFilter.isClickable = false
        initButtonListener()
        mPresenter = SuggestionPresenter(this)
        mToken =Const.TOKEN_PREFIX+ getSharedPreferences(Const.PREFS_FILENAME,0).getString(Const.PREFS_CHECK_TOKEN,"null")
        setupMyOwns(false)


    }

    private fun initButtonListener() {
        tvCommon.setOnClickListener{
            setupMyOwns(false)
        }
        imgArrow.setOnClickListener{
            onBackPressed()
        }
        tvMy.setOnClickListener {
            setupMyOwns(true)
        }
        iwFilter.setOnClickListener {
            if(filter.visibility == View.VISIBLE)
                filter.visibility = View.GONE
            else {
                filter.visibility = View.VISIBLE
                filter.animate()
            }
        }
        tvOutgoing.setOnClickListener {
            initOutgoingRecycler()
            filter.visibility = View.GONE
        }
        tvIncoming.setOnClickListener {
            initIncomingRecycler()
            filter.visibility = View.GONE
        }
        tvCreated.setOnClickListener {
            mPresenter.getOwnRoutes(this,mToken)
            filter.visibility = View.GONE

        }
        }

    private fun initIncomingRecycler() {
        recyclerView.adapter = SuggestionAdapter(mDeals.incomingList!!,this)
        recyclerView.visibility = View.VISIBLE

    }

    private fun initOutgoingRecycler() {
        recyclerView.adapter = SuggestionAdapter(mDeals.outgoingList!!,this)
        recyclerView.visibility = View.VISIBLE
    }


    private fun setupMyOwns(my: Boolean) {
        if(my){
            tvMy.setTextColor(resources.getColor(R.color.colorAccent))
            tvCommon.setTextColor(resources.getColor(R.color.gray))
            mPresenter.getDeals(this,mToken)
            iwFilter.visibility = View.VISIBLE


        }
        else{
            iwFilter.visibility = View.GONE
            tvMy.setTextColor(resources.getColor(R.color.gray))
            tvCommon.setTextColor(resources.getColor(R.color.colorAccent))
            iwFilter.visibility = View.GONE
            mPresenter.getDealsWithClosestDepartureTime(this,mToken)



        }
    }



    override fun showProgress() {
    }

    override fun hideProgress() {
    }
    override fun onDealClicked(routeId: Int?) {

    }

}