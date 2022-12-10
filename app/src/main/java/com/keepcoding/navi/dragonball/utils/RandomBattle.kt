package com.keepcoding.navi.dragonball.utils

import com.keepcoding.navi.dragonball.models.Hero
import kotlin.random.Random

object RandomBattle {

    fun getRandomHero(heroList: List<Hero>, position: Int): Int {
        val temp = heroList.filterIndexed { index, s -> (index != position) && (s.actualLife > 0)  }
        if (temp.isEmpty()) return -1
        val hero = temp.random()
        return heroList.indexOfFirst { it.name == hero.name }
    }

    fun getHurt() = Random.nextInt(10, 61)

    fun getCharacter() = Random.nextInt(1, 3)
}