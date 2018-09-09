package baktiyar.com.poputkakg.ui

import android.app.ActivityManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import baktiyar.com.poputkakg.R
import baktiyar.com.poputkakg.ui.login.LoginActivity
import baktiyar.com.poputkakg.ui.profile.ProfileActivity
import baktiyar.com.poputkakg.ui.suggestions.SuggestionActivity
import baktiyar.com.poputkakg.util.Const
import baktiyar.com.poputkakg.util.Const.Companion.hideKeyboard
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mFrameLayout: FrameLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mNavView: NavigationView
    private lateinit var mHeaderView: View
    private lateinit var mIvUserImage: CircleImageView
    private lateinit var mTvUserName: TextView
    private lateinit var mTvSuggestion: MenuItem
    private lateinit var mTvUserStatus: TextView


    protected lateinit var mPrefs: SharedPreferences

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initializeViews()
        initializeHeaderViews()
        mTvSuggestion = mNavView.menu.findItem(R.id.navItemSettingsStage)

        Glide.with(this)
                .load("https://www.w3schools.com/w3css/img_lights.jpg")
                .into(mIvUserImage)




        mTvSuggestion.setOnMenuItemClickListener {
            startIntent(SuggestionActivity::class.java)
        }

        init()
    }

    private fun initializeHeaderViews() {
        mHeaderView = mNavView.getHeaderView(0)
        mIvUserImage = mHeaderView.findViewById(R.id.ivProfileHeaderImage) as CircleImageView
        mTvUserName = mHeaderView.findViewById(R.id.tvUserName) as TextView
        mTvUserStatus = mHeaderView.findViewById(R.id.tvUserStatus) as TextView

        mTvUserName.text = "textName"
        mTvUserStatus.text = "textStatus"
        mTvUserName.setOnClickListener {startIntent(ProfileActivity::class.java) }

    }

    private fun initializeViews() {
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavView = findViewById(R.id.nav_view)
        mFrameLayout = findViewById(R.id.content_frame)
        mToolbar = findViewById(R.id.toolbar)
        mNavView.inflateHeaderView(R.layout.partial_nav_header)
    }

    private fun startIntent(java: Class<*>): Boolean {
        startActivity(Intent(this,java))
        return true
    }

    private fun init() {
        mPrefs = this.getSharedPreferences(Const.PREFS_FILENAME, 0)

        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val id = intent.getIntExtra("id", R.id.navItemSettingsStage)

        val toggle = object : ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                hideKeyboard(this@BaseActivity)
            }
        }


        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
//        mToolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP)

        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mNavView.setNavigationItemSelectedListener(this)
        mNavView.setCheckedItem(id)

    }


    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mDrawerLayout.closeDrawer(GravityCompat.START)
        val id: Int = item.itemId
        when (id) {
            R.id.navItemLogOut -> {
                logOut()
            }
        }
        return true

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

    private fun logOut() {
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
