package com.example.test1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.adapter.UserAdapter
import com.example.test1.databinding.ActivityThirdScreenBinding
import com.example.test1.model.User
import kotlinx.coroutines.launch

class ThirdScreen : AppCompatActivity() {

    private lateinit var binding: ActivityThirdScreenBinding
    private lateinit var userAdapter: UserAdapter
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Third Screen"

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter { user ->
            returnSelectedUser(user)
        }
        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ThirdScreen)
            adapter = userAdapter
        }
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.userRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition >= totalItemCount - 2) {
                    viewModel.loadMoreUsers()
                }
            }
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.swipeRefreshLayout.isRefreshing = false
                    when (state) {
                        is UserUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.emptyStateTextView.visibility = View.GONE
                            binding.userRecyclerView.visibility = View.GONE
                        }
                        is UserUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            if (state.users.isEmpty()) {
                                binding.emptyStateTextView.visibility = View.VISIBLE
                                binding.userRecyclerView.visibility = View.GONE
                            } else {
                                binding.emptyStateTextView.visibility = View.GONE
                                binding.userRecyclerView.visibility = View.VISIBLE
                                userAdapter.submitList(state.users)
                            }
                        }
                        is UserUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.emptyStateTextView.visibility = View.VISIBLE
                            binding.emptyStateTextView.text = state.message
                            Toast.makeText(this@ThirdScreen, state.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun returnSelectedUser(user: User) {
        val intent = Intent().apply {
            putExtra("EXTRA_SELECTED_USER_NAME", "${user.firstName} ${user.lastName}")
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}