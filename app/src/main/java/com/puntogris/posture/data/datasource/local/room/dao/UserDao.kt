package com.puntogris.posture.data.datasource.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.puntogris.posture.model.UserPrivateData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userPrivateData: UserPrivateData)

    @Query("SELECT * FROM UserPrivateData WHERE roomId = 1")
    suspend fun getUser(): UserPrivateData

    @Query("SELECT * FROM UserPrivateData WHERE roomId = 1")
    fun getUserLiveData(): LiveData<UserPrivateData>


    @Query("UPDATE UserPrivateData SET currentReminderId = :reminderId WHERE roomId = 1")
    suspend fun updateCurrentUserReminder(reminderId: String)

    @Query("UPDATE UserPrivateData SET username = :name WHERE roomId = 1")
    suspend fun updateUsername(name: String)

    @Query("UPDATE UserPrivateData SET experience = experience + :experience WHERE roomId = 1")
    suspend fun updateUserExperience(experience: Int)
}