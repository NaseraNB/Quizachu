package com.example.quizachu


import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.Objects

class Menu : AppCompatActivity() {

    //creem unes variables per comprovar ususari i authentificació
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;
    lateinit var tancarSessio: Button
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    lateinit var canviarImatge: Button
    //reference serà el punter que ens envia a la base de dades de jugadors
    lateinit var reference: DatabaseReference

    lateinit var puntuacio: TextView
    lateinit var uid: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView
    lateinit var edat: TextView
    lateinit var poblacio: TextView

    private var nivell ="1"

    lateinit var imatgePerfil: ShapeableImageView

    lateinit var referenciaDeAlmacenamamiento: StorageReference
    private var ruta: String = "FotosDePerfil/*"

    // PERMISOS
    private var CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO = 200
    private var CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN = 300
    private var CAMERA_PERMISSION_REQUEST_CODE = 103
    private var CODIGO_PARA_ABRIR_CAMARA = 104



    // MATRICES
    private lateinit var permisosDeAlmacenamiento: Array<String>
    private lateinit var imagen_uri: Uri
    private var perfil: String = "Imatge"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //Aquí creem un tipus de lletra a partir de una font
        val tf = Typeface.createFromAsset(assets,"Komigo3D-Regular.ttf")

        tancarSessio =findViewById<Button>(R.id.tancarSessio)
        CreditsBtn =findViewById<Button>(R.id.CreditsBtn)
        PuntuacionsBtn =findViewById<Button>(R.id.PuntuacionsBtn)
        jugarBtn =findViewById<Button>(R.id.jugarBtn)
        canviarImatge =findViewById<Button>(R.id.canviarImatge)

        puntuacio =findViewById(R.id.puntuacio)
        uid =findViewById(R.id.uid)
        correo =findViewById(R.id.correo)
        nom =findViewById(R.id.nom)
        edat =findViewById(R.id.edatMenu)
        poblacio =findViewById(R.id.poblacioMenu)

        imatgePerfil=findViewById(R.id.imagenUser)

        referenciaDeAlmacenamamiento = FirebaseStorage.getInstance().getReference()
        permisosDeAlmacenamiento = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


        consulta()

        tancarSessio.setOnClickListener(){
            tancalaSessio()
        }

        canviarImatge.setOnClickListener(){
            Toast.makeText(this,"EDITAR", Toast.LENGTH_SHORT).show()
            actualizarFotoPerfil()
        }

        CreditsBtn.setOnClickListener(){
            Toast.makeText(this,"Credits", Toast.LENGTH_SHORT).show()
        }
        PuntuacionsBtn.setOnClickListener(){
            val intent= Intent(this, RecyclerView::class.java)
            startActivity(intent)
            finish()
        }
        jugarBtn.setOnClickListener(){
            //hem d'enviar el id, el nom i el contador, i el nivell
            var Uids : String = uid.getText().toString()
            var noms : String = nom.getText().toString()
            var puntuacios : String = puntuacio.getText().toString()
            var nivells : String = nivell

                val intent= Intent(this, seleccio_De_Nivells::class.java)
                intent.putExtra("UID",Uids)
                intent.putExtra("NOM",noms)
                intent.putExtra("PUNTUACIO",puntuacios)
                intent.putExtra("NIVELL",nivells)
                startActivity(intent)
                finish()


        }

