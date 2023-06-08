package com.example.lab3

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.FragmentHistoryBinding
import com.example.lab3.databinding.FragmentProfileBinding
import com.example.lab3.databinding.ItemLayoutBinding
import com.example.lab3.databinding.ItemLayoutHistoryBinding

import java.time.LocalDate
import java.util.UUID

class HistoryAdapter(val onClick: (Event)-> Unit): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){
    inner class HistoryViewHolder(private val binding: ItemLayoutHistoryBinding): RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener { // 点击具体某个reservation时调用
                onClick(events[bindingAdapterPosition])
            }
        }
        fun bind(event: Event){
//            binding.itemText.text = "${event.courtName}  ${event.sportName}  ${event.startTime}  ${event.date}"
            binding.itemTextRank.text = "${bindingAdapterPosition + 1}"
            binding.itemTextDate.text = event.date.toString()
            binding.itemTextName.text = event.courtName.toString()
        }

    }
    val events = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val b = ItemLayoutHistoryBinding.inflate(parent.context.layoutInflater, parent, false)
        return HistoryViewHolder(b)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(events[position])
    }
}

class HistoryFragment : BaseFragment(R.layout.fragment_history),HasBackButton{
    private lateinit var  binding: FragmentHistoryBinding
    private val sharedvm : HistoryViewModel by activityViewModels()
    private val vmMain : MainViewModel by activityViewModels()

    private val events = mutableListOf<Event>()

    companion object {
        fun newInstance() = HistoryFragment()
    }
//    override val toolbar: Toolbar
//        get() = binding.activityToolbar
    override val titleRes: Int = R.string.history_title

    val adapter = HistoryAdapter{

        gotoRevDetailFrag(it.resId)
    }
    private fun gotoRevDetailFrag(resId: String){
        sharedvm.setHisReview(resId)
        vmMain.setShowNav(false)
        findNavController().navigate(R.id.action_historyFragment_to_ownRevFragment)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedvm.getHistoryResbyUser(vmMain.UID)
        sharedvm.reservations.observe(this){
            events.clear()
            for (res in it){
                events.add( Event(UUID.randomUUID().toString(), res.resId, res.name, res.sport,res.startTime, res.date))
            }
            events.sortBy {
                it.date
            }
            updateAdapter()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)


        val viewStub = view.findViewById<ViewStub>(R.id.emptyStateLayout)
        if (sharedvm.reservations.value.isNullOrEmpty()) {
            viewStub.visibility = View.VISIBLE
            // Perform any necessary operations on the inflated view
        } else {
            viewStub.visibility = View.GONE
        }

    }




    private fun updateAdapter() {
        adapter.apply {
            events.clear()
            events.addAll(this@HistoryFragment.events)
            notifyDataSetChanged() // 通知adapter发生变化，调用bind方法
        }
    }
}
