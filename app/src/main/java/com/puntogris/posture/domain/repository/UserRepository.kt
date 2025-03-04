package com.puntogris.posture.domain.repository

import androidx.lifecycle.LiveData
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.utils.SimpleResult

interface UserRepository {

    fun isUserLoggedIn(): Boolean

    fun getUserLiveData(): LiveData<UserPrivateData>

    suspend fun getUser(): UserPrivateData?

    suspend fun updateUsername(name: String): SimpleResult

    suspend fun updateActiveReminder(reminder: Reminder)

    suspend fun deleteUserAccount(): SimpleResult
}