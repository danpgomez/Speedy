package com.e.speedy.runTracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.e.speedy.R
import com.e.speedy.convertDurationToFormatted
import com.e.speedy.convertNumericQualityToString
import com.e.speedy.database.Run

@BindingAdapter("runDurationFormatted")
fun TextView.setRunDurationFormatted(item: Run?) {
    item?.let {
        text = convertDurationToFormatted(item.startTimeMillis, item.endTimeMillis, context.resources)
    }
}

@BindingAdapter("runQualityString")
fun TextView.setRunQualityFormatted(item: Run?) {
    item?.let {
        text = convertNumericQualityToString(item.runQuality, context.resources)
    }
}

@BindingAdapter("runQualityImage")
fun ImageView.setRunQualityImage(item: Run?) {
    item?.let {
        setImageResource(
            when (item.runQuality) {
                1 -> R.drawable.very_dissatisfied
                2 -> R.drawable.dissatisfied
                3 -> R.drawable.satisfied
                4 -> R.drawable.very_satisfied
                else -> R.drawable.active_run
            }
        )
    }
}