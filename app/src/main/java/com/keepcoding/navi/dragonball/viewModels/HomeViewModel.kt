package com.keepcoding.navi.dragonball.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.keepcoding.navi.dragonball.models.*
import com.keepcoding.navi.dragonball.utils.Constants
import com.keepcoding.navi.dragonball.utils.MainActivityState
import com.keepcoding.navi.dragonball.utils.RandomBattle
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
        val json = SharedPreferences.getHeroes(context)
        json?.let {
            val heroArray =  Gson().fromJson(it, Array<Hero>::class.java)
            heroList = heroArray.toList()
        }
        setValueOnMainThread(MainActivityState.Success(null))
    }

    fun downloadHeroes(context: Context) {
        setValueOnMainThread(MainActivityState.Loading)
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

    fun saveHeroes(context: Context){
        val json = Gson().toJson(heroList)
        SharedPreferences.saveHeroes(json,context)
    }

    fun setValueOnMainThread(value: MainActivityState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }

    fun  startFight(enemyPosition: Int, heroPosition: Int){
        val hurt = RandomBattle.getHurt()
        val character = RandomBattle.getCharacter()
        heroList?.let {
            if (character == 1){
                if (it[enemyPosition].actualLife - hurt > 0){
                    it[enemyPosition].actualLife -= hurt
                } else {
                    it[enemyPosition].actualLife = 0
                }
            } else {
                if (it[heroPosition].actualLife - hurt > 0){
                    it[heroPosition].actualLife -= hurt
                } else {
                    it[heroPosition].actualLife = 0
                }
            }
        }
    }

    fun resetGame(){
        heroList?.let { list ->
            list.forEach {
                it.actualLife = it.maximumLife
                it.selected = 0
            }
        }
    }

    fun endGame(): Boolean{
        val winner = heroList?.count { it.actualLife > 0 }
        return winner == 1
    }

    fun getWinner() = heroList?.first { it.actualLife > 0 }

}