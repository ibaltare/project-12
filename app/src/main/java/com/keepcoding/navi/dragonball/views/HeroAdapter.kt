package com.keepcoding.navi.dragonball.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keepcoding.navi.dragonball.databinding.HeroItemBinding
import com.keepcoding.navi.dragonball.models.Hero
import com.keepcoding.navi.dragonball.utils.EventCallback
import com.squareup.picasso.Picasso

class HeroAdapter(private var listener: EventCallback): RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    private var items = listOf<Hero>()

    inner class HeroViewHolder(val binding: HeroItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hero: Hero, position: Int){
            binding.tvName.text = hero.name
            binding.lifeBar.progress = hero.actualLife
            binding.lifeNumber.text = hero.actualLife.toString()
            Picasso.get().load(hero.photo).into(binding.imgHero)
            binding.btnInfo.setOnClickListener { listener.onClickInfo(position)}
            binding.root.setOnClickListener { listener.onClickStartBattle(position) }
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
        holder.bind(items[position],position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(heroList: List<Hero>?) {
        if (heroList != null) {
            items = heroList
            notifyDataSetChanged()
        }
    }
}