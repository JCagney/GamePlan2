package ie.wit.gameplan.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.gameplan.firebase.FirebaseDBManager
import ie.wit.gameplan.models.GameModel

class GameListViewModel : ViewModel() {

    private val gameList = MutableLiveData<List<GameModel>>()

    val observableGameList: LiveData<List<GameModel>>
        get() = gameList

    init {
        load()
    }

    fun load() {
        FirebaseDBManager.findAll(gameList)
    }

}