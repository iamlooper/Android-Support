package com.looper.android.support.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.AppBarLayout

import com.google.android.material.appbar.MaterialToolbar

import com.looper.android.support.R

open class JustNavigationActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set content view.
        setContentView(getContentView())

        // Initialize and set up the toolbar as the action bar.
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Find the navigation controller.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Consumes extra top padding caused by system windows.
        val navHostFragmentParent: ConstraintLayout = findViewById(R.id.nav_host_fragment_parent)
        navHostFragmentParent.viewTreeObserver.addOnGlobalLayoutListener {
            navHostFragmentParent.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = -navHostFragmentParent.paddingTop
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) leftMargin =
                    -navHostFragmentParent.paddingLeft
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) rightMargin =
                    -navHostFragmentParent.paddingRight
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    protected fun setupNavigation(navGraphId: Int, fragmentId: Int) {
        // Inflate the navigation graph.
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(navGraphId)

        // Set the inflated navigation graph as the graph for the navigation controller.
        navController.graph = navGraph

        // Setup app bar with navigation controller.
        appBarConfiguration = AppBarConfiguration(navGraph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Navigate to given fragment.
        navController.navigate(fragmentId)
    }

    protected open fun getContentView(): Int {
        return R.layout.just_navigation_activity
    }

    fun setAppBarLiftOnScroll(targetViewId: Int?) {
        val appbar = findViewById<AppBarLayout>(R.id.app_bar_layout)
        targetViewId?.let {
            appbar?.isLiftOnScroll = true
            appbar?.liftOnScrollTargetViewId = it
        }
    }
}