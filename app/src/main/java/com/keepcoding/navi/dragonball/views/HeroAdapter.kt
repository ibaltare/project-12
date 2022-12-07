package com.keepcoding.navi.dragonball.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.keepcoding.navi.dragonball.databinding.HeroItemBinding
import com.keepcoding.navi.dragonball.models.Hero
import com.squareup.picasso.Picasso

class HeroAdapter : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    private var items = listOf<Hero>()

    inner class HeroViewHolder(val binding: HeroItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hero: Hero){
            binding.tvName.text = hero.name
            binding.lifeBar.progress = hero.actualLife
            binding.lifeNumber.text = hero.actualLife.toString()
            Picasso.get().load(hero.photo).into(binding.imgHero)
            binding.btnInfo.setOnClickListener { Toast.makeText(binding.root.context, hero.name, Toast.LENGTH_LONG).show() }
            binding.root.setOnClickListener { Toast.makeText(binding.root.context, hero.id, Toast.LENGTH_LONG).show() }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        return HeroViewHolder(
            HeroItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(heroList: List<Hero>?) {
        if (heroList != null) {
            items = heroList
            notifyDataSetChanged()
        }
    }
}