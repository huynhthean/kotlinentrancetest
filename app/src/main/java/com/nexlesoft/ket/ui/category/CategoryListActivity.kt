package com.nexlesoft.ket.ui.category

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.nexlesoft.ket.R
import com.nexlesoft.ket.data.api.Resource
import com.nexlesoft.ket.databinding.ActivityCategoryListBinding
import com.nexlesoft.ket.utils.setWindowFullScreen
import com.nexlesoft.ket.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryListBinding

    private val viewModel: CategoryListViewModel by viewModels()

    private var categoryListAdapter: CategoryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowFullScreen()
        bindUI()
    }

    private fun bindUI() {
        binding.btnDone.setOnClickListener {
            categoryListAdapter?.let {
                if(it.hasSelectedItem()) {
                    toast("Button Enabled")
                }
            }
        }
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.categoryList.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    hideProgressBar()
                    val categoryList = result.data!!
                    categoryListAdapter = CategoryListAdapter(
                        this@CategoryListActivity,
                        R.layout.category_list_item_layout,
                        categoryList
                    )
                    binding.gvCategoryList.adapter = categoryListAdapter
                }
                is Resource.Error -> {
                    hideProgressBar()
                    toast(result.message.toString())
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
        viewModel.fetchCategoryList()
    }

    private fun hideProgressBar() {
        binding.pbLoading.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.pbLoading.visibility = View.VISIBLE
    }
}