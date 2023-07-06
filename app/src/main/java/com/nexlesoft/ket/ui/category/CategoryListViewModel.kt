package com.nexlesoft.ket.ui.category

import androidx.lifecycle.*
import com.nexlesoft.ket.data.api.Resource
import com.nexlesoft.ket.data.model.CategoryItem
import com.nexlesoft.ket.data.repository.IRepository
import com.nexlesoft.ket.utils.convertApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val repository: IRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    private var _categoryList = MutableLiveData<Resource<List<CategoryItem>>>()
    val categoryList : LiveData<Resource<List<CategoryItem>>> = _categoryList

    fun fetchCategoryList() = viewModelScope.launch {
        getCategoryList()
    }

    private suspend fun getCategoryList() {
        _categoryList.postValue(Resource.Loading())
        try {
            val response = repository.getCategoryList()
            _categoryList.postValue(convertApiResponse(response))
        } catch (ex: Exception) {
            _categoryList.postValue(Resource.Error(ex.message.toString()))
        }
    }

}