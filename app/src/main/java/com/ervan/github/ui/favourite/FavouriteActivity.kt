package com.ervan.github.ui.favourite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ervan.github.R
import com.ervan.github.data.database.FavouriteUser
import com.ervan.github.data.response.ItemsItem
import com.ervan.github.databinding.ActivityFavouriteBinding
import com.ervan.github.ui.Adapter.FavouriteAdapter
import com.ervan.github.ui.Adapter.UserAdapter
import com.ervan.github.ui.ViewModelFactory
import com.ervan.github.ui.detail.DetailActivity
import com.ervan.github.ui.model.FavouriteViewModel
import com.ervan.github.utils.Result

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding
    private val viewModel: FavouriteViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private val adapter by lazy {
        FavouriteAdapter {user ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("username", user.login)
                startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFavourite.layoutManager = LinearLayoutManager(this)
        binding.rvFavourite.adapter = adapter

        viewModel.getFavourite().observe(this) {
                adapter.setData(it as MutableList<FavouriteUser>)
        }
    }
}