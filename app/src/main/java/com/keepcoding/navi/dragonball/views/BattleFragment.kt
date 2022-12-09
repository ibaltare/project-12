package com.keepcoding.navi.dragonball.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.keepcoding.navi.dragonball.R
import com.keepcoding.navi.dragonball.databinding.FragmentBattleBinding
import com.keepcoding.navi.dragonball.utils.RandomBattle
import com.keepcoding.navi.dragonball.utils.SharedPreferences
import com.keepcoding.navi.dragonball.viewModels.HomeViewModel
import com.squareup.picasso.Picasso
import java.text.FieldPosition

class BattleFragment(private val heroPosition: Int, private val enemyPosition: Int) : Fragment() {

    private lateinit var binding: FragmentBattleBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBattleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createCharacterHero()
        createCharacterEnemy()
        setListeners()
    }

    private fun createCharacterHero(){
        viewModel.heroList?.let {
            with(it[heroPosition]){
                selected+=1
                binding.apply {
                    Picasso.get().load(photo).into(imgHeroSelect)
                    tvNameSelect.text = name
                    tvLifeSelect.text = actualLife.toString()
                    pbSelect.progress = actualLife
                }
            }
        }
    }

    private fun createCharacterEnemy(){
        viewModel.heroList?.let {
            with(it[enemyPosition]){
                binding.apply {
                    Picasso.get().load(photo).into(imgHeroEnemy)
                    tvNameEnemy.text = name
                    tvLifeEnemy.text = actualLife.toString()
                    pbEnemy.progress = actualLife
                }
            }
        }
    }

    private fun setListeners(){
        binding.btnFight.setOnClickListener {
            fight()
        }
    }

    private fun fight(){
        val hurt = RandomBattle.getHurt()
        val character = RandomBattle.getCharacter()
        viewModel.heroList?.let {
            if (character == 1){
                if (it[enemyPosition].actualLife - hurt > 0){
                    it[enemyPosition].actualLife -= hurt
                    binding.tvLifeEnemy.text = it[enemyPosition].actualLife.toString()
                    binding.pbEnemy.progress = it[enemyPosition].actualLife
                } else {
                    it[enemyPosition].actualLife = 0
                    parentFragmentManager.popBackStack()
                }
            } else {
                if (it[heroPosition].actualLife - hurt > 0){
                    it[heroPosition].actualLife -= hurt
                    binding.tvLifeSelect.text = it[heroPosition].actualLife.toString()
                    binding.pbSelect.progress = it[heroPosition].actualLife
                } else {
                    it[heroPosition].actualLife = 0
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun saveBattle(){
        val json = Gson().toJson(viewModel.heroList)
        SharedPreferences.saveHeroes(json,binding.root.context)
    }

    override fun onStop() {
        super.onStop()
        Log.d("onStop", viewModel.heroList?.get(heroPosition)?.actualLife.toString())
        Log.d("onStop", viewModel.heroList?.get(enemyPosition)?.actualLife.toString())
        Log.d("onStop POS", "H: $heroPosition E: $enemyPosition")
        saveBattle()
    }

}