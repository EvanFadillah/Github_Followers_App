package com.ervan.github.ui.detail.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ervan.github.data.response.ItemsItem
import com.ervan.github.databinding.FragmentFollowBinding
import com.ervan.github.ui.Adapter.UserAdapter
import com.ervan.github.ui.model.DetailViewModel
import com.ervan.github.utils.Result


class FollowFragment : Fragment() {

    private var binding: FragmentFollowBinding? = null
    private val adapter by lazy {
        UserAdapter {

        }
    }

    private val viewModel by activityViewModels<DetailViewModel>()
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollows?.apply{
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowFragment.adapter
        }


        when(type) {
            FOLLOWERS -> {
                viewModel.resultFollowersUser.observe(viewLifecycleOwner, this:: manageResultFollows)
            }
            FOLLOWING -> {
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this:: manageResultFollows)
            }
        }


    }
    private fun manageResultFollows(state: Result) {
        when(state) {
            is Result.Success<*> -> {
                adapter.setData(state.data as MutableList<ItemsItem>)
            }
            is Result.Error -> {
                Toast.makeText(requireActivity(), state.exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
            is Result.Loading -> {
                binding?.progressBarFragment?.isVisible = state.isLoading
            }
        }
    }

    companion object {
        const val FOLLOWING = 100
        const val FOLLOWERS = 101
        fun newInstance(type: Int) = FollowFragment()
            .apply {
                this.type = type
            }
    }
}