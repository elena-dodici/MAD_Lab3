package com.example.lab3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import java.sql.Time


class AddReservationFragment : Fragment() {

    companion object {
        fun newInstance() = AddReservationFragment()
    }

    private var layoutManager : RecyclerView.LayoutManager ?= null
    private var  adapter : MyAdapter1 ?= null
    private lateinit var viewModel: AddReservationViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_add_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView2)
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
            CourtTime(1, Time(18,0,0), Time(19,0,0))
        )

        recyclerView.apply {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = MyAdapter1(ct)
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddReservationViewModel::class.java)
        // TODO: Use the ViewModel

    }

}

/*class MyViewHolder1(v : View): RecyclerView.ViewHolder(v){
    val tv = v.findViewById<TextView>(R.id.item_time_slot)
    init {
        tv.setOnClickListener {
            setSingleSelection(adapterPosition)
        }
    }

}*/

class MyAdapter1(val l: List<CourtTime>) : RecyclerView.Adapter<MyAdapter1.MyViewHolder1>(){
    var singleItemSelectionPosition = -1

    inner class MyViewHolder1(v : View): RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<TextView>(R.id.item_time_slot)

        init {
            tv.setOnClickListener {
                setSingleSelection(adapterPosition)
                println(l[adapterPosition].startTime.toString())
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


