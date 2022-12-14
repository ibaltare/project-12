package com.keepcoding.navi.dragonball.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keepcoding.navi.dragonball.databinding.ActivityHomeBinding
import com.keepcoding.navi.dragonball.utils.EventCallback
import com.keepcoding.navi.dragonball.utils.RandomBattle
import com.keepcoding.navi.dragonball.viewModels.HomeViewModel

class HomeActivity : AppCompatActivity(),EventCallback {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

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
        viewModel.heroList?.let {
            val enemyPosition =  RandomBattle.getRandomHero(it,position)
            if (enemyPosition >= 0){
                supportFragmentManager.beginTransaction()
                    .replace(binding.fragmentContainerView.id, BattleFragment(position, enemyPosition))
                    .setReorderingAllowed(true)
                    .addToBackStack("_back")
                    .commit()
            }
        }
    }

    override fun onClickInfo(position: Int) {
        viewModel.heroList?.let {
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle(it[position].name)
                .setMessage("Seleccionado: ${it[position].selected}")
                .setPositiveButton("Aceptar",null)
                .show()
        }
    }
}