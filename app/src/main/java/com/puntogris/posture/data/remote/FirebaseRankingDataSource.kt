package com.puntogris.posture.data.remote

import com.google.firebase.firestore.Query
import com.puntogris.posture.utils.Constants.EXPERIENCE_FIELD
import com.puntogris.posture.utils.Constants.PUBLIC_PROFILE_COL_GROUP
import javax.inject.Inject

class FirebaseRankingDataSource @Inject constructor(): FirebaseDataSource() {

    fun getRankingsQueryWithLimit(limit: Long = 30) =
        firestore.collectionGroup(PUBLIC_PROFILE_COL_GROUP)
            .orderBy(EXPERIENCE_FIELD, Query.Direction.DESCENDING)
            .limit(limit)
}