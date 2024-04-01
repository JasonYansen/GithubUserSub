package com.example.githubusersub.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersub.UserAdapter
import com.example.githubusersub.data.local.DbModule
import com.example.githubusersub.data.local.FavoriteUser
import com.example.githubusersub.data.model.ItemsItem
import com.example.githubusersub.databinding.ActivityFavoriteBinding
import com.example.githubusersub.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val adapter by  lazy {
        UserAdapter { user ->
            val favoriteUser = FavoriteUser(user.login, user.id, user.avatarUrl)

            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", favoriteUser)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteUsers()
    }

    private fun loadFavoriteUsers() {
        viewModel.getUserFavorite().observe(this) { favoriteList: MutableList<FavoriteUser> ->
            adapter.setData(favoriteList.map {
                ItemsItem(login = it.login, id = it.id, avatarUrl = it.avatarUrl)
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}