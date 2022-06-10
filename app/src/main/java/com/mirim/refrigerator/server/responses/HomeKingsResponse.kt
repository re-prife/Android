package com.mirim.refrigerator.server.responses

import com.mirim.refrigerator.model.ChoreKing
import com.mirim.refrigerator.model.QuestKing

data class HomeKingsResponse (
    val choreKingResponse: List<ChoreKing>,
    val questKingResponse: QuestKing?
)