package baktiyar.com.poputkakg.ui.suggestions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.model.suggestion.Suggestion
import baktiyar.com.poputkakg.model.suggestion.SuggestionList
import baktiyar.com.poputkakg.util.Const
import kotlinx.android.synthetic.main.activity_suggestion.*

class SuggestionActivity :AppCompatActivity(),SuggestionContract.View ,SuggestionAdapter.Listener{



    lateinit var mPresenter : SuggestionPresenter
    lateinit var mIncomingDealAdapter: SuggestionAdapter
    lateinit var mOutgoingDealAdapter: SuggestionAdapter

    lateinit var mRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion)
        init()


    }
    override fun onSuccessGetDeals(list: SuggestionList) {
        val allDeals = list.incomingList
        allDeals!!.addAll(list.outgoingList!!)
        incomingRecycler.adapter = SuggestionAdapter(list.incomingList!!,this)
        outgoingRecycler.adapter = SuggestionAdapter(list.outgoingList!!,this)
        initButtonListener(allDeals)

    }

    private fun init() {
        mPresenter = SuggestionPresenter(this)
        val token = getSharedPreferences(Const.PREFS_FILENAME,0).getString(Const.PREFS_CHECK_TOKEN,"null")
        mPresenter.getDeals(this,Const.TOKEN_PREFIX+token)


    }

    private fun initButtonListener(allDeals: ArrayList<Suggestion>) {
        tvMy.setOnClickListener{
            setupMyOwns(true,allDeals)
        }
        tvCommon.setOnClickListener {
            setupMyOwns(false,allDeals)
        }
        imgArrow.setOnClickListener{
            onBackPressed()
        }
    }

    private fun setupMyOwns(my: Boolean, allDeals: ArrayList<Suggestion>) {
        if(my){
            tvMy.setTextColor(resources.getColor(R.color.colorPrimaryDark))
            tvCommon.setTextColor(resources.getColor(R.color.gray))
            recyclerView.visibility = View.GONE
            incomingPart.visibility = View.VISIBLE
            outgoingPart.visibility = View.VISIBLE
            incomingRecycler.visibility = View.VISIBLE
            outgoingRecycler.visibility = View.VISIBLE
        }
        else{

            tvMy.setTextColor(resources.getColor(R.color.gray))
            tvCommon.setTextColor(resources.getColor(R.color.colorPrimaryDark))
            recyclerView.visibility = View.VISIBLE
            incomingPart.visibility = View.GONE
            outgoingPart.visibility = View.GONE
            incomingRecycler.visibility = View.GONE
            outgoingRecycler.visibility = View.GONE
            recyclerView.adapter = SuggestionAdapter(allDeals,this)

        }
    }

    private fun initAdapter() {
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
    override fun onDealClicked(routeId: Int?) {

    }

}