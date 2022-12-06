package com.keepcoding.navi.dragonball.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepcoding.navi.dragonball.models.Constants
import com.keepcoding.navi.dragonball.models.MainActivityState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException


class LoginViewModel : ViewModel() {

    val stateLiveData : MutableLiveData<MainActivityState> by lazy {
        MutableLiveData<MainActivityState>()
    }

    fun doLogin(user: String, pass: String){
        setValueOnMainThreadToError(MainActivityState.Loading)
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
                    setValueOnMainThreadToError(MainActivityState.Error(e.message.toString()))
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200){
                        setValueOnMainThreadToError(MainActivityState.Success(response.body?.string()))
                    }else{
                        setValueOnMainThreadToError(MainActivityState.Error("Usuario no autenticado"))
                    }
                }
            }
        )

    }

    fun setValueOnMainThreadToError(value: MainActivityState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }
}