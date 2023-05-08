package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.Reservation
import java.sql.Time
import java.time.LocalDate

class SearchFragment() : BaseFragment(R.layout.fragment_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = view.findNavController()
        val b = view?.findViewById<Button>(R.id.button);
        val courts = listOf<Court>(
            Court("Court_1","qaz","basketball"))
        val ct = listOf<CourtTime>(
            CourtTime(1, Time(9,0,0), Time(10,0,0)),
            CourtTime(1, Time(10,0,0), Time(11,0,0)),
            CourtTime(1, Time(11,0,0), Time(12,0,0)),
            CourtTime(1, Time(12,0,0), Time(13,0,0)),
            CourtTime(1, Time(13,0,0), Time(14,0,0)),
            CourtTime(1, Time(14,0,0), Time(15,0,0)),
            CourtTime(1, Time(15,0,0), Time(16,0,0)),
            CourtTime(1, Time(16,0,0), Time(17,0,0)),
            CourtTime(1, Time(17,0,0), Time(18,0,0)),
            CourtTime(1, Time(18,0,0), Time(19,0,0)))
        val reservations = listOf<Reservation>(
            Reservation(1, 1, 0, LocalDate.of(2023,5,1),"aaa"),
            Reservation(2, 1, 0, LocalDate.of(2023,5,4),"aaa"),
            Reservation(3, 1, 0, LocalDate.of(2023,5,1),"aaa"))

        b?.setOnClickListener{
            //var bundle = bundleOf("courts" to courts,"ct" to ct,"reservations" to reservations)
         navController.navigate(R.id.action_searchFragment_to_addReservationFragment)
        }
    }


}