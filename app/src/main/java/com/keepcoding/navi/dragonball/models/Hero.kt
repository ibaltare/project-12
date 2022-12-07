package com.keepcoding.navi.dragonball.models

data class Hero(
    val id: String,
    val photo: String,
    val name: String,
    val maximumLife: Int,
    val actualLife: Int,
    val selected: Int
    )
