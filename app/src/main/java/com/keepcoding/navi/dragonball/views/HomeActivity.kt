package com.keepcoding.navi.dragonball.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.keepcoding.navi.dragonball.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, HeroesFragment())
                .commitNow()
        }
    }

    companion object {
        fun launch(context: Context){
            val intent = Intent(context,HomeActivity::class.java)
            context.startActivity(intent)
        }
    }
}