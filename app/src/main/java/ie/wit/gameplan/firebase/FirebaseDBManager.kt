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

        database.child("user-games").child(userid)
            .child(gameid).get().addOnSuccessListener {
                game.value = it.getValue(GameModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun findById(gameid: String, game: MutableLiveData<GameModel>) {

        database.child("games")
            .child(gameid).get().addOnSuccessListener {
                game.value = it.getValue(GameModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
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

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/games/$gameid"] = null
        childDelete["/user-games/$userid/$gameid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, gameid: String, game: GameModel) {

        val gameValues = game.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["games/$gameid"] = gameValues
        childUpdate["user-games/$userid/$gameid"] = gameValues

        database.updateChildren(childUpdate)
    }
}