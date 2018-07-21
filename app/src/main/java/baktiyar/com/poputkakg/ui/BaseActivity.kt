package baktiyar.com.poputkakg.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.ui.profile.ProfileActivity
import baktiyar.com.poputkakg.util.Const.Companion.hideKeyboard
import de.hdodenhof.circleimageview.CircleImageView


open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var rel: FrameLayout
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
//    private lateinit var profile: RelativeLayout

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        rel = findViewById(R.id.content_frame)
        toolbar = findViewById(R.id.toolbar)
        navigationView.inflateHeaderView(R.layout.partial_nav_header);
/*
        profile = findViewById<RelativeLayout>(R.id.profile)
        profile.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }*/
        init()
    }

    private fun init() {
        drawerLayout.openDrawer(GravityCompat.START)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val id = intent.getIntExtra("id", R.id.navItemSettingsStage)

        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                hideKeyboard(this@BaseActivity)
            }
        }


        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(id)

    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        val id: Int = item.itemId
        when (id) {

        }
        return true

    }


    private fun checkActivity(o: Class<*>): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        return !cn.shortClassName.contains(o.simpleName)
    }

}
