package com.example.lab3

import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.DAO.CourtDao
import com.example.lab3.database.DAO.CourtTimeDAO
import com.example.lab3.database.DAO.ReservationDAO
import com.example.lab3.database.DAO.UserDao
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.Reservation
import com.example.lab3.database.entity.User
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DBtest {
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var reservationDAO: ReservationDAO
    private lateinit var courtDao: CourtDao
    private lateinit var courtTimeDAO: CourtTimeDAO

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
        courtDao = db.courtDao()
        courtTimeDAO = db.courtTimeDao()
        reservationDAO = db.reservationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun addingNewUser() {
        val u = User("abc","cde","123")
        userDao.addUser(u)
        val u1 = userDao.getUserById(1)
        println(">>>>> ${u1}")
        Assert.assertEquals(u1 , u)
    }

    @Test
    @Throws(Exception::class)
    fun addingMultipleNewUser() {
        val u1 = User("abc1","cde1","123");
        val u2 = User("abc2","cde2","456");
        val u3 = User("abc3","cde3","789");
        val expectedList = listOf(u1,u2,u3)
        userDao.addUser(u1);
        userDao.addUser(u2);
        userDao.addUser(u3);
        val userList = userDao.getAllUsers()
        println(">>>>> ${userList}")
        Assert.assertEquals(userList , expectedList)
    }

    @Test
    @Throws(Exception::class)
    fun addingNewCourt() {
        val c = Court("Court1","Address1","tennis");
        courtDao.addCourt(c);
        val c1 = courtDao.getCourtById(1)
        println(">>>>> ${c1}")
        Assert.assertEquals(c1 , c)
    }

    @Test
    @Throws(Exception::class)
    fun addingMultipleNewCourts() {
        val c1 = Court("Court1","Address1","tennis");
        val c2 = Court("Court2","Address2","football");
        val c3 = Court("Court3","Address3","tennis");
        val expectedList = listOf<Court>(c1,c2,c3)
        courtDao.addCourt(c1);
        courtDao.addCourt(c2);
        courtDao.addCourt(c3);
        val courtList = courtDao.getAllCourtsTest()
        println(">>>>> ${courtList}")
        Assert.assertEquals(courtList , expectedList)
    }

    @Test
    @Throws(Exception::class)
    fun deletingCourt() {
        val c = Court("Court1","Address1","tennis");
        courtDao.addCourt(c);
        val courtToBeDeleted = courtDao.getCourtById(1)
        courtDao.deleteCourt(courtToBeDeleted!!)
        val result = courtDao.getCourtById(1)
        println(">>>>> ${result}")
        Assert.assertEquals(result , null)
    }

    @Test
    @Throws(Exception::class)
    fun deletingMultipleCourts() {
        val c1 = Court("Court1","Address1","tennis");
        val c2 = Court("Court2","Address2","football");
        val c3 = Court("Court3","Address3","tennis");
        courtDao.addCourt(c1);
        courtDao.addCourt(c2);
        courtDao.addCourt(c3);
        val courtsToBeDeleted = courtDao.getAllCourtsTest()
        for(c in courtsToBeDeleted){
            courtDao.deleteCourt(c)
        }
        val result = courtDao.getAllCourtsTest()
        println(">>>>> ${result}")
        Assert.assertEquals(result , emptyList<Court>())
    }

    @Test
    @Throws(Exception::class)
    fun updatingCourt() {
        val c = Court("Court1","Address1","tennis");
        courtDao.addCourt(c);
        val courtToBeUpdated = courtDao.getCourtById(1)
        courtToBeUpdated?.name = "UpdatedCourt1"
        courtToBeUpdated?.address = "UpdatedAddress1"
        courtToBeUpdated?.sport = "football"
        courtDao.update(courtToBeUpdated!!)
        val updatedCourt = courtDao.getCourtById(1)
        println(">>>>> ${updatedCourt}")
        Assert.assertEquals(updatedCourt?.name , "UpdatedCourt1")
        Assert.assertEquals(updatedCourt?.address , "UpdatedAddress1")
        Assert.assertEquals(updatedCourt?.sport , "football")
    }

    @Test
    @Throws(Exception::class)
    fun addingNewCourtTime() {
        val ct = CourtTime(1,"15:00","17:30");
        courtTimeDAO.addCourtTime(ct);
        val ct1 = courtTimeDAO.getAllTCourtTimesByCourtIdTest(1);
        println(">>>>> ${ct1}")
        Assert.assertEquals(ct1[0] , ct)
    }

    @Test
    @Throws(Exception::class)
    fun addingMultipleNewCourtTimes() {
        val ct1 = CourtTime( courtId = 1, startTime = "15:00", endTime = "17:30");
        val ct2 = CourtTime( courtId = 1,startTime = "17:30",endTime ="18:30");
        val ct3 = CourtTime( courtId = 1,startTime = "19:00",endTime = "21:30");
        val expectedList = listOf<CourtTime>(ct1,ct2,ct3);
        courtTimeDAO.addCourtTime(ct1);
        courtTimeDAO.addCourtTime(ct2);
        courtTimeDAO.addCourtTime(ct3);
        val courtTimeList = courtTimeDAO.getAllTCourtTimesByCourtIdTest(1);
        println(">>>>> ${courtTimeList}")
        Assert.assertEquals(courtTimeList , expectedList)
    }

    @Test
    @Throws(Exception::class)
    fun deletingSingleCourtTime() {
        val ct1 = CourtTime( courtId = 1, startTime = "15:00", endTime = "17:30");
        courtTimeDAO.addCourtTime(ct1);
        val ctToBeDeleted = courtTimeDAO.getAllTCourtTimesByCourtIdTest(1);
        courtTimeDAO.deleteCourtTime(ctToBeDeleted[0])
        val result = courtTimeDAO.getAllTCourtTimesByCourtIdTest(1);
        println(">>>>> ${ctToBeDeleted}")
        Assert.assertEquals(result , emptyList<CourtTime>())
    }

    @Test
    @Throws(Exception::class)
    fun deletingMultipleCourtTimes() {
        val ct1 = CourtTime( courtId = 1, startTime = "15:00", endTime = "17:30");
        val ct2 = CourtTime( courtId = 1,startTime = "17:30",endTime ="18:30");
        val ct3 = CourtTime( courtId = 1,startTime = "19:00",endTime = "21:30");
        courtTimeDAO.addCourtTime(ct1);
        courtTimeDAO.addCourtTime(ct2);
        courtTimeDAO.addCourtTime(ct3);
        var courtTimeList = courtTimeDAO.getAllTCourtTimesByCourtIdTest(1);
        for(c in courtTimeList){
            courtTimeDAO.deleteCourtTime(c);
        }
        courtTimeList = courtTimeDAO.getAllTCourtTimesByCourtIdTest(1);
        println(">>>>> ${courtTimeList}")
        Assert.assertEquals(courtTimeList , emptyList<CourtTime>())
    }

    @Test
    @Throws(Exception::class)
    fun updatingCourtTime() {
        val ct1 = CourtTime( courtId = 1, startTime = "15:00", endTime = "17:30");
        courtTimeDAO.addCourtTime(ct1);
        val ctToBeUpdated = courtTimeDAO.getAllTCourtTimesByCourtIdTest(1);
        ctToBeUpdated[0].startTime = "19:00"
        ctToBeUpdated[0].endTime = "21:00"
        courtTimeDAO.update(ctToBeUpdated[0]);
        val updatedCt = courtTimeDAO.getAllTCourtTimesByCourtIdTest(1);
        println(">>>>> ${updatedCt}")
        Assert.assertEquals(updatedCt[0].startTime , "19:00")
        Assert.assertEquals(updatedCt[0].endTime , "21:00")
    }

    @Test
    @Throws(Exception::class)
    fun addingNewReservation() {
        val r = Reservation(1,1,0,"04-07-2023","Description")
        reservationDAO.addReservation(r)
        val r1 = reservationDAO.getReservationById(1)
        println(">>>>> ${r1}")
        Assert.assertEquals(r1 , r)
    }

    @Test
    @Throws(Exception::class)
    fun addingMultipleNewReservation() {
        val r1 = Reservation(1,1,0,"04-07-2023","Description1")
        val r2 = Reservation(1,1,0,"04-07-2023","Description2")
        val r3 = Reservation(1,1,0,"04-07-2023","Description3")
        val expectedList = listOf<Reservation>(r1,r2,r3)
        reservationDAO.addReservation(r1)
        reservationDAO.addReservation(r2)
        reservationDAO.addReservation(r3)
        val reservationsList = reservationDAO.getAllReservationsTest()
        println(">>>>> ${reservationsList}")
        Assert.assertEquals(reservationsList, expectedList)
    }

    @Test
    @Throws(Exception::class)
    fun deletingReservation() {
        val r = Reservation(1,1,0,"04-07-2023","Description")
        reservationDAO.addReservation(r)
        val reservationToBeDeleted = reservationDAO.getReservationById(1)
        reservationDAO.deleteReservation(reservationToBeDeleted!!)
        val result = reservationDAO.getReservationById(1)
        println(">>>>> ${result}")
        Assert.assertEquals(result , null)
    }

    @Test
    @Throws(Exception::class)
    fun deletingMultipleReservations() {
        val r1 = Reservation(1,1,0,"04-07-2023","Description1")
        val r2 = Reservation(1,1,0,"04-07-2023","Description2")
        val r3 = Reservation(1,1,0,"04-07-2023","Description3")
        reservationDAO.addReservation(r1)
        reservationDAO.addReservation(r2)
        reservationDAO.addReservation(r3)
        var reservationsList = reservationDAO.getAllReservationsTest()
        for(r in reservationsList){
            reservationDAO.deleteReservation(r)
        }
        reservationsList = reservationDAO.getAllReservationsTest();
        println(">>>>> ${reservationsList}")
        Assert.assertEquals(reservationsList , emptyList<Reservation>())
    }

    @Test
    @Throws(Exception::class)
    fun updatingReservation() {
        val r = Reservation(1,1,0,"04-07-2023","Description")
        reservationDAO.addReservation(r)
        val reservationToBeUpdated = reservationDAO.getReservationById(1)
        reservationToBeUpdated?.courtId = 2
        reservationToBeUpdated?.description = "UpdatedDescription"
        reservationToBeUpdated?.date = "05-07-2023"
        reservationDAO.update(reservationToBeUpdated!!)
        val updatedReservation = reservationDAO.getReservationById(1)
        println(">>>>> ${updatedReservation}")
        Assert.assertEquals(updatedReservation?.courtId ,  2)
        Assert.assertEquals(updatedReservation?.description, "UpdatedDescription")
        Assert.assertEquals(updatedReservation?.date  , "05-07-2023")
    }


}