        auth= FirebaseAuth.getInstance()
        user =auth.currentUser

    }

    // Aquest mètode s'executarà quan s'obri el minijoc
    override fun onStart() {
        Usuarilogejat()
        super.onStart()
    }

    private fun Usuarilogejat()
    {
        if (user !=null)
        {
            Toast.makeText(this,"Jugador logejat",
                Toast.LENGTH_SHORT).show()
        }
        else
        {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun tancalaSessio() {
        auth.signOut() //tanca la sessió
        //va a la pantalla inicial
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun consulta() {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://quizachu-default-rtdb.europe-west1.firebasedatabase.app/")
        var bdreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
        bdreference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // Look for a child node with matching email for efficiency
                for (dataSnapshot in snapshot.children) {
                    if (dataSnapshot.child("Email").getValue().toString().equals(user?.email)) {
                        puntuacio.setText(dataSnapshot.child("Puntuacio").getValue().toString())
                        uid.setText(dataSnapshot.child("Uid").getValue().toString())
                        correo.setText(dataSnapshot.child("Email").getValue().toString())
                        nom.setText(dataSnapshot.child("Nom").getValue().toString())
                        edat.setText(dataSnapshot.child("Edat").getValue().toString())
                        poblacio.setText(dataSnapshot.child("Població").getValue().toString())
                        nivell = dataSnapshot.child("Nivell").getValue().toString()

                        val imatge: String = dataSnapshot.child("Imatge").getValue().toString()
                        try {
                            Picasso.get().load(imatge).into(imatgePerfil)
                        } catch (e: Exception) {
                            Picasso.get().load(R.drawable.imagen_user_pordefecto).into(imatgePerfil)
                        }
                        break  // Exit the loop once user data is found
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "ERROR DATABASE CANCEL")
            }
        })
    }

    private fun actualizarFotoPerfil(){
        perfil = "Imatge"
        mostrarOpcionesSeleccionImagen()
    }

    private fun mostrarOpcionesSeleccionImagen() {
        val opciones = arrayOf("Galería", "Cámara")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar imagen de:")
        builder.setItems(opciones) { dialogInterface, i ->
            when (i) {
                0 -> {
                    // Galería
                    if (!comprobarPermisosAlmacenamiento()) {
                        // Si los permisos de almacenamiento no están habilitados
                        solicitarPermisosAlmacenamiento()
                    } else {
                        // Si los permisos de almacenamiento están habilitados
                        elegirImagenDeGaleria()
                    }
                }
                1 -> {
                    // Cámara
                    if (checkCameraPermissions()) {
                        abrirCamara()
                    } else {
                        requestCameraPermissions()
                    }
                }
            }
        }
        builder.create().show()
    }



    private fun solicitarPermisosAlmacenamiento() {
        val permisos = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(this, permisos, CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO)
    }


    private fun comprobarPermisosAlmacenamiento(): Boolean {
        val resultadoAlmacenamiento = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val resultadoCamara = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        return resultadoAlmacenamiento && resultadoCamara
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO) {
            // Verifica si los permisos de almacenamiento y cámara están concedidos
            if (comprobarPermisosAlmacenamiento()) {
                // Los permisos están concedidos, proceder a elegir una imagen
                elegirImagenDeGaleria()
            } else {
                Toast.makeText(this, "Necesitas permisos de almacenamiento y cámara para acceder a la galería", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun elegirImagenDeGaleria() {
        val intentGaleria = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intentGaleria.type = "image/*"
        startActivityForResult(intentGaleria, CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO) {
            // Verifica si los permisos de almacenamiento están concedidos
            if (comprobarPermisosAlmacenamiento()) {
                // Los permisos están concedidos, proceder a elegir una imagen de la galería
                elegirImagenDeGaleria()
            } else {
                Toast.makeText(this, "Necesitas permisos de almacenamiento para acceder a la galería", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                // Guarda la URI de la imagen seleccionada
                imagen_uri = uri
                // Sube la imagen seleccionada al almacenamiento de Firebase
                subirFoto(imagen_uri)
            }
        } else if (requestCode == CODIGO_PARA_ABRIR_CAMARA && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // Convertir la imagen a URI
            val imageUri = getImageUri(imageBitmap)
            // Guarda la URI de la imagen seleccionada
            imagen_uri = imageUri
            // Sube la imagen seleccionada al almacenamiento de Firebase
            subirFoto(imagen_uri)
        }
    }


    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun subirFoto(imagenUri: Uri?) {
        val rutaDeArchivo = "$ruta/$perfil${user?.uid}" // Concatenación del ID de usuario
        val storageReference: StorageReference = referenciaDeAlmacenamamiento.child(rutaDeArchivo)
        storageReference.putFile(imagenUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                uriTask.addOnSuccessListener { downloadUri ->
                    val userDataUpdates = HashMap<String, Any>()
                    userDataUpdates["$perfil"] = downloadUri.toString()

                    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://quizachu-default-rtdb.europe-west1.firebasedatabase.app")
                    val reference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
                    val userRef = reference.child(user?.uid ?: "")

                    userRef.updateChildren(userDataUpdates)
                        .addOnSuccessListener {
                            Toast.makeText(this@Menu, "La imagen ha sido cambiada correctamente", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this@Menu, "Ha ocurrido un error: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@Menu, "Algo ha salido mal: $e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun abrirCamara() {
        val intentCamara = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intentCamara.resolveActivity(packageManager) != null) {
            startActivityForResult(intentCamara, CODIGO_PARA_ABRIR_CAMARA)
        } else {
            Toast.makeText(this, "La cámara no está disponible", Toast.LENGTH_SHORT).show()
        }
    }


    private fun checkCameraPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }


}