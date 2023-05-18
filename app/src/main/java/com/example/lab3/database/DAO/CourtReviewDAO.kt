package com.example.lab3.database.DAO

import androidx.room.*
import com.example.lab3.database.entity.CourtReview


interface CourtReviewDAO {
    @Query("SELECT * FROM courtReview")
    fun getAllCourtReviews(): List<CourtReview>

    @Query("SELECT * FROM courtReview WHERE crId=:id")
    fun getCourtReviewById(id:Int): CourtReview?

    @Query("SELECT * FROM courtReview WHERE userId=:id")
    fun getCourtReviewByUserId(id:Int): CourtReview?

    @Query("SELECT * FROM courtReview WHERE courtId=:id")
    fun getCourtReviewByCourtId(id:Int): CourtReview?

    @Update
    fun update(courtReview : CourtReview)

    @Delete
    fun deleteCourtReview(courtReview: CourtReview)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCourtReview(courtReview: CourtReview)
}