package ie.wit.gameplan.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(var email: String = "",
                     var firstName: String = "",
                     var lastName: String = "",
                     var passwordHash: Int = 0,
                     var image: Uri = Uri.EMPTY
): Parcelable
