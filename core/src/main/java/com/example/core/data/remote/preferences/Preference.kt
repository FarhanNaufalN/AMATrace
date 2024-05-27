package com.example.core.data.remote.preferences

import android.content.Context
import com.example.core.data.remote.response.Account
import com.google.gson.Gson

class Preference(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "MyAppPrefs"
        private const val KEY_ACCESS_TOKEN = "accessToken"
        private const val KEY_LOGIN_STATUS = "loginStatus"
        private const val KEY_ACCOUNT_INFO = "accountInfo"
    }

    fun setAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun setStatusLogin(status: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_LOGIN_STATUS, status).apply()
    }

    fun getStatusLogin(): Boolean {
        return sharedPreferences.getBoolean(KEY_LOGIN_STATUS, false)
    }

    fun saveAccountInfo(account: Account) {
        val accountJson = Gson().toJson(account)
        sharedPreferences.edit().putString(KEY_ACCOUNT_INFO, accountJson).apply()
    }

    fun getAccountInfo(): Account? {
        val accountJson = sharedPreferences.getString(KEY_ACCOUNT_INFO, null)
        return Gson().fromJson(accountJson, Account::class.java)
    }

    fun clearUserToken() {
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).apply()
    }

    fun clearUserLogin() {
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).remove(KEY_LOGIN_STATUS).remove(KEY_ACCOUNT_INFO).apply()
    }

}

