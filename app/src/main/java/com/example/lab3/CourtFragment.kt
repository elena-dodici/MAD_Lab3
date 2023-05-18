package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.ItemLayoutBinding
import com.example.lab3.databinding.ItemLayoutCourtScoreBinding

data class CourtInfo(val courtName:String, val avgScore:Float)

class ScoreAdapter(val onTap:(CourtInfo)->Unit):RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>(){
    inner class  ScoreViewHolder(private val binding:ItemLayoutCourtScoreBinding):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                onTap(courtInfoList[bindingAdapterPosition])
            }
        }
        fun bind(courtInfo:CourtInfo){
            binding.itemTextCourt.text = courtInfo.courtName
            binding.itemTextScore.text = courtInfo.avgScore.toString()
        }
    }
    val courtInfoList = mutableListOf<CourtInfo>()
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
class CourtFragment : BaseFragment(R.layout.fragment_court) {
    companion object {
        fun newInstance() = CourtFragment()
    }
    private val sharedvm : CourtViewModel by activityViewModels()
    private val courtInfoList = mutableListOf<CourtInfo>()
    val adapter = ScoreAdapter{
        println("click: ${it.courtName} - ${it.avgScore}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



}