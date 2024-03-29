import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizachu.Jugadors
import com.squareup.picasso.Picasso
import com.example.quizachu.R


class JugadoresAdapter(private val jugadoresList: List<Jugadors>) :
    RecyclerView.Adapter<JugadoresAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imatgeJugador)
        val nombreTextView: TextView = itemView.findViewById(R.id.nomJugador)
        val puntuacionTextView: TextView = itemView.findViewById(R.id.puntuacioJugador)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_jugador, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jugador = jugadoresList[position]
        holder.nombreTextView.text = jugador.Nom
        holder.puntuacionTextView.text = jugador.Puntuacio

        try {
            Picasso.get().load(jugador.Imatge).into(holder.imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return jugadoresList.size
    }
}
