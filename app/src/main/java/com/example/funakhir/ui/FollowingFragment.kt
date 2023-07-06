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
import com.example.funakhir.databinding.FragmentFollowingBinding
import com.example.funakhir.model.UsersItem


private const val KEY_BUNDLE = "username"

class FollowingFragment : Fragment() {
    private var username: String? = null

    private var  _binding : FragmentFollowingBinding? = null
    private val followingBinding get() = _binding!!

    private lateinit var viewModel: FollowingViewModel
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
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowingBinding.bind(view)

        userAdapter = UserAdapter(ArrayList())
        userAdapter.notifyDataSetChanged()

        followingBinding.apply{
            rvFollowing.setHasFixedSize(true)
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.adapter = userAdapter
        }

        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowingViewModel::class.java]
        viewModel.apply {
            setFollowingUser(username.toString())
            getFollowingUser().observe(viewLifecycleOwner) {
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
            followingBinding.pbFollowing.visibility = View.VISIBLE
        }
        else {
            followingBinding.pbFollowing.visibility = View.GONE
            if (userAdapter.itemCount == 0) {
                followingBinding.apply{
                    rvFollowing.visibility = View.GONE
                    tvZeroFollowing.text = getString(R.string.zero_following, username)
                    tvZeroFollowing.visibility = View.VISIBLE
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
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUNDLE, username)
                }
            }
    }
}