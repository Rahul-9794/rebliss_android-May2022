package com.rebliss.utils

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.rebliss.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun View.disableCard() {
    this.setBackgroundColor(Color.parseColor("#A4AFB7"))
    this.isEnabled = false
    this.isClickable = false
}

fun View.enableCard() {
    //dynamic image change
    this.setBackgroundColor(Color.parseColor("#D4EADE"))
    this.isEnabled = true
    this.isClickable = true
}

fun TextView.selectButton() {
    this.background = ContextCompat.getDrawable(this.context, R.drawable.button_selected)
    this.setTextColor(ContextCompat.getColor(this.context, R.color.black))
}

fun TextView.deselectButton() {
    this.background = ContextCompat.getDrawable(this.context, R.drawable.button_unselected)
    this.setTextColor(ContextCompat.getColor(this.context, R.color.text_color))
}

fun getVideoIdFromYoutubeUrl(url: String?): String? {

    var videoId: String? = null

    val regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)"
    val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(url)
    if (matcher.find()) {
        videoId = matcher.group(1)
    }
    return videoId
}

fun getCurrentMonthDate(): String {
    var formattedDate = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Calendar.getInstance().time)
    formattedDate += "-01"
    return formattedDate
}

fun getDisplayFormatDate(date: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formatter = SimpleDateFormat("dd.MM.yyyy")
    val formattedDate = formatter.format(parser.parse(date))
    return formattedDate
}
fun getDisplayFormatDate1(date: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd")
    val formatter = SimpleDateFormat("dd-MMM-yyyy")
    val formattedDate = formatter.format(parser.parse(date))
    return formattedDate
}
fun getServerFormatDate(date: String?): String? {
    val parser = SimpleDateFormat("dd-MM-yyyy")
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val formattedDate = formatter.format(parser.parse(date))
    return formattedDate
}

fun getServerDisplayFormatDate(date: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd")
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    val formattedDate = formatter.format(parser.parse(date))
    return formattedDate
}
