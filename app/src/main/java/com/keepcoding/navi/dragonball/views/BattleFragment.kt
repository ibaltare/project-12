package com.keepcoding.navi.dragonball.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keepcoding.navi.dragonball.databinding.FragmentBattleBinding
import com.keepcoding.navi.dragonball.viewModels.HomeViewModel
import com.squareup.picasso.Picasso

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
            compareLife()
        }
    }

    private fun fight(){
        viewModel.startFight(enemyPosition,heroPosition)
        viewModel.heroList?.let {
            binding.tvLifeEnemy.text = it[enemyPosition].actualLife.toString()
            binding.pbEnemy.progress = it[enemyPosition].actualLife
            binding.tvLifeSelect.text = it[heroPosition].actualLife.toString()
            binding.pbSelect.progress = it[heroPosition].actualLife
        }
    }

    private fun compareLife(){
        viewModel.heroList?.let {
            if (it[heroPosition].actualLife == 0){
                showWinner(it[enemyPosition].name)
            }else if (it[enemyPosition].actualLife == 0) {
                showWinner(it[heroPosition].name)
            }
        }
    }

    private fun showWinner(winner: String){
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Ganador: $winner")
            .setPositiveButton("Aceptar") { _, _ ->
                parentFragmentManager.popBackStack()
            }
            .show()
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveHeroes(binding.root.context)
    }

}