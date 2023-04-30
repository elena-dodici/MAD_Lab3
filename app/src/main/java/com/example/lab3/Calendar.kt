package com.example.lab3

import android.animation.ValueAnimator
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.example.lab3.databinding.CalendarDayLayoutBinding
import com.example.lab3.databinding.FragmentCalendarViewBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class Calendar : Fragment(R.layout.fragment_calendar_view) {
    private lateinit var binding: FragmentCalendarViewBinding
    private val monthCalendarView: CalendarView get() = binding.calendarView
    private val weekCalendarView: WeekCalendarView get() = binding.weekCalendar

    companion object {
        fun newInstance() = Calendar()
    }

    private lateinit var viewModel: CalendarViewModel

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_calender_view, container, false)
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarViewBinding.bind(view)


        val currentMonth = YearMonth.now() // 2023-04
        val startMonth = currentMonth.minusMonths(100) //2014-12
        val endMonth = currentMonth.plusMonths(100) // 2031-08
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
        configureBinders(daysOfWeek)
        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)
        weekCalendarView.setup(startMonth.atStartOfMonth(), endMonth.atEndOfMonth(), daysOfWeek.first())
        weekCalendarView.scrollToWeek(currentMonth.atStartOfMonth())

        // 箭头 下一个月
        binding.nextMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }
        // 箭头 上一个月
        binding.previousMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }

        monthCalendarView.isInvisible = binding.weekModeCheckBox.isChecked
        weekCalendarView.isInvisible = !binding.weekModeCheckBox.isChecked
        binding.weekModeCheckBox.setOnCheckedChangeListener(weekModeToggled)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private val weekModeToggled = object :CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(buttonView: CompoundButton, monthToWeek: Boolean) {
            // We want the first visible day to remain visible after the
            // change so we scroll to the position on the target calendar.
            if (monthToWeek) {
                val targetDate = monthCalendarView.findFirstVisibleDay()?.date ?: return
                weekCalendarView.scrollToWeek(targetDate)
            } else {
                // It is possible to have two months in the visible week (30 | 31 | 1 | 2 | 3 | 4 | 5)
                // We always choose the second one. Please use what works best for your use case.
                val targetMonth = weekCalendarView.findLastVisibleDay()?.date?.yearMonth ?: return
                monthCalendarView.scrollToMonth(targetMonth)
            }

            val weekHeight = weekCalendarView.height
            // If OutDateStyle is EndOfGrid, you could simply multiply weekHeight by 6.
            val visibleMonthHeight = weekHeight *
                    monthCalendarView.findFirstVisibleMonth()?.weekDays.orEmpty().count()

            val oldHeight = if (monthToWeek) visibleMonthHeight else weekHeight
            val newHeight = if (monthToWeek) weekHeight else visibleMonthHeight

            // Animate calendar height changes.
            val animator = ValueAnimator.ofInt(oldHeight, newHeight)
            animator.addUpdateListener { anim ->
                monthCalendarView.updateLayoutParams {
                    height = anim.animatedValue as Int
                }
                // A bug is causing the month calendar to not redraw its children
                // with the updated height during animation, this is a workaround.
                monthCalendarView.children.forEach { child ->
                    child.requestLayout()
                }
            }

            animator.doOnStart {
                if (!monthToWeek) {
                    weekCalendarView.isInvisible = true
                    monthCalendarView.isVisible = true
                }
            }
            animator.doOnEnd {
                if (monthToWeek) {
                    weekCalendarView.isVisible = true
                    monthCalendarView.isInvisible = true
                } else {
                    // Allow the month calendar to be able to expand to 6-week months
                    // in case we animated using the height of a visible 5-week month.
                    // Not needed if OutDateStyle is EndOfGrid.
                    monthCalendarView.updateLayoutParams { height =
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
                updateTitle()
            }
            animator.duration = 250
            animator.start()
        }
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        // set up month calender
        class DayViewContainer(view: View): ViewContainer(view) {
            lateinit var day :CalendarDay
            val binding = CalendarDayLayoutBinding.bind(view)

//            val textView = view.findViewById<TextView>(R.id.exFiveDayText)
            init {
                view.setOnClickListener{
                    println("Click!")
                }
            }
        }
        monthCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer>{
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
        // 显示当前月 update title
        monthCalendarView.monthScrollListener = {updateTitle()}
//        binding.calendarView.monthScrollListener = { month->
//            binding.monthYearText.text = month.yearMonth.displayText()
//        }
        class WeekDayViewContainer(view:View):ViewContainer(view) {
            lateinit var day:WeekDay
            val binding = CalendarDayLayoutBinding.bind(view)
            init {
                view.setOnClickListener{
                    println("Click!")
                }
            }
        }

        weekCalendarView.dayBinder = object : WeekDayBinder<WeekDayViewContainer>{
            // called only when a new container is needed
            override fun create(view: View) = WeekDayViewContainer(view)

            //called every time we need to reuse a container  显示日期
            override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                container.day = data
                val context = container.binding.root.context
                val textView = container.binding.dayText
                val layout = container.binding.dayLayout
                textView.text = data.date.dayOfMonth.toString()
                textView.setTextColor(Color.WHITE)
                textView.setTextSize(18F)
            }
        }
        weekCalendarView.weekScrollListener = { updateTitle() }




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
    private fun updateTitle() { // 更新月份
        val isMonthMode = !binding.weekModeCheckBox.isChecked
        if (isMonthMode) {
            val month = monthCalendarView.findFirstVisibleMonth()?.yearMonth ?: return
            binding.yearText.text = month.year.toString()
            binding.monthText.text = month.month.displayText(short = false)
        } else {
            val week = weekCalendarView.findFirstVisibleWeek() ?: return
            // In week mode, we show the header a bit differently because
            // an index can contain dates from different months/years.
            val firstDate = week.days.first().date
            val lastDate = week.days.last().date
            if (firstDate.yearMonth == lastDate.yearMonth) {
                binding.yearText.text = firstDate.year.toString()
                binding.monthText.text = firstDate.month.displayText(short = false)
            } else {
                binding.monthText.text =
                    firstDate.month.displayText(short = false) + " - " +
                            lastDate.month.displayText(short = false)
                if (firstDate.year == lastDate.year) {
                    binding.yearText.text = firstDate.year.toString()
                } else {
                    binding.yearText.text = "${firstDate.year} - ${lastDate.year}"
                }
            }
        }
    }
}
