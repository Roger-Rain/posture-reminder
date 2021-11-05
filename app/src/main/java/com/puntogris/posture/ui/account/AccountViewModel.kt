package com.puntogris.posture.ui.account

import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    userRepository: UserRepository,
    private val dayLogsRepository: DayLogsRepository
) : ViewModel() {

    val user = userRepository.getLocalUserLiveData()

    suspend fun getWeekData() = dayLogsRepository.getWeekDayLogs()
}