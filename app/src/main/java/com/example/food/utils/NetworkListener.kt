package com.example.food.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvaliable = MutableStateFlow(false)

    fun checkNetworkAvaliablility(context: Context): MutableStateFlow<Boolean> {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false

        connectivityManager.allNetworks.forEach { network ->
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            networkCapabilities?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        isNetworkAvaliable.value = isConnected

        return isNetworkAvaliable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvaliable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvaliable.value = false
    }
}
