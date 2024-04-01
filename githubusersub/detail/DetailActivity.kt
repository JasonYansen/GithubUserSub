package com.example.githubusersub.detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.githubusersub.R
import com.example.githubusersub.data.local.DbModule
import com.example.githubusersub.data.local.FavoriteUser
import com.example.githubusersub.data.model.ResponseDetailUser
import com.example.githubusersub.databinding.ActivityDetailBinding
import com.example.githubusersub.detail.follow.FollowsFragment
import com.example.githubusersub.utils.MyResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        DetailViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("DEPRECATION")
        val item = intent.getParcelableExtra<FavoriteUser>("item")
        val username = item?.login ?: ""

        viewModel.resultDetailUser.observe(this){
            when(it){
                is MyResult.Success<*> -> {
                    val user = it.data as ResponseDetailUser
                    binding.image.load(user.avatarUrl) {
                        transformations(RoundedCornersTransformation(12f))
                    }

                    binding.nama.text =user.name
                    binding.name.text =user.login
                    binding.tvfollowers.text = "${user.followers} Followers"
                    binding.tvfollowing.text = "${user.following} Following"
                }
                is MyResult.Error -> {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
                is MyResult.Loading-> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getDetailUser(username)

        val fragments = mutableListOf<Fragment>(
            FollowsFragment.newInstance(FollowsFragment.FOLLOWERS),
            FollowsFragment.newInstance(FollowsFragment.FOLLOWING)
        )
        val titleFragments = mutableListOf(
            getString(R.string.followers), getString(R.string.following)
        )
        val adapter = DetailAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewpager) { tab, posisi ->
            tab.text = titleFragments[posisi]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewModel.getFollowers(username)

        viewModel.resultSuksesFavorite.observe(this) {
            binding.btnFavourite.changeIconColor(R.color.red)
        }

        viewModel.resultDeleteFavorite.observe(this) {
            binding.btnFavourite.changeIconColor(R.color.white)
        }

        binding.btnFavourite.setOnClickListener {
            viewModel.setFavorite(item)
        }

        viewModel.findFavorite(item?.id ?: 0)

        viewModel.isFavorite.observe(this) {
            binding.btnFavourite.changeIconColor(if (it) R.color.red else R.color.white)
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

fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}
