package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
//                println(bindingAdapterPosition)
            }
        }
        fun bind(event: Event){
//            binding.itemText.text = "${event.courtName}  ${event.sportName}  ${event.startTime}  ${event.date}"
            binding.itemTextRank.text = ranking.toString()
            ranking += 1
            binding.itemTextDate.text = event.date.toString()
            binding.itemTextName.text = event.courtName.toString()
        }

    }
    val events = mutableListOf<Event>()
//    var ranking:Int = 1

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

var ranking: Int = 1
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
        println("active ${it}")
        gotoRevDetailFrag(it.resId)
    }
    private fun gotoRevDetailFrag(resId: String){
        sharedvm.setHisReview(resId)
        vmMain.setShowNav(false)
        findNavController().navigate(R.id.action_historyFragment_to_ownRevFragment)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedvm.getHistoryResbyUser(vmMain.user)
        sharedvm.reservations.observe(this){
            // 从viewmodel获取数据（viewmodel从数据库拿到数据）
            events.clear()
            ranking = 1
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
        ranking = 1
        binding = FragmentHistoryBinding.bind(view)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

//        binding.calBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_historyFragment_to_profileFragment)
//            vmMain.setShowNav(true)
//        }




    }
    private fun updateAdapter() {
        adapter.apply {
            events.clear()
            events.addAll(this@HistoryFragment.events)
//            println("2 $courtInfoList")
            notifyDataSetChanged() // 通知adapter发生变化，调用bind方法
        }
    }
}
