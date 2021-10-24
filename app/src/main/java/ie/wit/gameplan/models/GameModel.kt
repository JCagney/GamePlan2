package ie.wit.gameplan.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class GameModel(var id: Long = 0,
                     var title: String = "",
                     var description: String = "",
                     var date: LocalDate = LocalDate.now(),
                     var lat: Double = 0.0,
                     var lng: Double = 0.0,
                     var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
