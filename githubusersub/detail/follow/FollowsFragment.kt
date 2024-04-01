package com.example.githubusersub.detail.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersub.UserAdapter
import com.example.githubusersub.data.model.ItemsItem
import com.example.githubusersub.databinding.FragmentFollowsBinding
import com.example.githubusersub.detail.DetailViewModel
import com.example.githubusersub.utils.MyResult

class FollowsFragment : Fragment() {

    private var binding: FragmentFollowsBinding? = null
    private val adapter by lazy {
        UserAdapter{

        }
    }
    private val viewModel by activityViewModels<DetailViewModel>()
    private var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollows?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowsFragment.adapter
        }

        when(type) {
            FOLLOWERS -> {
                viewModel.resultFollowersUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
            FOLLOWING -> {
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
        }
    }

    private fun manageResultFollows(state: MyResult) {
        when(state){
            is MyResult.Success<*> -> {
                adapter.setData(state.data as MutableList<ItemsItem>)
            }
            is MyResult.Error -> {
                Toast.makeText(requireActivity(), state.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            is MyResult.Loading-> {
                binding?.progressBar?.isVisible =state.isLoading
            }
        }
    }
    companion object {
        const val FOLLOWING = 100
        const val FOLLOWERS = 101

        fun newInstance(type: Int) = FollowsFragment()
            .apply {
                this.type = type
            }
    }
}