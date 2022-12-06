package com.keepcoding.navi.dragonball.models

sealed class MainActivityState {
    data class Success(val message: String?) : MainActivityState()
    data class Error(val message: String): MainActivityState()
    object Loading : MainActivityState()
}
