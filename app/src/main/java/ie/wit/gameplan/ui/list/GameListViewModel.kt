package ie.wit.gameplan.ui.list

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.gameplan.firebase.FirebaseDBManager
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.ui.auth.LoggedInViewModel
import timber.log.Timber
import java.lang.Exception


class GameListViewModel : ViewModel() {

    private val gameList = MutableLiveData<List<GameModel>>()

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    var readOnly = MutableLiveData(false)

    val observableGameList: LiveData<List<GameModel>>
        get() = gameList



    init {
        load()
    }



    fun load() {
        try {
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, gameList)
            Timber.i("Report Load Success : ${gameList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(gameList)
            Timber.i("Report LoadAll Success : ${gameList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }



}