package com.example.cryptoboy

import android.content.Context
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myAudioRecorder: MediaRecorder? = null
        val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

//        val fileToRead = "testrrr.mp3"

//        test()

/*        writeToFile("testing encryption", this)
        val encryptedFile = EncryptedFile.Builder(
            this,
            test(),
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        println("tridiv " + encryptedFile.openFileInput())*/

    }

    private fun test(): File {
        val fileName = "${cacheDir.absolutePath}/complainTest.mp3"
//println(this.cacheDir)
        //        if (dir.exists()) {
//            for (f in dir.listFiles()) {
//                println("tridiv  Files" + "FileName:" + f.name)
//            }
//        }
        return File(fileName)
    }


    private fun writeToFile(data: String, context: Context) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput("config.txt", MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {

        }
    }

    private fun readFromFile(context: Context): String? {
        var ret = ""
        try {
            val inputStream: InputStream? = context.openFileInput("config.txt")
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String? = ""
                val stringBuilder = StringBuilder()
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append("\n").append(receiveString)
                }
                inputStream.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
            println("login activity" + "File not found: " + e.toString())
        } catch (e: IOException) {
            println("login activity" + "Can not read file: $e")
        }
        return ret
    }

    // updates the visualizer every 50 milliseconds

}