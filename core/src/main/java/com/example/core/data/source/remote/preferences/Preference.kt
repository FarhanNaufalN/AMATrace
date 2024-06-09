package com.example.core.data.source.remote.preferences

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.data.source.remote.response.Account
import com.example.core.data.source.remote.response.ClaimData
import com.example.core.data.source.remote.response.ProductDetailData
import com.example.core.data.source.remote.response.ShippingDetail
import com.example.core.data.source.remote.response.SupplierShippingDetailData
import com.google.gson.Gson
import kotlinx.coroutines.flow.first


private val Context.prefDataStore by preferencesDataStore(name = "settings")
class Preference(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "MyAppPrefs"
        private const val KEY_ACCESS_TOKEN = "accessToken"
        private const val KEY_LOGIN_STATUS = "loginStatus"
        private const val KEY_ACCOUNT_INFO = "accountInfo"
        private const val KEY_DETAIL_PRODUCT = "detailProduct"
        private const val KEY_DETAIL_SHIPPING = "detailShipping"
        private const val KEY_DETAIL_SHIPPING_SCAN = "detailShippingScan"
        private const val KEY_DETAIL_CLAIM_PRODUCT = "detailClaimProduct"

        private const val KEY_NIGHT_MODE = "nightMode"
    }

    fun setAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, " ")
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
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).remove(KEY_LOGIN_STATUS).remove(
            KEY_ACCOUNT_INFO
        ).apply()
    }

    fun saveProductDetail(detailProduct: ProductDetailData) {
        val productJson = Gson().toJson(detailProduct)
        sharedPreferences.edit().putString(KEY_DETAIL_PRODUCT, productJson).apply()
    }

    fun saveClaimProductDetail(detailProduct: ClaimData) {
        val productJson = Gson().toJson(detailProduct)
        sharedPreferences.edit().putString(KEY_DETAIL_CLAIM_PRODUCT, productJson).apply()
    }

    fun getClaimProductDetail(): ClaimData? {
        val productJson = sharedPreferences.getString(KEY_DETAIL_CLAIM_PRODUCT, null)
        return Gson().fromJson(productJson, ClaimData::class.java)
    }

    fun saveShippingScanDetail(detailProduct: SupplierShippingDetailData) {
        val productJson = Gson().toJson(detailProduct)
        sharedPreferences.edit().putString(KEY_DETAIL_SHIPPING_SCAN, productJson).apply()
    }

    fun getProductDetail(): ProductDetailData? {
        val productJson = sharedPreferences.getString(KEY_DETAIL_PRODUCT, null)
        return Gson().fromJson(productJson, ProductDetailData::class.java)
    }

    fun saveShippingDetail(detailShipping: ShippingDetail) {
        val shippingJson = Gson().toJson(detailShipping)
        sharedPreferences.edit().putString(KEY_DETAIL_SHIPPING, shippingJson).apply()
    }

    fun getShippingDetail(): ShippingDetail? {
        val shippingJson = sharedPreferences.getString(KEY_DETAIL_SHIPPING, null)
        return Gson().fromJson(shippingJson, ShippingDetail::class.java)
    }

    fun getShippingScanDetail(): SupplierShippingDetailData? {
        val shippingJson = sharedPreferences.getString(KEY_DETAIL_SHIPPING_SCAN, null)
        return Gson().fromJson(shippingJson, SupplierShippingDetailData::class.java)
    }
}

