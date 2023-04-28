package com.example.lab3

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.example.lab3.databinding.CalenderDayLayoutBinding
import com.example.lab3.databinding.FragmentCalenderViewBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalenderViewBinding.bind(view)


        val currentMonth = YearMonth.now() // 2023-04
        val startMonth = currentMonth.minusMonths(100) //2014-12
        val endMonth = currentMonth.plusMonths(100) // 2031-08
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
        configureBinders(daysOfWeek)

        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)

        binding.calendarView.monthScrollListener = { month->
            binding.monthYearText.text = month.yearMonth.displayText()

        }

        binding.nextMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }
        binding.previousMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalenderViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View): ViewContainer(view) {
            lateinit var day :CalendarDay
            val binding =CalenderDayLayoutBinding.bind(view)

//            val textView = view.findViewById<TextView>(R.id.exFiveDayText)
            init {
                view.setOnClickListener{
                    println("Click!")
                }
            }
        }
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer>{
            // called only when a new container is needed
            override fun create(view: View) = DayViewContainer(view)

            //called every time we need to reuse a container  显示日期
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val context = container.binding.root.context
                val textView = container.binding.dayText
                val layout = container.binding.dayLayout
                textView.text = data.date.dayOfMonth.toString()
                if (data.position == DayPosition.MonthDate) {
                    textView.setTextColor(Color.WHITE)
                    textView.setTextSize(18F)
                } else {
                    textView.setTextColor(Color.GRAY)
                    textView.setTextSize(15F)

                }
            }

        }
        class MonthViewContainer(view: View) : ViewContainer(view) {
//            val legendLayout = CalendarDayTitlesContainerBinding.bind(view).root
            val titlesContainer = view.findViewById<ViewGroup>(R.id.titlesContainer)
        }
        // header: Mon|Tue|Wed|Thu|Fri|Sat|Sun
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
                                tv.setTextColorRes(R.color.white)

                            }
                    }
                }

            }

    }
}
