package com.puntogris.posture.data.repository

import com.puntogris.posture.data.datasource.remote.FirebaseDataSource
import com.puntogris.posture.domain.model.Ticket
import com.puntogris.posture.domain.repository.TicketRepository
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.TICKET_COLLECTION
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TicketRepositoryImpl(
    private val firebase: FirebaseDataSource,
    private val dispatchers: DispatcherProvider
) : TicketRepository {

    override suspend fun fillTicketWithUserDataAndSend(ticket: Ticket): SimpleResult {
        val firebaseUser = firebase.getCurrentUser()
        ticket.apply {
            username = firebaseUser?.displayName.toString()
            email = firebaseUser?.email.toString()
            uid = firebaseUser?.uid.toString()
        }
        return sendTicketToServer(ticket)
    }

    private suspend fun sendTicketToServer(ticket: Ticket): SimpleResult =
        withContext(dispatchers.io) {
            SimpleResult.build {
                firebase.firestore.collection(TICKET_COLLECTION).add(ticket).await()
            }
        }
}