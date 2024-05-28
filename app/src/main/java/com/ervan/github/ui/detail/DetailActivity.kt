package com.ervan.github.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.ervan.github.R
import com.ervan.github.data.database.FavouriteUser
import com.ervan.github.data.response.DetailResponse
import com.ervan.github.databinding.ActivityDetailBinding
import com.ervan.github.ui.Adapter.DetailAdapter
import com.ervan.github.ui.ViewModelFactory
import com.ervan.github.ui.detail.follow.FollowFragment
import com.ervan.github.ui.favourite.FavouriteActivity
import com.ervan.github.ui.model.DetailViewModel
import com.ervan.github.ui.model.MainViewModel
import com.ervan.github.utils.Result
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra("username") ?: ""

        viewModel.resultDetailUser.observe(this) {
            when(it) {
                is Result.Success<*> -> {
                    val user = it.data as DetailResponse
                    binding.detailImageView.load(user.avatarUrl){
                        transformations(CircleCropTransformation())
                    }
                    binding.nameTextView.text = user.login
                    binding.usernameTextView.text = user.name
                    binding.fllwersNumTextView.text = user.followers.toString()
                    binding.fllwngNumTextView.text = user.following.toString()

                    var isFavorited = false
                    var favorit = FavouriteUser(login = user.login, avatarUrl = user.avatarUrl)

                    viewModel.findByLogin(user.login)

                    viewModel.favUser.observe(this@DetailActivity) { favUser ->
                        if (favUser != null) {
                            favorit = favUser
                            isFavorited = true
                            binding.fabFavorit.setImageResource(R.drawable.baseline_favorite_24)
                        } else {
                            isFavorited = false
                            binding.fabFavorit.setImageResource(R.drawable.baseline_favorite_border_24)

                        }
                    }

                    binding.fabFavorit.setOnClickListener {
                        if (!isFavorited) {
                            viewModel.insert(favorit)
                            isFavorited = true
                            binding.fabFavorit.setImageResource(R.drawable.baseline_favorite_24)
                        } else {
                            viewModel.delete(favorit)
                            isFavorited = false
                            binding.fabFavorit.setImageResource(R.drawable.baseline_favorite_border_24)
                        }
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBarDetail.isVisible = it.isLoading
                }
            }
        }
        viewModel.getDetailUser(username)

        val fragment = mutableListOf<Fragment>(
            FollowFragment.newInstance(FollowFragment.FOLLOWERS),
            FollowFragment.newInstance(FollowFragment.FOLLOWING)
            )
        val titleFragments = mutableListOf(
            getString(R.string.followers),
            getString(R.string.following)
        )
        val adapter = DetailAdapter(this, fragment)
        binding.detailViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.detailViewPager) { tab, position ->
            tab.text = titleFragments[position]
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == 0) {
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

