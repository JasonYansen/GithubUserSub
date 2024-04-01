package com.example.githubusersub

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersub.data.local.FavoriteUser
import com.example.githubusersub.data.local.SettingPreferences
import com.example.githubusersub.data.model.ItemsItem
import com.example.githubusersub.databinding.ActivityMainBinding
import com.example.githubusersub.detail.DetailActivity
import com.example.githubusersub.favorite.FavoriteActivity
import com.example.githubusersub.setting.SettingActivity
import com.example.githubusersub.utils.MyResult

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by  lazy {
        UserAdapter { user ->
            val favoriteUser = FavoriteUser(user.login, user.id, user.avatarUrl)

            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", favoriteUser)
                startActivity(this)
            }
        }
    }
    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(3000)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter


        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getUser(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false

        })

        viewModel.resultUser.observe(this){
            when(it){
                is MyResult.Success<*> -> {
                    adapter.setData(it.data as MutableList<ItemsItem>)
                }
                is MyResult.Error -> {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
                is MyResult.Loading-> {
                    binding.progressBar.isVisible =it.isLoading
                }
            }
        }

        viewModel.getUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> {
                Intent(this, FavoriteActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.setting -> {
                Intent(this, SettingActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
