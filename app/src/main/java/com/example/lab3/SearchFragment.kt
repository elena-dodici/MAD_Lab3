package com.example.lab3

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.CalendarDayLayoutBinding
import com.example.lab3.databinding.FragmentCalendarViewBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.UUID


class SearchFragment() : BaseFragment(R.layout.fragment_search),HasToolbar {

    val sports = listOf("running", "basketball", "swimming")
    val vm by viewModels<CalendarViewModel>()

     override val toolbar: Toolbar?
        get() = null
    //    override val titleRes: Int? = null

    private lateinit var binding: FragmentCalendarViewBinding
    private val monthCalendarView: CalendarView get() = binding.calendarView
    private val weekCalendarView: WeekCalendarView get() = binding.weekCalendar
    private var selectedDate:LocalDate? = null

    private val events = mutableMapOf<LocalDate, List<Event>>()

    val eventsAdapter = MyAdapter{
        // 点击下面recyclerView调用的事件
        println(it.resId)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 将res放入event数组即可！
        vm.getResBySport(this.requireActivity().application,"running")
        vm.reservations.observe(viewLifecycleOwner){
            // 从viewmodel获取数据（viewmodel从数据库拿到数据）
            for (res in it){
//                println(">>>>> $res")
                events[res.date] = events[res.date].orEmpty().plus(Event(res.resId, UUID.randomUUID().toString(), res.name, res.sport, res.startTime, res.date))
            }

        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarViewBinding.bind(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val spinnerSports = view.findViewById<Spinner>(R.id.sportSpinner)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sports)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSports.adapter = adapter
        //监听下拉菜单

        spinnerSports.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // 获取当前选中的字符串
                val selectedSport = parent.getItemAtPosition(position).toString()
                updateCalendar(selectedSport)
                updateAdapterForDate(selectedDate)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        recyclerView.adapter = eventsAdapter
        recyclerView.layoutManager =  LinearLayoutManager(this.context, RecyclerView.VERTICAL,false )

        val currentMonth = YearMonth.now() // 2023-04
        val startMonth = currentMonth.minusMonths(100) //2014-12
        val endMonth = currentMonth.plusMonths(100) // 2031-08
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.SUNDAY)
        configureBinders()
//    static header: Mon|Tue|Wed|Thu|Fri|Sat|Sun
        binding.legendLayout.root.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].displayText()
                textView.setTextColorRes(R.color.example_1_white)
            }

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



    private val weekModeToggled = object :CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(buttonView: CompoundButton, monthToWeek: Boolean) {
            if (monthToWeek) {
                val targetDate = monthCalendarView.findFirstVisibleDay()?.date ?: return
                weekCalendarView.scrollToWeek(targetDate)
            } else {
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
                updateMonthTitle()
            }
            animator.duration = 250
            animator.start()
        }
    }
    private fun updateAdapterForDate(date: LocalDate?) { // 在列表中将某个date的events显示出来
        eventsAdapter.apply {
            events.clear()
            events.addAll(this@SearchFragment.events[date].orEmpty())
            notifyDataSetChanged()
        }

    }
    private fun configureBinders() {
        // set up month calender
        class DayViewContainer(view: View): ViewContainer(view) {
            lateinit var day :CalendarDay
            val binding = CalendarDayLayoutBinding.bind(view)

            //            val textView = view.findViewById<TextView>(R.id.exFiveDayText)
            init {
                view.setOnClickListener{ // click one day
                    if(day.position == DayPosition.MonthDate){ // 当前月的才能选
                        dateClicked(date = day.date)
                    }
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
                val dotView = container.binding.DotView
                bindDate(data.date, textView, dotView, layout, data.position == DayPosition.MonthDate)
//                textView.text = data.date.dayOfMonth.toString()
                if (data.position == DayPosition.MonthDate) {
                    textView.setTextColor(Color.BLACK)
//                    textView.setTextSize(16f)
                } else {
                    textView.setTextColor(Color.GRAY)
//                    textView.setTextSize(16f)
                }
            }
        }
        // 显示当前月 update title
        monthCalendarView.monthScrollListener = {updateMonthTitle();clearBackground()}
        //        binding.calendarView.monthScrollListener = { month->
//            binding.monthYearText.text = month.yearMonth.displayText()
//        }
        class WeekDayViewContainer(view:View):ViewContainer(view) {
            lateinit var day:WeekDay
            val binding = CalendarDayLayoutBinding.bind(view)
            init {
                view.setOnClickListener{ // click one day
                    if (day.position == WeekDayPosition.RangeDate){
                        dateClicked(date = day.date)
                    }

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
                val dotView = container.binding.DotView
                bindDate(data.date, textView, dotView, layout, data.position == WeekDayPosition.RangeDate)
//                textView.text = data.date.dayOfMonth.toString()
                textView.setTextColor(Color.BLACK)
//                textView.setTextSize(16f)
            }
        }
        weekCalendarView.weekScrollListener = { updateMonthTitle();clearBackground() }


    }
    private fun bindDate(date:LocalDate, textView: TextView, dotView:View, layout:ConstraintLayout, isSelectable:Boolean){
        textView.text = date.dayOfMonth.toString()
        if (isSelectable){

            if (selectedDate == date){ // 选择的日期
                textView.setTextColorRes(R.color.blue)
                textView.setBackgroundResource(R.drawable.selected_bg)
                dotView.makeInVisible()

            }
            else{
                textView.setTextColorRes(R.color.black)

                textView.background = null
                dotView.isVisible = events[date].orEmpty().isNotEmpty()

            }
        }else{
            textView.background = null
            dotView.makeInVisible()

        }

    }
    private fun dateClicked(date: LocalDate) {
        if(selectedDate != date){
            val oldDate = selectedDate
            selectedDate = date
//                        println(day.date) // day.date就是点击的日期
//        Refresh both calendar views..
            monthCalendarView.notifyDateChanged(date)
            weekCalendarView.notifyDateChanged(date)
            oldDate?.let {
                monthCalendarView.notifyDateChanged(it)
                weekCalendarView.notifyDateChanged(it)

            }
            updateAdapterForDate(date) // 将当前date的events的内容显示在下面列表
        }

    }
    private fun updateMonthTitle() { // 更新月份

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
    private fun clearBackground(){
        selectedDate?.let {//切换mode的时候也会调用，但这时候不应该清除
            // clear selection if we scroll to a new month/week
            selectedDate = null
            monthCalendarView.notifyDateChanged(it)
            weekCalendarView.notifyDateChanged(it)
            updateAdapterForDate(null)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun updateCalendar(selectedSport: String) {
        vm.getResBySport(this.requireActivity().application,selectedSport)
        vm.reservations.observe(viewLifecycleOwner){
            val oldDate=events.keys
//            events.forEach(){
//                monthCalendarView.notifyDateChanged(it.key)
//                weekCalendarView.notifyDateChanged(it.key)
//            }
            events.clear()
            oldDate.forEach(){
                monthCalendarView.notifyDateChanged(it)
                weekCalendarView.notifyDateChanged(it)
            }

            for (res in it){
                println(res)
                events[res.date] = events[res.date].orEmpty().plus(Event(res.resId, UUID.randomUUID().toString(), res.name, res.sport, res.startTime, res.date))
                monthCalendarView.notifyDateChanged(res.date)
                weekCalendarView.notifyDateChanged(res.date)
            }


        }

    }

    companion object {
        fun newInstance() = SearchFragment()
    }


}