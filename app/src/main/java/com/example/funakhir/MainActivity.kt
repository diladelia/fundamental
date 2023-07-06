package com.example.funakhir

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funakhir.databinding.ActivityMainBinding
import com.example.funakhir.model.UsersItem
import com.example.funakhir.ui.*
import com.example.funakhir.ui.DetailActivity


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        userAdapter = UserAdapter(ArrayList())

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersItem) {
                val moveToDetail = Intent(this@MainActivity, DetailActivity::class.java)
                moveToDetail.putExtra(DetailActivity.EXTRA_USER, data.username)
                startActivity(moveToDetail)
            }
        })

        mainBinding.apply {
            rvUser.adapter = userAdapter
            rvUser.setHasFixedSize(true)

            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvUser.layoutManager = GridLayoutManager(this@MainActivity, 2)
            } else {
                rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val searchQuery = query.toString()
                    if (searchQuery.isEmpty()) return false
                    showLoading(true)
                    viewModel.setSearchUser(searchQuery)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val searchQuery = newText.toString()
                    if (searchQuery.isEmpty()) return false
                    showLoading(true)
                    viewModel.setSearchUser(searchQuery)
                    return true
                }
            })
        }

        viewModel.getSearchUser().observe(this) {
            if (it!=null) {
                userAdapter.setList(it)
                showLoading(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                val moveToDetail = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(moveToDetail)

                true
            }
            R.id.menu_settings -> {
                val moveToDetail = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(moveToDetail)

                true
            }
            else -> false
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            mainBinding.apply {
                tvChecker.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        }
        else {
            mainBinding.apply {
                progressBar.visibility = View.GONE
                rvUser.visibility = View.VISIBLE
            }
            if (userAdapter.itemCount == 0) {
                mainBinding.apply{
                    rvUser.visibility = View.GONE
                    tvChecker.text = getString(R.string.not_found)
                    tvChecker.visibility = View.VISIBLE
                }
            }
        }
    }
}