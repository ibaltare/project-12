package com.keepcoding.navi.dragonball.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keepcoding.navi.dragonball.databinding.FragmentHeroesBinding
import com.keepcoding.navi.dragonball.utils.EventCallback
import com.keepcoding.navi.dragonball.utils.MainActivityState
import com.keepcoding.navi.dragonball.viewModels.HomeViewModel


class HeroesFragment(private var listener: EventCallback) : Fragment(), EventCallback{

    private lateinit var binding: FragmentHeroesBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val adapter = HeroAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHeroesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
        viewModel.loadLocalHeroes(binding.root.context)
        if (viewModel.heroList == null){
            viewModel.downloadHeroes(binding.root.context)
        }
        checkWinner()
    }

    private fun checkWinner(){
        if (viewModel.endGame()){
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle("Juego Terminado")
                .setMessage("Ganador: ${viewModel.getWinner()?.name}")
                .setPositiveButton("Aceptar",null)
                .show()
        }
    }

    private fun setObservers() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MainActivityState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MainActivityState.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    showMessage(it.message)
                }
                is MainActivityState.Success -> {
                    createRecycler()
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setListeners(){
        binding.fabReset.setOnClickListener {
            MaterialAlertDialogBuilder(binding.root.context)
                .setTitle("Nuevo Juego")
                .setMessage("¿Desea reiniciar el juego?")
                .setPositiveButton("Aceptar") { _, _ ->
                    viewModel.resetGame()
                    viewModel.saveHeroes(binding.root.context)
                    createRecycler()
                }
                .setNegativeButton("Cancelar",null)
                .show()
        }
    }
    private fun createRecycler() {
        adapter.updateList(viewModel.heroList)
        binding.rvItems.adapter = adapter
        binding.rvItems.layoutManager = LinearLayoutManager(binding.root.context)
    }

    private fun showMessage(message: String){
        Toast.makeText(binding.root.context, message, Toast.LENGTH_LONG).show()
    }

    override fun onClickStartBattle(position: Int) {
        listener.onClickStartBattle(position)
    }

    override fun onClickInfo(position: Int) {
        listener.onClickInfo(position)
    }

}