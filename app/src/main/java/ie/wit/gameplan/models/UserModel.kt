package ie.wit.gameplan.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(var email: String = "",
                     var firstName: String = "",
                     var lastName: String = "",
                     var password: String = ""
): Parcelable
