package com.example.lab3.database

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Time

class Converter {
    @TypeConverter
    fun Long2Date(value:Long?):Date?{
        return value?.let{Date(it)}
    }
    @TypeConverter
    fun Date2Long(date:Date?):Long?{
        return date?.time?.toLong()
    }

    @TypeConverter
    fun Long2Time(value:Long?):Time?{
        return value?.let{Time(it)}
    }
    @TypeConverter
    fun Time2Long(time:Time?):Long?{
        return time?.time?.toLong()
    }
}