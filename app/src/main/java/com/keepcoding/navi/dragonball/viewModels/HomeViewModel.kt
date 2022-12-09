package com.keepcoding.navi.dragonball.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.keepcoding.navi.dragonball.models.*
import com.keepcoding.navi.dragonball.utils.Constants
import com.keepcoding.navi.dragonball.utils.MainActivityState
import com.keepcoding.navi.dragonball.utils.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class HomeViewModel: ViewModel() {

    var heroList: List<Hero>? = null
    val stateLiveData : MutableLiveData<MainActivityState> by lazy {
        MutableLiveData<MainActivityState>()
    }

    fun loadLocalHeroes(context: Context){
        setValueOnMainThread(MainActivityState.Loading)
        var json = SharedPreferences.getHeroes(context)
        json?.let {
            var heroArray =  Gson().fromJson(it, Array<Hero>::class.java)
            heroList = heroArray.toList()
            Log.d("loadLocalHeroes","loadLocalHeroes *******")
        }
        setValueOnMainThread(MainActivityState.Success(null))
    }

    fun downloadHeroes(context: Context) {
        setValueOnMainThread(MainActivityState.Loading)
        Log.d("downloadHeroes","downloadHeroes *******")
        val client = OkHttpClient()
        val url = Constants.URL_HEROES_ALL
        val token = SharedPreferences.getToken(context)
        val formBody = FormBody.Builder().add("name","").build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
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

                        val heroDtoArray: Array<HeroDto> =
                            Gson().fromJson(response.body?.string(), Array<HeroDto>::class.java)

                        heroList = heroDtoArray.map {
                            Hero(it.id, it.photo,it.name,100,100, 0)
                        }

                        val json = Gson().toJson(heroList)
                        SharedPreferences.saveHeroes(json,context)
                        setValueOnMainThread(MainActivityState.Success(null))
                    }else{
                        setValueOnMainThread(MainActivityState.Error("Error al descargar los datos"))
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