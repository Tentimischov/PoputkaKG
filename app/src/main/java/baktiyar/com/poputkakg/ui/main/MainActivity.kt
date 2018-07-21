package baktiyar.com.poputkakg.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.ui.BaseActivity
import baktiyar.com.poputkakg.ui.map.MapViewActivity

class MainActivity : MapViewActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



}
