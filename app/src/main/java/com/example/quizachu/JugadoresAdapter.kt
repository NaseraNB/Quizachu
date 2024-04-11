package com.example.quizachu

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.example.quizachu.R

// JugadoresAdapter que extèn RecyclerView.Adapter<JugadoresAdapter.ViewHolder>
class JugadoresAdapter(private val context: Context, private val jugadoresList: List<Jugadors>) :
    RecyclerView.Adapter<JugadoresAdapter.ViewHolder>() {

    // Classe interna ViewHolder que extèn RecyclerView.ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declaració dels elements de la interfície
        val imageView: ImageView = itemView.findViewById(R.id.imatgeJugador)
        val nombreTextView: TextView = itemView.findViewById(R.id.nomJugador)
        val puntuacionTextView: TextView = itemView.findViewById(R.id.puntuacioJugador)
        val emailJugador: TextView = itemView.findViewById(R.id.emailJugador)
        val edadJugador: TextView = itemView.findViewById(R.id.edadJugador)
        val poblacionJugador: TextView = itemView.findViewById(R.id.poblacionJugador)

        init {
            // Acció que s'executa quan es fa clic a un element del RecyclerView
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val jugador = jugadoresList[position]
                    // Iniciar DetallesJugadorActivity i enviar dades mitjançant un Intent
                    val intent = Intent(itemView.context, DetallesJugadorActivity::class.java).apply {
                        putExtra("Imagen", jugador.Imatge)
                        putExtra("Nom", jugador.Nom)
                        putExtra("Email", jugador.Email)
                        putExtra("Puntuacio", jugador.Puntuacio)
                        putExtra("Edat", jugador.Edat)
                        putExtra("Població", jugador.Població)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    // Mètode onCreateViewHolder, crea una nova instància de ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_jugador, parent, false)
        return ViewHolder(view)
    }

    // Mètode onBindViewHolder, vincula les dades amb els elements de la interfície
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jugador = jugadoresList[position]
        holder.nombreTextView.text = jugador.Nom
        holder.puntuacionTextView.text = jugador.Puntuacio
        holder.emailJugador.text = jugador.Email
        holder.edadJugador.text = jugador.Edat
        holder.poblacionJugador.text = jugador.Població

        // Carrega la imatge del jugador amb Picasso, o una imatge per defecte si no hi ha imatge personalitzada
        if (jugador.Imatge.isNotEmpty()) {
            Picasso.get().load(jugador.Imatge).into(holder.imageView)
        } else {
            Picasso.get().load(R.drawable.imagen_user_pordefecto).into(holder.imageView)
        }
    }

    // Mètode getItemCount, retorna la quantitat d'elements a mostrar
    override fun getItemCount(): Int {
        return jugadoresList.size
    }
}
