package com.keepcoding.navi.dragonball.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keepcoding.navi.dragonball.databinding.FragmentHeroesBinding
import com.keepcoding.navi.dragonball.utils.MainActivityState
import com.keepcoding.navi.dragonball.viewModels.HomeViewModel


class HeroesFragment : Fragment() {

    private lateinit var binding: FragmentHeroesBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val adapter = HeroAdapter()

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
        //setListeners()
        setObservers()

        viewModel.downloadHeroes(view.context)
    }

    private fun setObservers() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MainActivityState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MainActivityState.Error -> {
                    showMessage(it.message)
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is MainActivityState.Success -> {
                    createRecycler()
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
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
}