package com.mirim.refrigerator.model

class Housework(
    var userId: Int?,
    var choreDate: String?,
    var createdDate: String?,
    var modifiedDate: String?,
    var choreCategory: String?,
    var choreTitle: String?,
    var choreId: Int?,
    var choreCheck: String?
) {
    companion object {
        fun categoryKoreanConverter(english: String?): String {
            return when(english) {
                "DISH_WASHING" -> "설거지"
                "SHOPPING" -> "장보기"
                "COOK" -> "요리"
                else -> "집안일"
            }
        }
        fun categoryEnglishConverter(korean: String?) : String {
            return when(korean) {
                "설거지" -> "DISH_WASHING"
                "장보기" -> "SHOPPING"
                "요리" -> "COOK"
                else -> "DISH_WASHING"
            }
        }
    }
}