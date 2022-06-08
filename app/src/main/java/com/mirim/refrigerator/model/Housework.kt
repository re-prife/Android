package com.mirim.refrigerator.model

class Housework(
    var userId: Int?,
    var choreDate: IntArray?,
//    var choreRegisterDate: String?,
//    var choreModifyDate: String?,
    var choreCategory: String?,
    var choreTitle: String?,
    var choreId: Int?,
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
    }
}