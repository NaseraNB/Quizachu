package com.example.quizachu
import java.io.Serializable
data class Jugadors (
    var Uid: String = "",
    var Nom: String = "",
    var Puntuacio: String = "",
    var Imatge: String = "",
    var Email: String = "",
    var Edat: String = "",
    var Població: String = ""
): Serializable {
}