package com.example.lab3

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.CalendarDayTitlesContainerBinding
import com.example.lab3.databinding.CalenderDayLayoutBinding
import com.example.lab3.databinding.FragmentCalenderViewBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class Calender : Fragment(R.layout.fragment_calender_view) {
    private lateinit var binding: FragmentCalenderViewBinding

    companion object {
        fun newInstance() = Calender()
    }

    private lateinit var viewModel: CalenderViewModel

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_calender_view, container, false)
//    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        println("onDetach")

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalenderViewBinding.bind(view)
        println("onViewCreated")

        val currentMonth = YearMonth.now() // 2023-04
        val startMonth = currentMonth.minusMonths(100) //2014-12
        val endMonth = currentMonth.plusMonths(100) // 2031-08
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
        configureBinders(daysOfWeek)

        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)

//        val titlesContainer = view.findViewById<ViewGroup>(R.id.titlesContainer)
//        titlesContainer.children
//            .map { it as TextView }
//            .forEachIndexed { index, textView ->
//                val dayOfWeek = daysOfWeek[index]
//                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
//                textView.text = title
//            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalenderViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View): ViewContainer(view) {
            val binding =CalenderDayLayoutBinding.bind(view)
            val context = binding.root.context
            val textView = binding.dayText
            val layout = binding.dayLayout
//            val textView = view.findViewById<TextView>(R.id.exFiveDayText)

        }
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer>{
            // called only when a new container is needed
            override fun create(view: View) = DayViewContainer(view)

            //called every time we need to reuse a container  显示日期
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
            }

        }
        class MonthViewContainer(view: View) : ViewContainer(view) {
//            val legendLayout = CalendarDayTitlesContainerBinding.bind(view).root
            val titlesContainer = view.findViewById<ViewGroup>(R.id.titlesContainer)
        }

        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer>{

                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.titlesContainer.tag == null) {

                        container.titlesContainer.tag = data.yearMonth // 当前月
                        container.titlesContainer.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                val dayOfWeek = daysOfWeek[index]
                                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                tv.text = title
                            }
                    }
                }

            }

    }
}
