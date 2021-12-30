package ie.wit.gameplan.models

import android.net.Uri
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@IgnoreExtraProperties
@Parcelize
data class GameModel(var uid: String? = "",
                     var title: String = "",
                     var description: String = "",
                     var date: String = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(
                         FormatStyle.LONG)),
                     var lat: Double = 0.0,
                     var lng: Double = 0.0,
                     var zoom: Float = 0f,
                     var creator: String = "",
                     var profilepic: String = "",
                     //var location: String = LatLng(0.0,0.0).toString()
                     //var creatorPic: Uri = Uri.EMPTY
    ) : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "description" to description,
            "date" to date,
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom,
            "creator" to creator,
            "profilepic" to profilepic
            //"creatorPic" to creatorPic
        )
    }
}


@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
