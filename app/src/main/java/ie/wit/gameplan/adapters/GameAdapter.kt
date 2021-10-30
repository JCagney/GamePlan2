package ie.wit.gameplan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.gameplan.databinding.CardGameBinding
import ie.wit.gameplan.models.GameModel

interface GameListener {
    fun onGameClick(game: GameModel)
}

class GameAdapter constructor(private var games: List<GameModel>, private val listener: GameListener) :
    RecyclerView.Adapter<GameAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGameBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val game = games[holder.adapterPosition]
        holder.bind(game, listener)
    }

    override fun getItemCount(): Int = games.size

    class MainHolder(private val binding : CardGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: GameModel, listener: GameListener) {
            binding.gameTitle.text = game.title
            binding.gameDate.text = game.date
            binding.root.setOnClickListener { listener.onGameClick(game) }
        }
    }
}