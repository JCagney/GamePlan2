package ie.wit.gameplan.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface GameStore {
    fun findAll(gameList:
                MutableLiveData<List<GameModel>>)
    fun findAll(userid:String,
                gameList:
                MutableLiveData<List<GameModel>>)
    fun findById(userid:String, gameid: String,
                 game: MutableLiveData<GameModel>)
    fun findById(gameid: String,
                 game: MutableLiveData<GameModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, game: GameModel)
    fun delete(userid:String, gameid: String)
    fun update(userid:String, gameid: String, game: GameModel)
}