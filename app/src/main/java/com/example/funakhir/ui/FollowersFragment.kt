package com.example.funakhir.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funakhir.R
import com.example.funakhir.databinding.FragmentFollowersBinding
import com.example.funakhir.model.UsersItem

private const val KEY_BUNDLE = "username"

class FollowersFragment : Fragment() {
    private var username: String? = null

    private var  _binding : FragmentFollowersBinding? = null
    private val followersBinding get() = _binding!!

    private lateinit var viewModel: FollowersViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(KEY_BUNDLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowersBinding.bind(view)

        userAdapter = UserAdapter(ArrayList())
        userAdapter.notifyDataSetChanged()

        followersBinding.apply{
            rvFollowers.setHasFixedSize(true)
            rvFollowers.layoutManager = LinearLayoutManager(activity)
            rvFollowers.adapter = userAdapter
        }

        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowersViewModel::class.java]
        viewModel.apply {
            setFollowersUser(username.toString())
            getFollowersUser().observe(viewLifecycleOwner) {
                if (it != null) {
                    userAdapter.setList(it)
                    showLoading(false)
                }
            }
        }

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersItem) {
                val moveToDetail = Intent(activity, DetailActivity::class.java)
                moveToDetail.putExtra(DetailActivity.EXTRA_USER, data.username)
                startActivity(moveToDetail)
            }

        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            followersBinding.pbFollowers.visibility = View.VISIBLE
        }
        else {
            followersBinding.pbFollowers.visibility = View.GONE
            if (userAdapter.itemCount == 0) {
                followersBinding.apply{
                    rvFollowers.visibility = View.GONE
                    tvZeroFollowers.text = getString(R.string.zero_followers, username)
                    tvZeroFollowers.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(username: String): Fragment =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUNDLE, username)
                }
            }
    }
}