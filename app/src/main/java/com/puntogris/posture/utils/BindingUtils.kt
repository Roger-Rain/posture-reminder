package com.puntogris.posture.utils

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.BindingAdapter
import com.db.williamchart.view.DonutChartView
import com.puntogris.posture.R
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.utils.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.lang.Exception

@BindingAdapter("reminderSummaryStatus")
fun TextView.setReminderSummaryStatus(isActive: Boolean){
    text = if (isActive) context.getString(R.string.alarm_on) else context.getString(
        R.string.alarm_off)
}

@BindingAdapter("reminderStatus")
fun TextView.setReminderStatus(isActive: Boolean){
    text = if (isActive) context.getString(R.string.stop_alarm) else context.getString(
        R.string.start_alarm)
}

@BindingAdapter("imageFromRes")
fun ImageView.setImageFromRes(@DrawableRes image: Int){
    setImageDrawable(ContextCompat.getDrawable(context, image))
}


@BindingAdapter("exerciseDuration")
fun TextView.setExerciseDuration(duration: Int){
    text = context.getString(R.string.exercise_duration_seconds, duration)
}

@BindingAdapter("minutesToHourlyTime")
fun TextView.setMinutesToHourlyTime(minutes: Int){
    text = Utils.minutesFromMidnightToHourlyTime(minutes)
}

@BindingAdapter("daysSummary")
fun TextView.setDaysSummary(reminder: Reminder?){
    text = reminder?.alarmDaysSummary(resources.getStringArray(R.array.alarmDays))
}

@BindingAdapter("reminderColor")
fun ImageView.setReminderColor(color:Int){
    backgroundTintList = try {
        ColorStateList.valueOf(getColor(context, color))
    }catch (e:Exception){
        ColorStateList.valueOf(color)
    }
}

@BindingAdapter("dayMonth")
fun TextView.setDayMonth(date: String){
    val dateNow = LocalDate.parse(date)
    text = dateNow.format(DateTimeFormatter.ofPattern("d MMM"))
}

@BindingAdapter("pagerDay")
fun TextView.setPagerDay(position: Int){
    text = if (position == 0) "Hoy" else "Ayer"
}

@BindingAdapter("accountLevelTitle")
fun TextView.setAccountLevelTitle(exp: Int){
    text = if (exp < 100 )"Nivel 1" else "Nivel ${(exp / 100)}"
}

@BindingAdapter("accountBadgeLevel")
fun TextView.setAccountBadgeLevel(exp: Int){
    val string =  when(exp / 100){
        0 -> R.string.level_1_title
        1 -> R.string.level_2_title
        2 -> R.string.level_3_title
        3 -> R.string.level_4_title
        4 -> R.string.level_5_title
        5 -> R.string.level_6_title
        else -> R.string.level_7_title
    }
    text = context.getString(string)
}

@BindingAdapter("expForNextLevel")
fun TextView.setExpForNextLevel(exp: Int){
    val level = if (exp < 100) 1 else (exp / 100)
    text = "+${100 - (exp - (exp / 100) * 100)} para lvl ${level + 1}"
}

@BindingAdapter("expFromTotalLevel")
fun TextView.setExpFromTotalLevel(exp: Int){
    text = "${exp - (exp / 100) * 100} / 100"
}

@BindingAdapter("donutChartProgress")
fun DonutChartView.setDonutChartProgress(exp: Int){
    donutColors = intArrayOf(getColor(context, R.color.colorPrimaryDark))
    animation.duration = 1000
    val data = if (exp < 100) exp else exp - (exp / 100) * 100
    animate(listOf(data.toFloat()))
}

@BindingAdapter("donutLevel")
fun TextView.setDonutLevel(exp: Int){
    text = if (exp < 100 )"Lvl. 1" else "Lvl. ${(exp / 100)}"
}

@BindingAdapter("rankingLevel")
fun TextView.setRankingLevel(exp: Int){
    text = if (exp < 100 )"lvl. 1" else "lvl. ${(exp / 100)}"
}

@BindingAdapter("profileRankingNumber")
fun TextView.setProfileRankingNumber(position: Int){
    if (position in 0..2) gone()
    else {
        visible()
        text = (position + 1).toString()
    }
}

@BindingAdapter("profileRankingMedal")
fun ImageView.setProfileRankingMedal(position: Int){
    when (position) {
        0 -> R.drawable.ic_gold_medal
        1 -> R.drawable.ic_silver_medal
        2 -> R.drawable.ic_bronze_medal
        else -> null
    }?.let {
        setImageDrawable(ContextCompat.getDrawable(context, it))
    }
}

@BindingAdapter("backgroundColorTintView")
fun View.setBackgroundColorTintView(color: Int){
    backgroundTintList = try {
        ColorStateList.valueOf(getColor(context, color))
    }catch (e:Exception){
        ColorStateList.valueOf(color)
    }
}

@BindingAdapter("progressBarSmoothMax")
fun ProgressBar.setProgressBarSmoothMax(duration: Int){
    max = duration * PROGRESS_BAR_SMOOTH_OFFSET
}
