package com.recipe.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {

    @TypeConverter
    fun fromAnyToString(attribute:Any): String {
        return if (attribute == null) {
            ""
        } else attribute as String
    }
    @TypeConverter
    fun fromStringToAny(attribute: String?) : Any {
        return attribute ?: ""
    }
}