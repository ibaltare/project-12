package com.keepcoding.navi.dragonball.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.keepcoding.navi.dragonball.R
import com.keepcoding.navi.dragonball.databinding.FragmentBattleBinding
import com.keepcoding.navi.dragonball.viewModels.HomeViewModel

class BattleFragment() : Fragment() {

    private lateinit var binding: FragmentBattleBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBattleBinding.inflate(inflater)
        return binding.root
    }

}