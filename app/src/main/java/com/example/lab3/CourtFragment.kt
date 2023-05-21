package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.database.entity.CourtInfo
import com.example.lab3.databinding.FragmentCourtBinding
import com.example.lab3.databinding.ItemLayoutBinding
import com.example.lab3.databinding.ItemLayoutCourtScoreBinding
import java.time.LocalDate
import kotlin.math.roundToInt

//data class CourtInfo(val courtName:String, val avgScore:Float)

class ScoreAdapter(val onTap:(CourtInfo)->Unit):RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>(){
    inner class  ScoreViewHolder(private val binding:ItemLayoutCourtScoreBinding):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                onTap(courtInfoList[bindingAdapterPosition])
            }
        }
        fun bind(courtInfo:CourtInfo){
            binding.itemTextRank.text =ranking.toString()
            ranking+=1
            binding.itemTextCourt.text = courtInfo.courtname
            courtInfo.avg_rating = ((courtInfo.avg_rating *100.0).roundToInt() / 100.0).toFloat()
            binding.itemTextScore.text =  courtInfo.avg_rating.toString()
        }
    }
    val courtInfoList = mutableListOf<CourtInfo>()
    var ranking:Int = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val b = ItemLayoutCourtScoreBinding.inflate(parent.context.layoutInflater, parent, false)
        return ScoreViewHolder(b)
    }

    override fun getItemCount(): Int {
        return courtInfoList.size
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bind(courtInfoList[position])
    }
}
class CourtFragment : BaseFragment(R.layout.fragment_court), HasToolbar {
    companion object {
        fun newInstance() = CourtFragment()
    }
    override val toolbar: Toolbar?
        get() = binding.activityToolbar
    private lateinit var binding:FragmentCourtBinding
    private val sharedvm : CourtViewModel by activityViewModels()
    private val courtInfoList = mutableListOf<CourtInfo>()
    val adapter = ScoreAdapter{
        println("click: ${it.courtname} - ${it.avg_rating}")
        //avg_rating: 0-xxx
        gotoCourtDetailFrag(it.courtId, it.avg_rating)
    }
    private fun gotoCourtDetailFrag(courtId: Int, avg_rating:Float){
       sharedvm.setSelectedCourtById(courtId,avg_rating, this.requireActivity().application)
        findNavController().navigate(R.id.action_courtFragment_to_courtDetailFragment)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedvm.getCourtInfo(this.requireActivity().application)
        sharedvm.courtInfo.observe(this){
            courtInfoList.clear();
            courtInfoList.addAll(it)
            updateAdapter()
            //println("1 ${courtInfoList}")
        }
//        println(courtInfoList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCourtBinding.bind(view)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL,false)
//        updateAdapter()

    }


    private fun updateAdapter() {
        adapter.apply {
            courtInfoList.clear()
            courtInfoList.addAll(this@CourtFragment.courtInfoList)
           // println("2 ${this@CourtFragment.courtInfoList}")

        }

    }
}