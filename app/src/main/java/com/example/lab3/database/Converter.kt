package com.example.lab3.database

import androidx.room.TypeConverter
import java.sql.Time
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


class Converter {
    @TypeConverter
    fun Long2LocalDate(value:Long?):LocalDate?{
        val zone = ZoneId.systemDefault()
        return value?.let{
            Instant.ofEpochSecond(it).atZone(zone).toLocalDate();
        }
    }
    @TypeConverter
    fun LocalDate2Long(date:LocalDate?):Long?{
        val zoneId: ZoneId = ZoneId.systemDefault()
	    return date?.atStartOfDay(zoneId)?.toEpochSecond()
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