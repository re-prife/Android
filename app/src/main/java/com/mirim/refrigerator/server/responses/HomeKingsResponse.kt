package com.mirim.refrigerator.server.responses

import com.mirim.refrigerator.model.ChoreKing
import com.mirim.refrigerator.model.QuestKing

data class HomeKingsResponse (
    val choreKing: List<ChoreKing>,
    val questKing: QuestKing
)