package com.ervan.github.ui.main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ervan.github.R
import com.ervan.github.data.response.ItemsItem
import com.ervan.github.databinding.ActivityMainBinding
import com.ervan.github.ui.Adapter.UserAdapter
import com.ervan.github.ui.ViewModelFactory
import com.ervan.github.ui.detail.DetailActivity
import com.ervan.github.ui.favourite.FavouriteActivity
import com.ervan.github.ui.model.MainViewModel
import com.ervan.github.ui.model.SettingViewModel
import com.ervan.github.ui.theme.SettingActivity
import com.ervan.github.utils.Result


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        UserAdapter {user ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("username", user.login)
                startActivity(this)
            }
        }
    }
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private val settingViewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userRecycleView.layoutManager = LinearLayoutManager(this)
        binding.userRecycleView.setHasFixedSize(true)

        binding.userRecycleView.adapter = adapter

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            val mode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        binding.userSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewModel.getUser(p0.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        viewModel.resultUser.observe(this) {
            when(it) {
                is Result.Success<*> -> {
                    adapter.setData(it.data as MutableList<ItemsItem>)
                }
                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
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
        when(item.itemId) {
            R.id.favourite -> {
                var intent = Intent(this@MainActivity, FavouriteActivity::class.java)
                startActivity(intent)
            }

            R.id.setting -> {
                var intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}