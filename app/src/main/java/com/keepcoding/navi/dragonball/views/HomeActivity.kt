package com.keepcoding.navi.dragonball.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.keepcoding.navi.dragonball.databinding.ActivityHomeBinding
import com.keepcoding.navi.dragonball.utils.EventCallback

class HomeActivity : AppCompatActivity(),EventCallback {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, HeroesFragment(this))
                .commitNow()
        }
    }

    companion object {
        fun launch(context: Context){
            val intent = Intent(context,HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onClickStartBattle(position: Int) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, BattleFragment())
            .setReorderingAllowed(true)
            .addToBackStack("_back")
            .commit()
    }

    override fun onClickInfo(position: Int) {
        Log.d("Info", position.toString())
    }
}