package com.example.lab3.database.DAO

import androidx.room.*
import com.example.lab3.database.entity.CourtInfo
import com.example.lab3.database.entity.CourtReview

@Dao
interface CourtReviewDAO {
    @Query("SELECT c.courtId,c.name as courtname,avg(cr.rating) as avg_rating\n" +
            "FROM courtReview as cr,court as c\n" +
            "WHERE cr.courtId = c.courtId\n" +
            "GROUP BY cr.courtId\n" +
            "ORDER BY avg_rating DESC")
    fun getAllCourtReviews(): List<CourtInfo>
//    @Query("SELECT * FROM courtReview")
//    fun getAllCourtReviews(): List<CourtReview>

    @Query("SELECT * FROM courtReview WHERE crId=:id")
    fun getCourtReviewById(id:Int): CourtReview?

//    @Query("SELECT * FROM courtReview WHERE userId=:id")
//    fun getCourtReviewByUserId(id:Int): CourtReview?

    @Query("SELECT * FROM courtReview WHERE courtId=:id")
    fun getCourtReviewByCourtId(id:Int): List<CourtReview>?

    @Query("SELECT * FROM courtReview WHERE courtId=:id AND userId = :uid")
    fun getCourtReviewByCourtIdUserId(id:Int, uid:Int): CourtReview?

    @Update
    fun update(courtReview : CourtReview)

    @Delete
    fun deleteCourtReview(courtReview: CourtReview)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCourtReview(courtReview: CourtReview)
}