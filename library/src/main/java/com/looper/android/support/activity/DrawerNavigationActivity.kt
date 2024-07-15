package com.looper.android.support.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.looper.android.support.R
import com.looper.android.support.util.SystemServiceUtils


open class DrawerNavigationActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    protected lateinit var navController: NavController
    protected lateinit var navView: NavigationView
    protected lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set content view.
        setContentView(getContentView())

        // Initialize and set up the toolbar as the action bar.
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Find the navigation controller.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view_content_main) as NavHostFragment
        navController = navHostFragment.navController

        // Get the view of drawer.
        navView = findViewById(R.id.nav_view)

        // Set the header padding.
        val topExtraPadding = resources.displayMetrics.density * 8
        val topPadding = getStatusBarHeight() + topExtraPadding
        navView.getHeaderView(0).setPadding(0, topPadding.toInt(), 0, 0)
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    protected open fun setupNavigation(navGraphId: Int, menuId: Int) {
        // Inflate the navigation graph.
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(navGraphId)

        // Set the inflated graph as the graph for the navigation  controller.
        navController.graph = navGraph

        // Inflate the menu for the drawer.
        navView.inflateMenu(menuId)

        // Find the drawer layout.
        drawerLayout = findViewById(R.id.drawer_layout)

        // Fix the drawer header logo height.
        drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {}

            override fun onDrawerStateChanged(newState: Int) {
                if (newState != DrawerLayout.STATE_IDLE) {
                    SystemServiceUtils.hideKeyboard(this@DrawerNavigationActivity)
                }
            }
        })

        // Setup app bar with navigation controller.
        appBarConfiguration = AppBarConfiguration(navView.menu, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up drawer with navigation controller.
        navView.setupWithNavController(navController)
    }

    fun setDrawerFragmentTitle(fragmentId: Int, title: String) {
        navView.menu.findItem(fragmentId).title = title
    }

    override fun getContentView(): Int {
        return R.layout.drawer_navigation_activity
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}