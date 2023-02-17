package com.mhmdawad.radu.utils

import kotlinx.coroutines.flow.Flow

interface AudioHelper {

    suspend fun changeVolume(volume: Int)
    suspend fun getCurrentVolume(): Flow<Int>
    fun getMaxVolume(): Int
    fun onDestroy()

}