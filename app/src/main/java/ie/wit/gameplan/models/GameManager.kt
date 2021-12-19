package ie.wit.gameplan.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object GameManager : GameStore {
    override fun findAll(gameList: MutableLiveData<List<GameModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, gameList: MutableLiveData<List<GameModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(userid: String, gameid: String, game: MutableLiveData<GameModel>) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, game: GameModel) {
        TODO("Not yet implemented")
    }

    override fun delete(userid: String, gameid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, gameid: String, game: GameModel) {
        TODO("Not yet implemented")
    }

}