package com.keepcoding.navi.dragonball.models

data class Hero(
    val id: String,
    val photo: String,
    val name: String,
    val maximumLife: Int,
    var actualLife: Int,
    var selected: Int
    )
