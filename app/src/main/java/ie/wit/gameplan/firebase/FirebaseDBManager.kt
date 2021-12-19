package ie.wit.gameplan.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.models.GameStore
import timber.log.Timber

object FirebaseDBManager : GameStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(gameList: MutableLiveData<List<GameModel>>) {
        database.child("games")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Game error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<GameModel>()
                    val children = snapshot.children
                    children.forEach {
                        val game = it.getValue(GameModel::class.java)
                        localList.add(game!!)
                    }
                    database.child("games")
                        .removeEventListener(this)

                    gameList.value = localList
                }
            })
    }

    override fun findAll(userid: String, gameList: MutableLiveData<List<GameModel>>) {

        database.child("user-games").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Game error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<GameModel>()
                    val children = snapshot.children
                    children.forEach {
                        val game = it.getValue(GameModel::class.java)
                        localList.add(game!!)
                    }
                    database.child("user-games").child(userid)
                        .removeEventListener(this)

                    gameList.value = localList
                }
            })
    }

    override fun findById(userid: String, gameid: String, game: MutableLiveData<GameModel>) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, game: GameModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("games").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        game.uid = key
        val gameValues = game.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/games/$key"] = gameValues
        childAdd["/user-games/$uid/$key"] = gameValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, gameid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, gameid: String, game: GameModel) {
        TODO("Not yet implemented")
    }
}