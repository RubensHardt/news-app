package com.rubenshardt.newsapp.data.modules.list

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.rubenshardt.newsapp.R
import com.rubenshardt.newsapp.data.modules.list.ui.ArticlesListAdapter
import com.rubenshardt.newsapp.databinding.ActivityListBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var adapter: ArticlesListAdapter
    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listViewModel = ViewModelProvider(this)[ListViewModel::class.java]

        setupToolbar()
        setupNavView()

        adapter = ArticlesListAdapter {}
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            listViewModel.refresh()
        }
        setupObservers()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupNavView() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        navView.setNavigationItemSelectedListener { item ->
            listViewModel.selectCategory(item.title.toString().lowercase(Locale.getDefault()))
            item.isChecked = true
            title = item.title
            drawerLayout.closeDrawers()
            true
        }

        navView.setCheckedItem(R.id.menu_all)
        val category = navView.checkedItem?.title.toString().lowercase(Locale.getDefault())
        listViewModel.selectCategory(category)
        title = category

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, binding.appBarMain.toolbar, R.string.drawer_open,  R.string.drawer_close)
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()
        drawerLayout.addDrawerListener(drawerToggle)
    }

    fun setupObservers() {
        listViewModel.articlesListState.observe(this) {
            with(binding) {
                swipeRefresh.isRefreshing = it.isLoading
                emptyTextView.isVisible = it.isEmpty
                if (it.error?.showError == true) {
                    it.error.showError = false
                    Toast.makeText(this@ListActivity, it.error.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            adapter.submitList(it.articles)
        }
    }
}