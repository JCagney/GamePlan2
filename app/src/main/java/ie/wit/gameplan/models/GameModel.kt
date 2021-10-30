package ie.wit.gameplan.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Parcelize
data class GameModel(var id: Long = 0,
                     var title: String = "",
                     var description: String = "",
                     var date: String = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(
                         FormatStyle.LONG)),
                     var lat: Double = 0.0,
                     var lng: Double = 0.0,
                     var zoom: Float = 0f,
                     var creator: String = "",
                     var creatorPic: Uri = Uri.EMPTY) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
