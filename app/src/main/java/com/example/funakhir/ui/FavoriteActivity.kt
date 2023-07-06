package com.example.funakhir.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funakhir.R
import com.example.funakhir.databinding.ActivityFavoriteBinding
import com.example.funakhir.model.UsersItem

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        userAdapter = UserAdapter(ArrayList())

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersItem) {
                val moveToDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                moveToDetail.putExtra(DetailActivity.EXTRA_USER, data.username)
                startActivity(moveToDetail)
            }
        })

        favoriteBinding.apply {
            rvFavorite.adapter = userAdapter
            rvFavorite.setHasFixedSize(true)

            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvFavorite.layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
            } else {
                rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            }
        }

        viewModel.getFavoriteList()?.observe(this) {
            if (it != null) {
                userAdapter.setFavoriteList(it)

                if (it.isEmpty()) {
                    favoriteBinding.apply{
                        rvFavorite.visibility = View.GONE
                        tvChecker.visibility = View.VISIBLE
                    }
                } else {
                    favoriteBinding.apply{
                        rvFavorite.visibility = View.VISIBLE
                        tvChecker.visibility = View.GONE
                    }
                }
            }
        }

        val actionBar = supportActionBar
        if (actionBar != null) {
            title = getString(R.string.favorite)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val favoriteMenu = menu.findItem(R.id.menu_favorite)
        favoriteMenu.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                val moveToDetail = Intent(this@FavoriteActivity, SettingsActivity::class.java)
                startActivity(moveToDetail)

                true
            }
            else -> false
        }
    }
}