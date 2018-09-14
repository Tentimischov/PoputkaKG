package baktiyar.com.poputkakg.ui

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.ui.login.LoginActivity
import baktiyar.com.poputkakg.ui.suggestions.SuggestionActivity
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.Const.Companion.hideKeyboard
import de.hdodenhof.circleimageview.CircleImageView


open class BaseActivity : AppCompatActivity(){




    protected lateinit var mDrawer: DrawerLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var mIvUserImage: CircleImageView
    protected lateinit var mPrefs: SharedPreferences
    protected lateinit var navigationView:NavigationView
    protected lateinit var toggle: ActionBarDrawerToggle

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mToolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(mToolbar)
        mDrawer = findViewById(R.id.drawer_layout)
        navigationView  = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            onNavigationItemSelected(it)
        })
        init()
    }

    private fun init() {

        mPrefs = this.getSharedPreferences(Const.PREFS_FILENAME, 0)
        toggle = object : ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.app_name, R.string.app_name) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                hideKeyboard(this@BaseActivity)
            }
        }
        toggle.isDrawerSlideAnimationEnabled = false


        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setHomeButtonEnabled(true)
        mDrawer.addDrawerListener(toggle)
        toggle.syncState()
    }
     fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navItemImageMap ->
                mDrawer.closeDrawer(GravityCompat.START)
            R.id.navItemSuggestion ->
                startActivity(Intent(this, SuggestionActivity::class.java))
            R.id.navItemLogOut -> logOut()
        }
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }




override fun onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    private fun checkActivity(o: Class<*>): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        return !cn.shortClassName.contains(o.simpleName)
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.channel_name),name,importance)
            channel.description= description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
    }

    protected fun logOut() {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {

                    mPrefs.edit().remove(Const.PREFS_CHECK_TOKEN).apply()
                    mPrefs.edit().remove(Const.PREFS_CHECK_IS_DRIVER).apply()
                    mPrefs.edit().remove(Const.PREFS_CHECK_USER_ID).apply()
                    finish()  //Kill the activity from which you will go to next activity
                    startActivity(Intent(this, LoginActivity::class.java))

                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.dismiss()
                }

            }

        }

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.ask_sure_logout).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show()
    }

}
