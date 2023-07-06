package com.example.funakhir.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.funakhir.R
import com.example.funakhir.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val username = intent.getStringExtra(EXTRA_USER)

        Toast.makeText(this@DetailActivity, getString(R.string.profile, username), Toast.LENGTH_SHORT).show()

        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        showLoading(true)
        username?.let { viewModel.setDetailUser(it) }
        viewModel.getDetailUser().observe(this) {
            if (it != null) {
                showLoading(false)
                detailBinding.apply {
                    tvNameReceived.text = it.name
                    tvUsernameReceived.text = it.username
                    tvFollowersReceived.text = it.followers.toString()
                    tvFollowingReceived.text = it.following.toString()
                    tvRepositoryReceived.text = it.repository.toString()

                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .into(civAvatarReceived)

                    var stateFav = false

                    CoroutineScope(Dispatchers.IO).launch {
                        val checkId = viewModel.getFavoriteDetail(it.username!!)
                        withContext(Dispatchers.Main){
                            if (checkId != null) {
                                stateFav = if (checkId > 0) {
                                    includeFavorite.fab.setImageResource(R.drawable.ic_favorite)
                                    includeFavorite.fab.setColorFilter(ContextCompat.getColor(this@DetailActivity, R.color.red))
                                    true
                                } else {
                                    includeFavorite.fab.setImageResource(R.drawable.ic_favorite)
                                    includeFavorite.fab.setColorFilter(ContextCompat.getColor(this@DetailActivity, R.color.grey))
                                    false
                                }
                            }
                        }
                    }

                    includeFavorite.fab.setOnClickListener { view ->
                        if (!stateFav) {
                            it.isFavorite = true
                            stateFav = true
                            includeFavorite.fab.setImageResource(R.drawable.ic_favorite)
                            includeFavorite.fab.setColorFilter(ContextCompat.getColor(this@DetailActivity, R.color.red))
                            viewModel.insertToFavorite(it)
                            Snackbar.make(view, "User Favorited", Snackbar.LENGTH_SHORT)
                                .show()
                        } else {
                            it.isFavorite = false
                            stateFav = false
                            includeFavorite.fab.setImageResource(R.drawable.ic_favorite)
                            includeFavorite.fab.setColorFilter(ContextCompat.getColor(this@DetailActivity, R.color.grey))
                            viewModel.deleteFromFavorite(it)
                            Snackbar.make(view, "User Unfavorited", Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        val followsPagerAdapter = FollowsPagerAdapter(this, username.toString())
        val viewPager: ViewPager2 = detailBinding.viewPager
        viewPager.adapter = followsPagerAdapter
        val tabs: TabLayout = detailBinding.tabs

        TabLayoutMediator(tabs,viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val actionBar = supportActionBar
        if (actionBar != null) {
            title = getString(R.string.profile_detail, username)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home)
            actionBar.setDisplayHomeAsUpEnabled(true)
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
                val moveToDetail = Intent(this@DetailActivity, FavoriteActivity::class.java)
                startActivity(moveToDetail)

                true
            }
            R.id.menu_settings -> {
                val moveToDetail = Intent(this@DetailActivity, SettingsActivity::class.java)
                startActivity(moveToDetail)

                true
            }
            else -> false
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            detailBinding.apply {
                civAvatarReceived.visibility = View.GONE
                tvNameReceived.visibility = View.GONE
                tvUsernameReceived.visibility = View.GONE
                tvRepositoryReceived.visibility = View.GONE
                tvFollowersReceived.visibility = View.GONE
                tvFollowingReceived.visibility = View.GONE
                tvRepository.visibility = View.GONE
                tvFollowers.visibility = View.GONE
                tvFollowing.visibility = View.GONE
                div1.visibility = View.GONE
                div2.visibility = View.GONE
                div3.visibility = View.GONE
                div4.visibility = View.GONE
                tabs.visibility = View.GONE
                viewPager.visibility = View.GONE
                includeFavorite.fab.visibility = View.GONE
                pbDetail.visibility = View.VISIBLE
            }
        }
        else {
            detailBinding.apply {
                civAvatarReceived.visibility = View.VISIBLE
                tvNameReceived.visibility = View.VISIBLE
                tvUsernameReceived.visibility = View.VISIBLE
                tvRepositoryReceived.visibility = View.VISIBLE
                tvFollowersReceived.visibility = View.VISIBLE
                tvFollowingReceived.visibility = View.VISIBLE
                tvRepository.visibility = View.VISIBLE
                tvFollowers.visibility = View.VISIBLE
                tvFollowing.visibility = View.VISIBLE
                div1.visibility = View.VISIBLE
                div2.visibility = View.VISIBLE
                div3.visibility = View.VISIBLE
                div4.visibility = View.VISIBLE
                tabs.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                includeFavorite.fab.visibility = View.VISIBLE
                pbDetail.visibility = View.GONE
            }
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}