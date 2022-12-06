package com.keepcoding.navi.dragonball.models

import android.content.Context

object SharedPreferences {

    fun saveToken(token: String, context: Context) {
        val editPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE).edit()
        editPreferences.putString(Constants.SHARED_PREFERENCES_TOKEN,token)
        editPreferences.apply()
    }

    fun getToken(context: Context): String? {
        return context
            .getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
            .getString(Constants.SHARED_PREFERENCES_TOKEN,"")
    }
}