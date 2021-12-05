package ie.wit.gameplan.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object GameManager : GameStore {

    val games = ArrayList<GameModel>()

    override fun findAll(): List<GameModel> {
        return games
    }

    override fun create(game: GameModel) {
        game.id = getId()
        games.add(game)
        logAll()
    }

    override fun update(game: GameModel) {
        var foundGame: GameModel? = games.find { g -> g.id == game.id }
        if (foundGame != null) {
            foundGame.title = game.title
            foundGame.description = game.description
            foundGame.lat = game.lat
            foundGame.lng = game.lng
            foundGame.zoom = game.zoom
            foundGame.date = game.date
            logAll()
        }
    }

    override fun delete(id: Long)
    {
        var foundGame: GameModel? = games.find { g -> g.id == id }
        games.remove(foundGame)
        logAll()

    }

    fun logAll() {
        games.forEach{ i("${it}") }
    }
}