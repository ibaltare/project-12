package com.keepcoding.navi.dragonball.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepcoding.navi.dragonball.utils.Constants
import com.keepcoding.navi.dragonball.utils.MainActivityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException


class LoginViewModel : ViewModel() {

    val stateLiveData : MutableLiveData<MainActivityState> by lazy {
        MutableLiveData<MainActivityState>()
    }

    fun doLogin(user: String, pass: String){
        setValueOnMainThread(MainActivityState.Loading)
        val client = OkHttpClient()
        val url = Constants.URL_LOGIN
        val credentials = Credentials.basic(user,pass)
        val formBody = FormBody.Builder().build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", credentials)
            .post(formBody)
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object: Callback {

                override fun onFailure(call: Call, e: IOException) {
                    setValueOnMainThread(MainActivityState.Error(e.message.toString()))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200){
                        setValueOnMainThread(MainActivityState.Success(response.body?.string()))
                    }else{
                        setValueOnMainThread(MainActivityState.Error("Usuario no autenticado"))
                    }
                }
            }
        )

    }

    fun setValueOnMainThread(value: MainActivityState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }
}