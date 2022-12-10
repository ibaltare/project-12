package com.keepcoding.navi.dragonball.views

import android.annotation.SuppressLint
import android.graphics.Color
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
            with(binding){
                tvName.text = hero.name
                lifeBar.progress = hero.actualLife
                lifeNumber.text = hero.actualLife.toString()
                Picasso.get().load(hero.photo).into(imgHero)
                btnInfo.setOnClickListener { listener.onClickInfo(position)}
            }
        }

        fun setHeroEnabled(hero:Hero, position: Int){
            binding.apply {
                if (hero.actualLife > 0) {
                    root.setOnClickListener {
                        listener.onClickStartBattle(position)
                    }
                }else{
                    imgHero.setColorFilter(Color.argb(210, 128, 128, 128))
                    tvName.setTextColor(Color.GRAY)
                }
            }
        }

    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

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
        holder.setHeroEnabled(items[position],position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(heroList: List<Hero>?) {
        if (heroList != null) {
            items = heroList
            notifyDataSetChanged()
        }
    }
}