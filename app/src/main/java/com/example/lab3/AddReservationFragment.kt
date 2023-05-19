package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.Reservation
import com.google.android.material.textfield.TextInputLayout
import java.sql.Time
import java.time.LocalDate


class AddReservationFragment : BaseFragment(R.layout.fragment_add_reservation),HasToolbar {
    override val toolbar: Toolbar?
        get() = null
    companion object {
        fun newInstance() = AddReservationFragment()
    }

    private var layoutManager : RecyclerView.LayoutManager ?= null
    private var  adapter : MyAdapter1 ?= null
    //private lateinit var viewModel: AddReservationViewModel
    private val viewModel:  AddReservationViewModel by activityViewModels()
    private val mainVm: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView2)
        val doneButton = view.findViewById<Button>(R.id.button2)
        val description = view.findViewById<TextInputLayout>(R.id.textInputLayout)
        val dateDisplayed = view.findViewById<TextView>(R.id.textView6)
        val sportDisplayed = view.findViewById<TextView>(R.id.textView11)
        selectedSlot = ""

//        viewModel = ViewModelProvider(this).get(AddReservationViewModel::class.java)
        var dateString : String? = arguments?.getString("date")
        dateDisplayed.setText(dateString)
        var sport : String? = arguments?.getString("sport")
        sportDisplayed.setText(sport)
        var dateLD : LocalDate = LocalDate.parse(dateString)

        viewModel.getTimeSlots(this.requireActivity().application,sport!!,dateLD)

        recyclerView.apply {
            recyclerView.layoutManager = LinearLayoutManager(context)
           // recyclerView.adapter = MyAdapter1(ct)
        }

        val ctLiveData = viewModel.timeSlots
        ctLiveData.observe(viewLifecycleOwner){
            recyclerView.apply {
                recyclerView.adapter = MyAdapter1(ctLiveData.value!!)
            }
        }

        doneButton.setOnClickListener {
            var failed = false;
            if(selectedSlot == ""){
                //println("SELECT A SLOT FIRST !")
                Toast.makeText(this.context,"SELECT A SLOT FIRST !",Toast.LENGTH_SHORT).show()
            } else {
                val splittedSelectedSlot =
                    selectedSlot.split(":") // GET THE (ex) "10" in "10:00:00" - the only thing needed for later are the two first digits
                val timeParameters: List<Int> =
                    listOf(splittedSelectedSlot[0].toString().toInt(), 0, 0)
                val startTime = Time(timeParameters[0], timeParameters[1], timeParameters[2])
                val courtId = viewModel.getCourtIdBySport(this.requireActivity().application, sport)
                val courtTimeId = viewModel.getCourtTimeIdByCourtIdAndStartTime(
                    this.requireActivity().application,
                    courtId,
                    startTime
                )
                val resDescription = description.editText?.text.toString()
                val newReservation: Reservation =
                    Reservation(courtTimeId, 1, 0, dateLD, resDescription)
                try {viewModel.addNewReservation(this.requireActivity().application, newReservation) }
                catch (e : Exception){
                    failed = true;
                    Toast.makeText(this.context,"Operation failed , retry later!",Toast.LENGTH_SHORT).show()
                }
                if(failed == false) {
                    Toast.makeText(
                        this.context,
                        "New Reservation successfully added!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                mainVm.setShowNav(true)
                findNavController().navigate(R.id.action_addReservationFragment_to_searchFragment)
            }
            /*
            println("NEW RESERVATION : $newReservation")
            println("DESCRIPTION : ${description.editText?.text.toString()}")
            println("COURT ID : ${courtId} - COURTTIMEID : ${courtTimeId}")
            println("SELECTED SLOT : " + selectedSlot)
            println("SELECTED DATE : " + dateString)
            println("DATELD : " + dateLD.toString())
            println("SELECTED SPORT : " + sport)
            println("TIME START : " + startTime)*/

        }

    }
}

var selectedSlot : String = ""
class MyAdapter1(val l: List<CourtTime>) : RecyclerView.Adapter<MyAdapter1.MyViewHolder1>(){
    var singleItemSelectionPosition = -1

    inner class MyViewHolder1(v : View): RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<TextView>(R.id.item_time_slot)

        init {
            tv.setOnClickListener {
                setSingleSelection(adapterPosition)
                selectedSlot = l[adapterPosition].startTime.toString()
                //println(l[adapterPosition].startTime.toString())
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder1{
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_time_slots,parent,false)
        return MyViewHolder1(v)
    }

    override fun getItemCount(): Int {
        return l.size
    }

    override fun onBindViewHolder(holder: MyViewHolder1, position: Int){
       holder.tv.text = "${l[position].startTime.toString()}" + " - " + "${ l[position].endTime.toString()}"
        if(singleItemSelectionPosition == position){
            holder.tv.setBackgroundColor(holder.tv.resources.getColor(R.color.blue))
        } else {
            holder.tv.setBackgroundColor(0)
        }

    }

    private fun setSingleSelection(adapterPosition: Int){
        if(adapterPosition == RecyclerView.NO_POSITION) return

        notifyItemChanged(singleItemSelectionPosition)
        singleItemSelectionPosition = adapterPosition
        notifyItemChanged(singleItemSelectionPosition)
    }
}


