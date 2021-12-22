package ie.wit.gameplan.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.gameplan.firebase.FirebaseDBManager
import ie.wit.gameplan.models.GameManager
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.Location
import timber.log.Timber

class GameViewModel : ViewModel() {

    private val game = MutableLiveData<GameModel>()

    var observableGame: LiveData<GameModel>
        get() = game
        set(value) {game.value = value.value}

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status



    fun addGame(firebaseUser: MutableLiveData<FirebaseUser>,
                game: GameModel) {
        status.value = try {
            //DonationManager.create(donation)
            FirebaseDBManager.create(firebaseUser,game)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }




    fun updateGame(userid:String, id: String, game: GameModel) {
        try {
            FirebaseDBManager.update(userid, id, game)

        } catch (e: IllegalArgumentException) {

        }
    }

    fun getGame(id: String) {
        try {
            //DonationManager.findById(email, id, donation)
            FirebaseDBManager.findById(id, game)
            Timber.i("Detail getDonation() Success : ${
                game.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getDonation() Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

    private var location = MutableLiveData<Location>()

    val observableGameList: LiveData<Location>
        get() = location





}