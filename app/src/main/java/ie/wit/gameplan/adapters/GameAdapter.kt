package ie.wit.gameplan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.gameplan.databinding.CardGameBinding
import ie.wit.gameplan.models.GameModel
import ie.wit.gameplan.ui.list.GameListViewModel

interface GameListener {
    fun onGameClick(game: GameModel)
}

class GameAdapter constructor(private var games: ArrayList<GameModel>, private val listener: GameListener, private val readOnly: Boolean) :
    RecyclerView.Adapter<GameAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGameBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding, readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val game = games[holder.adapterPosition]
        holder.bind(game, listener)
    }

    override fun getItemCount(): Int = games.size

    class MainHolder(private val binding : CardGameBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(game: GameModel, listener: GameListener) {
            //binding.gameTitle.text = game.title
            //binding.gameDate.text = game.date

            binding.root.tag = game

            binding.game = game

            binding.root.setOnClickListener { listener.onGameClick(game)}
            binding.executePendingBindings()
        }
    }

    fun removeAt(position: Int) {
        games.removeAt(position)
        notifyItemRemoved(position)
    }
}