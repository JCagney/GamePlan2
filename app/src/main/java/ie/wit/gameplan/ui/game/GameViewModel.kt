package ie.wit.gameplan.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.gameplan.models.GameManager
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.Location

class GameViewModel : ViewModel() {

    fun addGame(game: GameModel) {
        try {
            GameManager.create(game)

        } catch (e: IllegalArgumentException) {

        }
    }

    fun updateGame(game: GameModel) {
        try {
            GameManager.update(game)

        } catch (e: IllegalArgumentException) {

        }
    }

    private var location = MutableLiveData<Location>()

    val observableGameList: LiveData<Location>
        get() = location





}