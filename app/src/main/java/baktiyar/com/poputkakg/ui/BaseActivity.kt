package baktiyar.com.poputkakg.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.ui.login.LoginActivity
import baktiyar.com.poputkakg.ui.profile.ProfileActivity
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.Const.Companion.hideKeyboard


open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var rel: FrameLayout
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var mPrefs: SharedPreferences

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        rel = findViewById(R.id.content_frame)
        toolbar = findViewById(R.id.toolbar)
        navigationView.inflateHeaderView(R.layout.partial_nav_header);

        init()
    }

    private fun init() {
        mPrefs = this.getSharedPreferences(Const.PREFS_FILENAME, 0)

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
            R.id.navItemProfileInfo -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.navItemLogOut ->{
                mPrefs.edit().remove(Const.PREFS_CHECK_TOKEN).apply()
                mPrefs.edit().remove(Const.PREFS_CHECK_IS_DRIVER).apply()
                mPrefs.edit().remove(Const.PREFS_CHECK_USER_ID).apply()
                finish()  //Kill the activity from which you will go to next activity
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        return true

    }


    private fun checkActivity(o: Class<*>): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        return !cn.shortClassName.contains(o.simpleName)
    }

}
