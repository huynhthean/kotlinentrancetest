package com.nexlesoft.ket.ui.signup

import androidx.lifecycle.*
import com.nexlesoft.ket.data.api.Resource
import com.nexlesoft.ket.data.model.SignInRequest
import com.nexlesoft.ket.data.model.SignInResponse
import com.nexlesoft.ket.data.model.SignUpRequest
import com.nexlesoft.ket.data.model.SignUpResponse
import com.nexlesoft.ket.data.repository.IRepository
import com.nexlesoft.ket.utils.convertApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    private val _signUpResponse = MutableLiveData<Resource<SignUpResponse>>()
    val signUpResponse: LiveData<Resource<SignUpResponse>> = _signUpResponse

    private val _signInResponse = MutableLiveData<Resource<SignInResponse>>()
    val signInResponse: LiveData<Resource<SignInResponse>> = _signInResponse

    fun createNewUser(email: String, password: String) = viewModelScope.launch {
        signUp(email, password)
    }

    fun loginWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signIn(email, password)
    }

    private suspend fun signUp(email: String, password: String) {
        val payload = SignUpRequest(
            email, password, "TesterA", "Mrs"
        )
        _signUpResponse.postValue(Resource.Loading())
        try {
            val response = repository.signUp(payload)
            _signUpResponse.postValue(convertApiResponse(response))
        } catch (ex: Exception) {
            _signUpResponse.postValue(Resource.Error(ex.message.toString()))
        }
    }

    private suspend fun signIn(email: String, password: String) {
        val payload = SignInRequest(email, password)
        _signInResponse.postValue(Resource.Loading())
        try {
            val response = repository.signIn(payload)
            _signInResponse.postValue(convertApiResponse(response))
        } catch (ex: Exception) {
            _signInResponse.postValue(Resource.Error(ex.message.toString()))
        }
    }
}