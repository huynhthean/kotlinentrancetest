package com.nexlesoft.ket.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.nexlesoft.ket.data.api.Resource
import retrofit2.Response

fun Context.toast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}

fun Activity.setWindowFullScreen() {
    val window: Window = window
    val winParams: WindowManager.LayoutParams = window.attributes
    winParams.flags =
        winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
    window.attributes = winParams
    window.decorView
        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
}

fun <T> ViewModel.convertApiResponse(response: Response<T>): Resource<T> {
    if (response.isSuccessful) {
        response.body()?.let { data ->
            return Resource.Success(data)
        }
    }
    return Resource.Error(response.message())
}

//fun Context.showKeyBoard(searchView: TextInputEditText) {
//    searchView.apply {
//        text = null
//        requestFocus()
//        val imm =
//            this@showKeyBoard.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
//        imm!!.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
//    }
//
//}
//
//fun Activity.hideSoftKeyboard() {
//    val view = this.currentFocus
//    view?.let {
//        val imm =
//            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }
//}