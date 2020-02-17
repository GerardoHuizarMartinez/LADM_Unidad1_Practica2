package mx.edu.ittepic.ladm_unidad1_practica2_gerardo_huizar

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if(nombre.text.isEmpty() || frase.text.isEmpty()){
                mensaje("No deje campos vacios")
            }
            guardarArchivo()
        }

        button2.setOnClickListener {
            if(nombre.text.isEmpty()){
                mensaje("Ingrese el nombre del archivo a abrir")
            }
            abrirArchivo()

        }

    }//Fin del on create

    fun abrirArchivo(){
        if (radioButton.isChecked){
            try {
                var flujoEntrada = BufferedReader(InputStreamReader(openFileInput( nombre.text.toString())))
                var data = flujoEntrada.readLine()

                agregaTexto(data,nombre.text.toString())
                flujoEntrada.close()


            }catch (error : IOException){
                mensaje(error.message.toString())
            }//Fin del catch
        }//Fin del primer radiobutton que agrega en la memoria Interna

        if (radioButton2.isChecked){
            if(noSD()){
                mensaje("No esta la SD externa")
                return
            }

            try {
                //Ruta y nombre del archivo se ocupan para referenciar a la memoria
                var rutaSD = Environment.getExternalStorageDirectory()
                var datosArchivo = File(rutaSD.absolutePath,nombre.text.toString())

                var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))


                var data = flujoEntrada.readLine()


                agregaTexto(data,nombre.text.toString())
                flujoEntrada.close()



            }catch (error: IOException){
                mensaje(error.message.toString())
            }
        }
    }

    fun guardarArchivo(){

        if(radioButton.isChecked){
            try {
                var flujoSalida = OutputStreamWriter(openFileOutput(nombre.text.toString(), Context.MODE_PRIVATE))
                var data = frase.text.toString()

                flujoSalida.write(data)
                flujoSalida.flush()
                flujoSalida.close()

                mensaje("EXITO se guardo el archivo correctamente")
                agregaTexto("","")

            }catch (error: IOException){
                mensaje("Asigne un nombre al archivo")
                //mensaje(error.message.toString())
            }

        }//Fin del radioButon interna


        //RadioButton2 que guarda pero dentro de la memoria externa SD
        if(radioButton2.isChecked){

            if(noSD()){
                mensaje("No hay memoria Externa")
                return
            }


            //Si entra al siguiente if es porque aun no cuenta con permisos
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED ){

                //Para solicitar los permisos se usa
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),0)

            }

            try {
                //Ruta y nombre del archivo se ocupan para referenciar a la memoria
                var nomb = nombre.text.toString()
                var rutaSD = Environment.getExternalStorageDirectory()
                var datosArchivo = File(rutaSD.absolutePath,nomb)

                var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))

                //Codigo para referenciar a la memoria externa


                var data = frase.text.toString()

                flujoSalida.write(data)
                flujoSalida.flush()
                flujoSalida.close()

                mensaje("EXITO se guardo el archivo correctamente")
                agregaTexto("","")

            }catch (error: IOException){
                mensaje(error.message.toString())
            }

        }//Fin del radioButton SD

    }//Fin del metodo para guardar archivo

    fun mensaje(m: String){
        AlertDialog.Builder(this)
            .setTitle("Mensaje")
            .setMessage(m)
            .setPositiveButton("Ok"){d,i->}
            .show()
    }

    fun agregaTexto(t1:String,t2:String){
        frase.setText(t1)
        nombre.setText(t2)
    }

    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()

        if(estado!= Environment.MEDIA_MOUNTED){
            return true
        }

        return false
    }

}//Fin del activity
